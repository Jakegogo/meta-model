package com.concur.meta.client.conversion;

import java.lang.reflect.Type;

/**
 * 自定义转换器适配类(具体的转换器实现此类)
 * 通过support()判断是否调用次转换器
 * @author jake
 */
public abstract class CustomConverter<S, T> implements Converter<S, T> {

    /**
     * 是否支持转换
     * @param type Type
     * @return
     */
    public abstract boolean support(Type type);

    @Override
    public T convert(S source, Object... objects) {
        return null;
    }

    @Override
    public T convert(S source, Type targetType, Object... objects) {
        return this.convert(source, objects);
    }


}