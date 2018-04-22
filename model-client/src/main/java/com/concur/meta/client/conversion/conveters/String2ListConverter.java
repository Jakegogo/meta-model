package com.concur.meta.client.conversion.conveters;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.concur.meta.client.conversion.ConverterAdapter;
import com.concur.meta.client.utils.ExtraUtil;
import com.concur.meta.client.utils.TypeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 字符串转List
 *
 * @author yongfu.cyf
 * @create 2017-07-24 下午12:00
 **/
public class String2ListConverter extends ConverterAdapter<String, List> {

    private static final String STRING_SPLITER = ",";

    @Override
    public List convert(String source, Type targetType, Object... objects) {
        if (StringUtils.isBlank(source)) {
            return new ArrayList();
        }
        String[] array = source.split(STRING_SPLITER);
        List result = new ArrayList();
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
