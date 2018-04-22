package com.concur.meta.client.api.query;

import java.io.Serializable;

/**
 * 聚合类型
 *
 * @author yongfu.cyf
 * @create 2017-06-29 下午5:09
 **/
public enum AggregateType implements Serializable {

    /**
     * 统计数量
     */
    COUNT("count", "统计个数"),

    /**
     * 求和
     */
    SUM("sum", "求和"),

    ;

    AggregateType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    private String name;
    private String desc;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

}
