package com.concur.meta.client.dataobject;

import java.io.Serializable;
import java.util.Map;

import com.concur.meta.client.config.LmodelConfig;
import com.concur.meta.client.constants.DataSourceType;

/**
 * 元数据存取服务请求
 *
 * @author yongfu.cyf
 * @create 2017-06-28 下午4:26
 **/
public class MetaRequest implements Serializable {

    private static final long serialVersionUID = -8621382107470565300L;
    /**
     * 请求元数据类型
     */
    private String className;
    /**
     * 数据源类型
     */
    private DataSourceType dataSourceType = DataSourceType.MYSQL;
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    /**
     * 参数对象
     */
    private Map<String, Serializable> object;
    /**
     * 是否为批量
     */
    private boolean isBatch = false;
    /**
     * 是否需要显示日志
     */
    private boolean showLog = LmodelConfig.isShowLog();

    public Map<String, Serializable> getObject() {
        return object;
    }

    /**
     * 获取参数
     * @param key 参数Key
     * @return
     */
    public Serializable getParam(String key) {
        if (this.object == null) {
            return null;
        }
        return this.object.get(key);
    }

    public void setObject(Map<String, Serializable> object) {
        this.object = object;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 设置数据源类型
     * @param dataSourceType DataSourceType
     */
    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    /**
     * 获取数据源类型
     * @return
     */
    public DataSourceType getDataSourceType() {
        return dataSourceType;
    }

    /**
     * 获取数据源ID
     * @return
     */
    public Long getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public boolean isBatch() {
        return isBatch;
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }

    /**
     * 添加属性
     * @param key KEY
     * @param value VALUE
     */
    public void addAttach(String key, Serializable value) {
        this.object.put(key, value);
    }

    public boolean isShowLog() {
        return showLog;
    }
}
