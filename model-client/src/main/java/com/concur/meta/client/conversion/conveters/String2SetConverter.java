package com.concur.meta.client.conversion.conveters;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import com.concur.meta.client.conversion.ConverterAdapter;
import com.concur.meta.client.utils.ExtraUtil;
import com.concur.meta.client.utils.TypeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * ${DESCRIPTION}
 *
 * @author yongfu.cyf
 * @create 2017-07-24 下午4:06
 **/
public class String2SetConverter extends ConverterAdapter<String, Set> {

    private static final String STRING_SPLITER = ",";

    @Override
    public Set convert(String source, Type targetType, Object... objects) {
        if (StringUtils.isBlank(source)) {
            return new HashSet();
        }
        String[] array = source.split(STRING_SPLITER);
        Set result = new HashSet();
        for (String elem : array) {
            if (StringUtils.isNotBlank(elem)) {
                elem = ExtraUtil.decode(elem);
                if (targetType instanceof ParameterizedType) {
                    Object value = converterService.convert(elem,
                        TypeUtils.getParameterizedType((ParameterizedType) targetType, 0));
                    result.add(value);
                } else {
                    result.add(elem);
                }
            }
        }
        return result;
    }
}
