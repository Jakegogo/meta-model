package com.concur.meta.metadata.domain;

import java.util.HashMap;
import java.util.Map;

import com.concur.meta.client.annotation.LModel;

/**
 * 模型的列DO
 *
 * @author yongfu.cyf
 * @create 2017-06-28 上午11:12
 **/
@LModel(table = "lmodel_col")
public class MetaModelColDO extends MetaBaseDO {

    private long id;

    /**
     * MetaModelDO.id
     */
    private long modelId;

    /**
     * 字段存储的列名
     */
    private String colCode;

    /**
     * 属性名
     */
    private String colName;

    /**
     * 状态 , -1删除
     */
    private int status;

    /**
     * 默认值(TODO)
     */
    private String defaultValue;

    /**
     * 是否可空
     */
    private boolean blankAble;

    /**
     * 对应java的数据类型(TODO)
     */
    private String javaType;

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

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    public String getColCode() {
        return colCode;
    }

    public void setColCode(String colCode) {
        this.colCode = colCode;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isBlankAble() {
        return blankAble;
    }

    public void setBlankAble(boolean blankAble) {
        this.blankAble = blankAble;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
