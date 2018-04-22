package com.concur.meta.client.domain.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.concur.meta.client.domain.ToString;

/**
 * 数据源DTO
 *
 * @author yongfu.cyf
 * @create 2017-07-24 下午8:19
 **/
public class DataSourceDTO extends ToString implements Serializable {
    private static final long serialVersionUID = -4952513636068514253L;

    /**
     * 空数据源
     */
    public static final DataSourceDTO NULL_DATASOURCE = new DataSourceDTO();

    /**
     * 数据源ID
     */
    private long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 最后修改时间
     */
    private Date gmtModified;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源类型
     */
    private String type;

    /**
     * 扩展属性
     */
    private Map<String, String> attributes= new HashMap<String, String>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
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

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
