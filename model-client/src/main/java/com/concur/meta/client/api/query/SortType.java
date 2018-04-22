package com.concur.meta.client.api.query;

import java.io.Serializable;

/**
 * 排序类型参数
 *
 * @author yongfu.cyf
 * @create 2017-06-29 下午4:26
 **/
public enum SortType implements Serializable {

    ASC("asc", "正序"),

    DESC("desc", "倒序");

    SortType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private String type;
    private String desc;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
