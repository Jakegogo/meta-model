package com.concur.meta.metadata.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.concur.meta.client.annotation.LModel;

/**
 * 模型DO
 *
 * @author yongfu.cyf
 * @create 2017-06-28 上午11:11
 **/
@LModel(table = "lmodel")
public class MetaModelDO extends MetaBaseDO {

    private long id;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 存储的表名
     */
    private String modelCode;

    /**
     * 模型类型
     */
    private String modelType;

    /**
     * 状态 , -1删除
     */
    private int status;

    /**
     * 对应的Java类名称
     */
    private String className;

    /**
     * 支持的数据源ID(TODO)
     */
    private Set<Long> datasourceIds;

    /**
     * 额外属性
     */
    private Map<String, String> attributes= new HashMap<String, String>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Set<Long> getDatasourceIds() {
        return datasourceIds;
    }

    public void setDatasourceIds(Set<Long> datasourceIds) {
        this.datasourceIds = datasourceIds;
    }

}
