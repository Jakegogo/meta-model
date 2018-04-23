package com.concur.meta.client.conversion.conveters;

import com.concur.meta.client.conversion.ConverterAdapter;

/**
 * Integer转Boolean
 *
 * @author yongfu.cyf
 * @create 2017-07-24 下午4:09
 **/
public class Integer2BooleanConverter extends ConverterAdapter<Integer, Boolean> {

    @Override
    public Boolean convert(Integer source, Object... objects) {
        if (source == null) {
            return Boolean.valueOf(false);
        }

        if (source > 0) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    @Override
    protected void postRegister() {
        converterService.registerConverter(Integer.class, boolean.class, this);
        converterService.registerConverter(int.class, boolean.class, this);
    }
}
