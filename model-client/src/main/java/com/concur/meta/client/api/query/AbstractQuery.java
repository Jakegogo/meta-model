package com.concur.meta.client.api.query;

import java.io.Serializable;

import com.concur.meta.client.constants.QueryType;

/**
 * 查询基础类
 *
 * @author yongfu.cyf
 * @create 2017-08-01 上午10:07
 **/
public abstract class AbstractQuery<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -3087580292926609230L;
    /**
     * 查询的类
     */
    protected Class<T> clazz;
    /**
     * 查询参数
     */
    protected QueryParam<T> queryParam;
    /**
     * 数据源ID
     */
    protected Long dataSourceId;
    /**
     * 模型编码
     */
    public String code;

    public Long getDataSourceId() {
        return dataSourceId;
    }

    /**
     * 获取参数构造链
     * @return
     */
    public QueryParam<T> params() {
        return this.getQueryParam();
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public QueryParam<T> getQueryParam() {
        return queryParam;
    }

    /**
     * 获取查询类型
     * @return
     */
    public abstract QueryType getQueryType();

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof AbstractQuery)) { return false; }

        AbstractQuery<?> baseQuery = (AbstractQuery<?>)o;

        if (clazz != null ? !clazz.equals(baseQuery.clazz) : baseQuery.clazz != null) { return false; }
        if (queryParam != null ? !queryParam.equals(baseQuery.queryParam) : baseQuery.queryParam != null) {
            return false;
        }
        return dataSourceId != null ? dataSourceId.equals(baseQuery.dataSourceId) : baseQuery.dataSourceId == null;
    }

    @Override
    public int hashCode() {
        int result = clazz != null ? clazz.hashCode() : 0;
        result = 31 * result + (queryParam != null ? queryParam.hashCode() : 0);
        result = 31 * result + (dataSourceId != null ? dataSourceId.hashCode() : 0);
        return result;
    }

}
