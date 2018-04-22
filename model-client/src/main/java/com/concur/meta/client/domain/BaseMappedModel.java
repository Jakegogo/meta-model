package com.concur.meta.client.domain;

import java.util.HashMap;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Map类型映射的DO基类
 *
 * @author yongfu.cyf
 * @create 2017-06-28 下午3:44
 **/
public abstract class BaseMappedModel<T> extends HashMap implements BaseModel {

    @Override
    public T put(Object key, Object value) {
        super.put(key, value);
        return (T) this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
