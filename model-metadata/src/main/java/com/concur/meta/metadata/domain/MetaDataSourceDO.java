package com.concur.meta.metadata.domain;

import java.util.HashMap;
import java.util.Map;

import com.concur.meta.client.annotation.LModel;
import com.concur.meta.client.constants.DataSourceType;

/**
 *  数据源DO
 *
 * @author yongfu.cyf
 * @create 2017-06-28 下午5:44
 **/
@LModel(table = "lmodel_datasource")
public class MetaDataSourceDO extends MetaBaseDO {

    /**
     * 数据源ID(Client指定的值)
     */
    private long id;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源类型
     * @see DataSourceType
     */
    private String type;

    /**
     * 额外属性
     */
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
