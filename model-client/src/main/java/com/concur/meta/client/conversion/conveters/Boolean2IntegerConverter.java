package com.concur.meta.client.conversion.conveters;

import com.concur.meta.client.conversion.ConverterAdapter;

/**
 * Boolean转Integer
 *
 * @author yongfu.cyf
 * @create 2017-07-24 下午4:09
 **/
public class Boolean2IntegerConverter extends ConverterAdapter<Boolean, Integer> {

    @Override
    public Integer convert(Boolean source, Object... objects) {
        if (source == null) {
            return Integer.valueOf(0);
        }

        if (source) {
            return Integer.valueOf(1);
        }
        return Integer.valueOf(0);
    }

    @Override
    protected void postRegister() {
        converterService.registerConverter(boolean.class, Integer.class, this);
        converterService.registerConverter(boolean.class, int.class, this);
    }
}
