package com.concur.meta.client.conversion;

import java.lang.reflect.Type;

/**
 * 转换器适配类(具体的转换器实现此类)
 * @author jake
 */
public abstract class ConverterAdapter<S, T> implements Converter<S, T> {

    protected ConverterService converterService;

    public ConverterAdapter setConverterService(ConverterService converterService) {
        this.converterService = converterService;
        return this;
    }

    @Override
    public T convert(S source, Object... objects) {
        return null;
    }

    @Override
    public T convert(S source, Type targetType, Object... objects) {
        return this.convert(source, objects);
    }

    protected void postRegister() {
    }

}