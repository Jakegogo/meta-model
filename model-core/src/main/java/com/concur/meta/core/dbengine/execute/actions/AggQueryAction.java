package com.concur.meta.core.dbengine.execute.actions;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.concur.meta.client.api.query.QueryParam;
import com.concur.meta.client.constants.ParamKeys;
import com.concur.meta.client.constants.QueryType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.dataobject.ResponseResult;
import com.concur.meta.core.dbengine.execute.QueryAction;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.concur.meta.core.dbengine.factory.MetaDataFactory;
import com.concur.meta.core.dbengine.meta.TableMeta;
import com.concur.meta.core.dbengine.execute.ActionTemplate;
import com.concur.meta.core.dbengine.execute.SqlMapper;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * 聚合查询Action
 *
 * @author yongfu.cyf
 * @create 2017-08-23 下午12:00
 **/
public class AggQueryAction extends ActionTemplate implements QueryAction {

    public AggQueryAction(MetaDataFactory metaDataFactory, SqlSessionFactory sqlSessionFactory) {
        this.metaDataFactory = metaDataFactory;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public boolean support(QueryType type) {
        return QueryType.AggQuery == type;
    }

    @Override
    protected Object executeInner(MetaRequest request, MetaDatasource metaDatasource,
                                        SqlMapper sqlMapper, TableMeta tableMeta) {
        // 构建Sql
        String querySql = metaDatasource.getSqlBuilder().buildQuery(tableMeta, request);
        Map<String, Serializable> paramMap = metaDatasource.getParameterBuilder().forModelQuery(tableMeta, request);
        // 执行查询
        List<Map<String, Object>> result = sqlMapper.selectList(querySql, paramMap);

        // 字段名转换
        result = metaDatasource.getColumnMapper().mapRows(result);
        // 返回结果
        QueryParam queryParam = (QueryParam) request.getParam(ParamKeys.QUERY_PARAM);
        return new ResponseResult(queryParam, -1, result);
    }

}
