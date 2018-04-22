package com.concur.meta.client.conversion;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.concur.meta.client.conversion.conveters.spring.ConversionService;
import com.concur.meta.client.conversion.conveters.spring.support.DefaultConversionService;
import com.concur.meta.client.utils.GenericsUtils;
import com.concur.meta.client.utils.TypeUtils;

/**
 * 简单的spring风格转换器实现
 * @author jake
 */
public class SimpleConverterService implements ConverterService {
	
	private final ConcurrentMap<ConverterCacheKey, Converter<?, ?>> converterMap = new ConcurrentHashMap<ConverterCacheKey, Converter<?,?>>();

	private final List<CustomConverter> customConverters = new CopyOnWriteArrayList<CustomConverter>();

	private ConversionService conversionService = new DefaultConversionService();
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> T convert(Object source, Class<T> targetType, Object...objects) {
	    if (source == null) {
	        return null;
        }
	    if (source.getClass().equals(targetType) || targetType == Object.class) {
	        return (T) source;
        }

        // 使用自定义转换器
        for (CustomConverter customConverter : customConverters) {
	        if (customConverter.support(targetType)) {
	            return (T) customConverter.convert(source, targetType, objects);
            }
        }

        // 使用内置转换器
		ConverterCacheKey converterCacheKey = this.buildConverterCacheKey(source, targetType);
		Converter conveter = converterMap.get(converterCacheKey);
		
		if(conveter != null){
			return (T)conveter.convert(source, targetType, objects);
		} else if(conversionService != null && (objects == null || objects.length == 0)){//没有就找spring的转换器
			return (T) conversionService.convert(source, targetType);
		}
		
		return null;
	}

    @Override
    public <T> T convert(Object source, Type targetType, Object... objects) {
	    if (source == null) {
	        return null;
        }
        Class<?> targetClass =  TypeUtils.getRawClass(targetType);
        if (source.getClass().equals(targetClass) || targetClass == Object.class) {
            return (T) source;
        }

        // 使用自定义转换器
        for (CustomConverter customConverter : customConverters) {
            if (customConverter.support(targetType)) {
                return (T) customConverter.convert(source, targetType, objects);
            }
        }

        // 使用内置转换器
        ConverterCacheKey converterCacheKey = this.buildConverterCacheKey(source, targetClass);
        Converter conveter = converterMap.get(converterCacheKey);

        if(conveter != null){
            return (T) conveter.convert(source, targetType, objects);
        } else if(conversionService != null && objects == null ||objects.length == 0){//没有就找spring的转换器
            return (T) conversionService.convert(source, targetClass);
        }

        return null;
    }

    @Override
	public <T> List<T> convertCollection(Collection<?> sourceCollection, Class<T> targetType, Object... objects) {
		if(sourceCollection != null && !sourceCollection.isEmpty()){
			List<T> list = new ArrayList<T>(sourceCollection.size());
			for(Object source : sourceCollection){
				T target = this.convert(source, targetType, objects);
				if(target != null){
					list.add(target);
				}
			}
			return list;
		}
		return Collections.<T>emptyList();
	}

	@Override
	public void registerConverter(Converter<?, ?> converter) {
        Class[] genericTypes = GenericsUtils.getSuperClassGenricTypes(converter.getClass());
		if(genericTypes != null && genericTypes.length >= 2){
			this.registerConverter((Class<?>) genericTypes[0], (Class<?>) genericTypes[1], converter);
		} else {
            this.registerConverter(null, null, converter);
        }
	}


    @Override
    public void registerConverter(Class<?> sourceType, Class<?> targetType, Converter<?, ?> converter) {
	    if (converter instanceof CustomConverter) {
            this.customConverters.add((CustomConverter) converter);
        } else {
            ConverterCacheKey converterCacheKey = this.buildConverterCacheKey(sourceType, targetType);
            converterMap.put(converterCacheKey, converter);
        }
    }

    /**
     * 构建转换器的key
     * @param source 转换对象
     * @param targetClazz 目标类
     * @return
     */
	private ConverterCacheKey buildConverterCacheKey(Object source, Class<?> targetClazz){
		return new ConverterCacheKey(source.getClass(), targetClazz);
	}

    /**
     * 构建转换器的key
     * @param sourceClazz 源类
     * @param targetClazz 目标类
     * @return
     */
	private ConverterCacheKey buildConverterCacheKey(Class<?> sourceClazz, Class<?> targetClazz){
		return new ConverterCacheKey(sourceClazz, targetClazz);
	}

    /**
     * 转换器的key内部类定义
     */
	private static final class ConverterCacheKey {

		private final Class<?> sourceType;
		
		private final Class<?> targetType;
		
		public ConverterCacheKey(Class<?> sourceType, Class<?> targetType) {
			this.sourceType = sourceType;
			this.targetType = targetType;
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof ConverterCacheKey)) {
				return false;
			}
			ConverterCacheKey otherKey = (ConverterCacheKey) other;
			return this.sourceType.equals(otherKey.sourceType) && this.targetType.equals(otherKey.targetType);
		}

        @Override
		public int hashCode() {
			return this.sourceType.hashCode() * 29 + this.targetType.hashCode();
		}

        @Override
		public String toString() {
			return "ConverterCacheKey [sourceType = " + this.sourceType + ", targetType = " + this.targetType + "]";
		}
	}

}