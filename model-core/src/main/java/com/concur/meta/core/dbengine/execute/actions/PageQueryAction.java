package com.concur.meta.core.dbengine.execute.actions;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.concur.meta.client.api.query.QueryParam;
import com.concur.meta.client.constants.ParamKeys;
import com.concur.meta.client.constants.QueryType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.dataobject.ResponseResult;
import com.concur.meta.core.dbengine.execute.ActionTemplate;
import com.concur.meta.core.dbengine.execute.QueryAction;
import com.concur.meta.core.dbengine.execute.SqlMapper;
import com.concur.meta.core.dbengine.factory.MetaDataFactory;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.concur.meta.core.dbengine.meta.TableMeta;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * 分页查询Action
 *
 * @author yongfu.cyf
 * @create 2017-08-23 上午11:51
 **/
public class PageQueryAction extends ActionTemplate implements QueryAction {

    public PageQueryAction(MetaDataFactory metaDataFactory, SqlSessionFactory sqlSessionFactory) {
        this.metaDataFactory = metaDataFactory;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public boolean support(QueryType type) {
        return QueryType.PageQuery == type;
    }

    @Override
    protected Object executeInner(MetaRequest request, MetaDatasource metaDatasource,
                                          SqlMapper sqlMapper, TableMeta tableMeta) {
        // 需要分页
        QueryParam queryParam = (QueryParam) request.getParam(ParamKeys.QUERY_PARAM);

        String querySql = metaDatasource.getSqlBuilder().buildQuery(tableMeta, request);
        Map<String, Serializable> paramMap = metaDatasource.getParameterBuilder().forModelQuery(tableMeta, request);
        if (queryParam.isNeedPagenation()) {
            // 统计总数
            String countSql = metaDatasource.getSqlBuilder().buildCount(tableMeta, request);
            int total = MapUtils.getIntValue(sqlMapper.selectOne(countSql, paramMap), "total", 0);

            List<Map<String, Object>> result = Collections.emptyList();
            if (total > 0) {
                result = sqlMapper.selectList(querySql, paramMap);
                // 字段名转换
                result = metaDatasource.getColumnMapper().mapRows(result);
            }
            // 返回结果
            return new ResponseResult(queryParam, total, result);

        } else {
            List<Map<String, Object>> result = sqlMapper.selectList(querySql, paramMap);
            // 字段名转换
            result = metaDatasource.getColumnMapper().mapRows(result);
            // 返回结果
            return new ResponseResult(queryParam, -1, result);
        }
    }
}
