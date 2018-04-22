package com.concur.meta.client.annotation.parser;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.concur.meta.client.annotation.LColumn;
import com.concur.meta.client.annotation.Version;
import com.concur.meta.client.constants.ParamKeys;
import com.concur.meta.client.utils.ReflectionUtils;
import com.concur.meta.client.annotation.LModel;
import com.concur.meta.client.utils.StringUtils;

/**
 * LModel注解解析器
 *
 * @author yongfu.cyf
 * @create 2017-06-29 下午8:23
 **/
public class LModelAnnoParser {

    private static final ConcurrentMap<Class<?>, Map<String, Serializable>> CACHE
                = new ConcurrentHashMap<Class<?>, Map<String, Serializable>>();

    /**
     * 解析LModel的注解信息
     * @param clazz 实体类
     * @return
     */
    public static Map<String, Serializable> parse(Class<?> clazz) {
        Map<String, Serializable> result = CACHE.get(clazz);
        if (result != null) {
            return result;
        }

        final Map<String, Serializable> lmodelInfo = new HashMap<String, Serializable>();
        if (clazz.isAnnotationPresent(LModel.class)) {
            LModel lmodel = clazz.getAnnotation(LModel.class);
            lmodelInfo.put(ParamKeys.LMODEL_TABLE_NAME, lmodel.table());
            lmodelInfo.put(ParamKeys.LMODEL_DEFAULT_DATASOURCE, lmodel.defaultDateSource());
        }

        ReflectionUtils.doWithFields(clazz, new ReflectionUtils.FieldCallback() {

            @SuppressWarnings("unchecked")
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                if (field.isAnnotationPresent(Version.class)) {
                    String keyName = field.getName();
                    if (field.isAnnotationPresent(LColumn.class)) {
                        LColumn lColumnAnno = field.getAnnotation(LColumn.class);
                        if (StringUtils.isNotBlank(lColumnAnno.name())) {
                            keyName = lColumnAnno.name();
                        }
                    }
                    lmodelInfo.put(ParamKeys.MODEL_UPDATE_CAS_VERSION_FIELD, keyName);
                }
            }
        });

        CACHE.putIfAbsent(clazz, lmodelInfo);
        return CACHE.get(clazz);
    }

}
