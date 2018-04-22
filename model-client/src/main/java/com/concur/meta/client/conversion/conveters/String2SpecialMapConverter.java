package com.concur.meta.client.conversion.conveters;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.concur.meta.client.conversion.ConverterAdapter;
import com.concur.meta.client.result.ClientResultCode;
import com.concur.meta.client.utils.ExtraUtil;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.utils.TypeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 字符串转Map
 *
 * @author yongfu.cyf
 * @create 2017-07-24 下午12:00
 **/
public class String2SpecialMapConverter extends ConverterAdapter<String, AbstractMap> {

    private static final String STRING_SPLITER = ";";

    private static final String KEY_VALUE_SPLITER = ":";

    @Override
    public AbstractMap convert(String source, Type targetType, Object... objects) {
        if (StringUtils.isBlank(source)) {
            return new HashMap();
        }
        String[] array = source.split(STRING_SPLITER);
        AbstractMap<Serializable, Serializable> result = null;
        try {
            result = (AbstractMap<Serializable, Serializable>) targetType.getClass().newInstance();
        } catch (Exception e) {
            throw new ExecuteException(ClientResultCode.DATA_CONVERT_EXCEPTION, e);
        }
        for (String elem : array) {
            if (StringUtils.isNotBlank(elem)) {
                String[] entry = elem.split(KEY_VALUE_SPLITER);
                if (entry.length != 2) {
                    continue;
                }
                String key = ExtraUtil.decode(entry[0]);
                String value = ExtraUtil.decode(entry[1]);
                if (targetType instanceof ParameterizedType) {
                    key = converterService.convert(key,
                        TypeUtils.getParameterizedType((ParameterizedType)targetType, 0));
                    value = converterService.convert(value,
                        TypeUtils.getParameterizedType((ParameterizedType)targetType, 1));
                    result.put(key, value);
                } else {
                    result.put(key, value);
                }
            }
        }
        return result;
    }

    @Override
    protected void postRegister() {
        converterService.registerConverter(String.class, HashMap.class, this);
        converterService.registerConverter(String.class, LinkedHashMap.class, this);
        converterService.registerConverter(String.class, ConcurrentHashMap.class, this);
        converterService.registerConverter(String.class, TreeMap.class, this);
        converterService.registerConverter(String.class, ConcurrentSkipListMap.class, this);
    }
}
