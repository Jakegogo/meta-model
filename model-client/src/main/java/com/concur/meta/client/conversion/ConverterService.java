package com.concur.meta.client.conversion;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * 转换服务接口
 * @author jake
 *
 */
public interface ConverterService {
	
	
	/**
	 * 将指定的对象转换为指定的类对象
	 * @param <T>
	 * @param source 需要转换的对象
	 * @param targetType 目标类
	 * @return
	 */
	<T> T convert(Object source, Class<T> targetType, Object... objects);

    /**
     * 将指定的对象转换为指定的类对象
     * @param <T>
     * @param source 需要转换的对象
     * @param targetType 目标类
     * @return
     */
    <T> T convert(Object source, Type targetType, Object... objects);
	
	
	/**
	 * 将指定的对象转换为指定的类对象
	 * @param <T>
	 * @param sourceCollection 需要转换的集合
	 * @param targetType 目标类
	 * @return
	 */
	<T> List<T> convertCollection(Collection<?> sourceCollection, Class<T> targetType, Object... objects);
	
	
	/**
	 * 注册转换器
	 * @param converter 转换器
	 */
	void registerConverter(Converter<?, ?> converter);

    /**
     * 注册转换器
     * @param sourceType 原类型
     * @param targetType 目标类型
     * @param converter 转换器
     */
    void registerConverter(Class<?> sourceType, Class<?> targetType, Converter<?, ?> converter);
}