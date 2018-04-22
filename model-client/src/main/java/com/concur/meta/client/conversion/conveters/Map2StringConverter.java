package com.concur.meta.client.conversion.conveters;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.concur.meta.client.utils.ExtraUtil;
import com.concur.meta.client.conversion.ConverterAdapter;

/**
 * Map转字符串
 *
 * @author yongfu.cyf
 * @create 2017-07-24 下午12:00
 **/
public class Map2StringConverter extends ConverterAdapter<Map, String> {

    private static final String STRING_SPLITER = ";";

    private static final String KEY_VALUE_SPLITER = ":";

    @Override
    public String convert(Map source, Object... objects) {
        if (source == null || source.size() == 0) {
            return null;
        }

        Map<String, Object> map = (Map<String, Object>) source;
        StringBuilder result = new StringBuilder();
        for (Entry<String, Object> entry : map.entrySet()) {
            String value = converterService.convert(entry.getValue(), String.class);
            result.append(ExtraUtil.encode(entry.getKey())).append(KEY_VALUE_SPLITER)
                .append(ExtraUtil.encode(value)).append(STRING_SPLITER);
        }
        return result.toString();
    }

    @Override
    protected void postRegister() {
        converterService.registerConverter(HashMap.class, String.class, this);
        converterService.registerConverter(LinkedHashMap.class, String.class, this);
        converterService.registerConverter(ConcurrentMap.class, String.class, this);
        converterService.registerConverter(ConcurrentHashMap.class, String.class, this);
    }

}
