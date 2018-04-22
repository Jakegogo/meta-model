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
import com.concur.meta.core.dbengine.execute.ActionTemplate;
import com.concur.meta.core.dbengine.execute.SqlMapper;
import com.concur.meta.core.dbengine.meta.TableMeta;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * Sql查询Action
 *
 * @author yongfu.cyf
 * @create 2017-08-23 下午12:00
 **/
public class SqlQueryAction extends ActionTemplate implements QueryAction {

    public SqlQueryAction(MetaDataFactory metaDataFactory, SqlSessionFactory sqlSessionFactory) {
        this.metaDataFactory = metaDataFactory;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public boolean support(QueryType type) {
        return QueryType.SqlQuery == type;
    }

    @Override
    protected Object executeInner(MetaRequest request, MetaDatasource metaDatasource, SqlMapper sqlMapper, TableMeta tableMeta) {
        QueryParam queryParam = (QueryParam) request.getParam(ParamKeys.QUERY_PARAM);
        // 获取表元数据信息
        String querySql = queryParam.getSql();
        Map<String, Serializable> paramMap = queryParam.getSqlParams();

        List<Map<String, Object>> result = sqlMapper.selectList(querySql, paramMap);
        // 字段名转换
        result = metaDatasource.getColumnMapper().mapRows(result);
        // 返回结果
        return new ResponseResult(queryParam, -1, result);
    }

    @Override
    protected TableMeta getTableMeta(MetaRequest request, MetaDatasource metaDatasource) {
        return null;
    }
}
