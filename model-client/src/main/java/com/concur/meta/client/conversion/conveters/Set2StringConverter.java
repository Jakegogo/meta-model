package com.concur.meta.client.conversion.conveters;

import java.util.Set;

import com.concur.meta.client.conversion.ConverterAdapter;
import com.concur.meta.client.utils.ExtraUtil;

/**
 * Set转字符串
 *
 * @author yongfu.cyf
 * @create 2017-07-24 下午4:09
 **/
public class Set2StringConverter extends ConverterAdapter<Set, String> {

    private static final String STRING_SPLITER = ",";

    @Override
    public String convert(Set source, Object... objects) {
        if (source == null || source.size() == 0) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        for (Object entry : source) {
            String value = converterService.convert(entry, String.class);
            result.append(ExtraUtil.encode(value)).append(STRING_SPLITER);
        }
        return result.toString();
    }
}
