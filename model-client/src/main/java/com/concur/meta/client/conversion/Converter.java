/**
 * 
 */
package com.concur.meta.client.conversion;

import java.lang.reflect.Type;

/**
 * 抽象的转换者接口
 * @author jake
 *
 */
public interface Converter<S, T>{

    /**
     * 转换
     * @param source 源对象
     *@param objects 附加参数  @return
     */
    T convert(S source, Object... objects);

	/**
	 * 转换
     * @param source 源对象
     * @param targetType 目标类
     * @param objects 附加参数  @return
     */
	T convert(S source, Type targetType, Object... objects);
	
}