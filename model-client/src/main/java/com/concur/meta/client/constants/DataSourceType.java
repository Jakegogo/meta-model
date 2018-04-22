package com.concur.meta.client.constants;

import java.io.Serializable;

/**
 * 数据源类型
 *
 * @author yongfu.cyf
 * @create 2017-06-28 下午5:53
 **/
public enum DataSourceType implements Serializable {

    MYSQL("mysql", "mysql数据库"),

    TAIR("tair", "tair缓存"),

    HBASE("hbase", "Hbase数据库"),

    GARUDA("garuda", "Garuda数据库"),

    POSTGRESQL("postgreSQL", "PostgreSQL数据库"),

    NULL("null", "未指定"),

    ;


    DataSourceType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    private final String name;
    private final String desc;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static DataSourceType getByName(String name) {
        for (DataSourceType value : DataSourceType.values()) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
