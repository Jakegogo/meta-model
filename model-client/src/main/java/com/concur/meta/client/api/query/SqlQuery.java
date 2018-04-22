package com.concur.meta.client.api.query;

import java.io.Serializable;
import java.util.Map;

import com.concur.meta.client.api.result.SqlResult;
import com.concur.meta.client.dataobject.ResponseResult;
import com.concur.meta.client.service.ServiceFactory;
import com.concur.meta.client.constants.QueryType;

/**
 * SQL查询
 * 支持模型名到表名、属性名到属性名的替换
 * 1.${StoreRelation} : ot_store_relation表
 * 2.${StoreRelation.*} : ot_store_relation表的所有字段
 * 3.${StoreRelation.storeId} ： ot_store_relation.store_id字段
 * @author yongfu.cyf
 * @create 2017-08-23 上午10:48
 **/
public class SqlQuery<T extends Serializable> extends AbstractQuery<T> {

    SqlQuery() {
    }

    SqlQuery(Query<?> fromQuery, String sql) {
        this.clazz = (Class<T>) fromQuery.getClazz();
        this.dataSourceId = fromQuery.dataSourceId;
        this.queryParam = new QueryParam();
        this.queryParam.setSql(sql);
    }

    /**
     * 指定数据源ID
     * @param dataSourceId Long
     * @return
     */
    public SqlQuery<T> dataSource(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
        return this;
    }

    /**
     * 指定参数
     * @param params 参数
     * @return
     */
    public SqlQuery<T> params(Map<String, Serializable> params) {
        Map<String, Serializable> sqlParams = this.queryParam.getSqlParams();
        sqlParams.putAll(params);
        return this;
    }

    /**
     * 指定参数
     * @param key 参数的key(属性名)
     * @param value 参数值
     * @return
     */
    public SqlQuery<T> param(String key, Serializable value) {
        Map<String, Serializable> sqlParams = this.queryParam.getSqlParams();
        sqlParams.put(key, value);
        return this;
    }

    /**
     * 执行查询
     * @return
     */
    public SqlResult<T> execute() {
        return new SqlResult<T>(this.clazz, new ClientQueryAction() {
            @Override
            public ResponseResult<Map> doQuery() {
                return ServiceFactory.getInstance()
                    .getMetaDataReadService().query(SqlQuery.this);
            }
        });
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.SqlQuery;
    }

}
