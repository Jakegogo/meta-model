package com.concur.meta.client.utils;

import java.lang.reflect.Field;

/**
 * 属性过滤
 */
public interface FieldFilter {
    boolean doFilter(Field field, Object value);
}