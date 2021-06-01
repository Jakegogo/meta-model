package com.concur.meta.client.utils;

import com.alibaba.fastjson.JSONObject;
import com.concur.meta.client.conversion.QueryJsonResult;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;

import com.concur.meta.client.annotation.LColumn;
import com.concur.meta.client.annotation.LModel;
import com.concur.meta.client.constants.ColumnType;
import com.concur.meta.client.conversion.Converter;
import com.concur.meta.client.conversion.ConverterRegistry;
import com.concur.meta.client.conversion.ConverterService;
import com.concur.meta.client.conversion.SimpleConverterService;
import com.concur.meta.client.domain.BaseModel;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.result.ClientResultCode;
import org.apache.commons.beanutils.converters.DateConverter;

/**
 * 数据转换工具类
 *
 * @author yongfu.cyf
 * @create 2017-06-29 上午10:18
 **/
public class ConvertUtils {

    /**
     * 数据转换服务
     */
    private static ConverterService converterService = new SimpleConverterService();

    static {
        try {
            new ConverterRegistry(converterService).init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // apache转换注册Date支持
        org.apache.commons.beanutils.ConvertUtils
                .register(new DateConverter(null), java.util.Date.class);
    }

    /**
     * 注册转换器
     * @param converter 转换器
     */
    public static void registerConverter(Converter<?, ?> converter) {
        converterService.registerConverter(converter);
    }

    /**
     * 注册转换器
     * @param sourceType 原类型
     * @param targetType 目标类型
     * @param converter 转换器
     */
    public static void registerConverter(Class<?> sourceType, Class<?> targetType, Converter<?, ?> converter) {
        converterService.registerConverter(sourceType, targetType, converter);
    }

    /**
     * 对象转换成Map
     * @param object 实体对象
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Map<String, Serializable> toMap(Serializable object) throws IntrospectionException,
        IllegalAccessException, IllegalArgumentException,
        InvocationTargetException {
        if (object == null) {
            return null;
        }
        if (object instanceof Map) {
            return (Map<String, Serializable>) object;
        }

        Map<String, Serializable> objectAsMap = new HashMap<String, Serializable>();
        BeanInfo info = Introspector.getBeanInfo(object.getClass());
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            Method reader = pd.getReadMethod();
            if (reader != null) {
                Serializable value = (Serializable) reader.invoke(object);
                objectAsMap.put(pd.getName(), value);
            }
        }
        return objectAsMap;
    }

    /**
     * 对象转换成Map bean
     * @param object 实体对象
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Map<String, Serializable> toMapBean(final Serializable object) throws IntrospectionException,
        IllegalAccessException, IllegalArgumentException,
        InvocationTargetException {
        return toMapBean(object, null);
    }

    /**
     * 对象转换成Map bean
     * @param object 实体对象
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Map<String, Serializable> toMapBean(final Serializable object, final FieldFilter fieldFilter) throws IntrospectionException,
        IllegalAccessException, IllegalArgumentException,
        InvocationTargetException {
        if (object == null) {
            return null;
        }
        if (object instanceof Map) {
            return (Map<String, Serializable>) object;
        }

        final Map<String, Serializable> objectAsMap = new HashMap<String, Serializable>();

        // 遍历属性信息
        ReflectionUtils.doWithFields(object.getClass(), new ReflectionUtils.FieldCallback() {

            @SuppressWarnings("unchecked")
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                try {
                    int modifiers = field.getModifiers();
                    if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers) || Modifier.isTransient(modifiers)) {
                        return;
                    }

                    Class<?> fieldClass = field.getType();
                    // 暂时不支持级联持久化
                    if (BaseModel.class.isAssignableFrom(fieldClass)) {
                        return;
                    }
                    if (fieldClass.isAnnotationPresent(LModel.class)) {
                        return;
                    }

                    // 字段注解处理
                    boolean isJson = false;
                    ReflectionUtils.makeAccessible(field);
                    String keyName = field.getName();
                    if (field.isAnnotationPresent(LColumn.class)) {
                        LColumn lColumnAnno = field.getAnnotation(LColumn.class);
                        if (StringUtils.isNotBlank(lColumnAnno.name())) {
                            keyName = lColumnAnno.name();
                        }
                        if (lColumnAnno.type() == ColumnType.JSON) {
                            isJson = true;
                        }
                    }

                    Serializable value = (Serializable) ReflectionUtils.getField(field, object);
                    if (fieldFilter != null && !fieldFilter.doFilter(field, value)) {
                        return;
                    }

                    if (objectAsMap.containsKey(keyName)) {
                        // 保证子类属性能覆盖父类属性值
                        return;
                    }
                    if (value == null && !StringUtils.isEmpty(keyName)) {
                        objectAsMap.put(keyName, value);
                        return;
                    }

                    if (value == null || ClassUtils.isPrimitiveOrWrapper(value.getClass()) || Date.class.isAssignableFrom(value.getClass())) {
                    } else if (isJson) {
                        value = JSON.toJSONString(value);
                    } else {
                        value = converterService.convert(value, String.class);
                    }
                    objectAsMap.put(keyName, value);
                } catch (RuntimeException e) {
                    throw new ExecuteException(ClientResultCode.DATA_CONVERT_EXCEPTION, field.toString(), e);
                }
            }
        });

        return objectAsMap;
    }


    /**
     * Map转换成PoJo对象
     * @param clazz PoJo类型
     * @param map Map数据
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     */
    public static <T> T toPoJo(Class<T> clazz, final Map map)
            throws IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException, NoSuchFieldException {
        if (map == null) {
            return null;
        }

        //针对配置结果集类型为QueryJsonResult 直接将结果转换成json类型即可
        if (clazz.equals(QueryJsonResult.class)){
            String data = JSONObject.toJSONString(map);
            System.out.println(data);
            T instance = clazz.newInstance();

            Field f = clazz.getDeclaredField("jsonData");
            f.setAccessible(true);
            f.set(instance, data);
            return instance;
        }

        final T instance = clazz.newInstance();
        if (Map.class.isAssignableFrom(clazz)) {
            ((Map) instance).putAll(map);
            return instance;
        }

        if (ClassUtils.isPrimitiveOrWrapper(clazz)) {
            for (Object entry : map.entrySet()) {
                Entry<Serializable, Serializable> resultEntry = (Entry<Serializable, Serializable>) entry;
                return converterService.convert(resultEntry.getValue(), clazz);
            }
        }

        // 遍历属性信息
        ReflectionUtils.doWithFields(clazz, new ReflectionUtils.FieldCallback() {

            @SuppressWarnings("unchecked")
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                try {
                    int modifiers = field.getModifiers();
                    if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers) || Modifier.isTransient(
                        modifiers)) {
                        return;
                    }

                    String keyName = field.getName();

                    // 字段注解处理
                    boolean isJson = false;
                    if (field.isAnnotationPresent(LColumn.class)) {
                        LColumn lColumnAnno = field.getAnnotation(LColumn.class);
                        if (StringUtils.isNotBlank(lColumnAnno.name())) {
                            keyName = lColumnAnno.name();
                        }
                        if (lColumnAnno.type() == ColumnType.JSON) {
                            isJson = true;
                        }
                    }

                    Object value = map.get(keyName);
                    if (value == null) {
                        return;
                    }

                    Object targetValue = value;
                    if (isJson) {
                        targetValue = JSON.parseObject((String)value, field.getGenericType());
                    } else if (!field.getType().isAssignableFrom(value.getClass())) {
                        targetValue = converterService.convert(value, field.getGenericType());
                    }
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, instance, targetValue);
                } catch (RuntimeException e) {
                    throw new ExecuteException(ClientResultCode.DATA_CONVERT_EXCEPTION, field.toString(), e);
                }
            }
        });

        return instance;
    }


    /**
     * 将指定的对象转换为指定的类对象
     * @param <T>
     * @param source 需要转换的对象
     * @param targetType 目标类
     * @return
     */
    public static <T> T convert(Object source, Type targetType, Object... objects) {
        return converterService.convert(source, targetType, objects);
    }


}
