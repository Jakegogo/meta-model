/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.concur.meta.client.utils;

import java.lang.reflect.*;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class TypeUtils {


    public static Class<?> getRawClass(Type type) {

        if (type == null) {
            return Object.class;
        }

        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType());
        } else {
            throw new IllegalArgumentException("TODO");
        }
    }

    public static boolean isGenericParamType(Type type) {
        if (type instanceof ParameterizedType) {
            return true;
        }
        
        if (type instanceof Class) {
            return isGenericParamType(((Class<?>) type).getGenericSuperclass());
        }
        
        return false;
    }
    
    public static Type getGenericParamType(Type type) {
        if (type instanceof ParameterizedType) {
            return type;
        }
        
        if (type instanceof Class) {
            return getGenericParamType(((Class<?>) type).getGenericSuperclass());
        }
        
        return type;
    }
    
    public static Type unwrap(Type type) {
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            if (componentType == byte.class) {
                return byte[].class;
            }
            if (componentType == char.class) {
                return char[].class;
            }
        }
        
        return type;
    }

    public static Class<?> getClass(Type type) {
        if (type.getClass() == Class.class) {
            return (Class<?>) type;
        }

        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }

        return Object.class;
    }
    
    public static Field getField(Class<?> clazz, String fieldName) {
        for (Field field : clazz.getDeclaredFields()) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        
        Class<?> superClass = clazz.getSuperclass();
        if(superClass != null && superClass != Object.class) {
            return getField(superClass, fieldName);
        }

        return null;
    }
    

    public static String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
                Character.isUpperCase(name.charAt(0))){
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }


    /**
     * 获取泛型参数
     * @param type ParameterizedType
     * @return
     */
    public static Type getParameterizedType(ParameterizedType type, int index) {
        return type.getActualTypeArguments()[index];
    }


    /**
     * 获取泛型参数类
     * @param type Type
     * @return
     */
    public static Class<?> getParameterizedClass(Type type, int index) {
        if (type instanceof ParameterizedType) {
            Type parameterizedType = ((ParameterizedType)type).getActualTypeArguments()[index];
            return getRawClass(parameterizedType);
        } else {
            return Object.class;
        }
    }


    public static Byte castToByte(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).byteValue();
        }

        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }

            if ("null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }

            return Byte.parseByte(strVal);
        }

        throw new IllegalArgumentException("can not cast to byte, value : " + value);
    }
    
}