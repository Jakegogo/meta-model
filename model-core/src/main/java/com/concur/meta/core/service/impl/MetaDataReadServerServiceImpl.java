package com.concur.meta.core.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.concur.meta.client.constants.ParamKeys;
import com.concur.meta.client.constants.QueryType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.dataobject.ResponseResult;
import com.concur.meta.client.service.server.MetaDataReadServerService;
import com.concur.meta.core.dbengine.execute.actions.PageQueryAction;
import com.concur.meta.core.dbengine.execute.ActionContext;
import com.concur.meta.core.dbengine.execute.QueryAction;
import com.concur.meta.core.dbengine.factory.MetaDataFactory;
import com.concur.meta.core.dbengine.execute.ActionTemplate;
import com.concur.meta.core.dbengine.execute.ActionTemplate.ActionCallback;
import com.concur.meta.core.dbengine.execute.SqlMapper;
import com.concur.meta.core.dbengine.execute.actions.AggQueryAction;
import com.concur.meta.core.dbengine.execute.actions.SqlQueryAction;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.concur.meta.core.dbengine.meta.TableMeta;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * DB数据读服务服务端实现
 *
 * @author yongfu.cyf
 * @create 2017-06-28 下午12:13
 **/
public class MetaDataReadServerServiceImpl implements MetaDataReadServerService, ApplicationContextAware {

    @Resource
    private MetaDataFactory metaDataFactory;
    
    @Resource
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询Action
     */
    private List<QueryAction> queryActions = new ArrayList<QueryAction>();

    @Override
    public MetaResponse get(MetaRequest request) {
        return ActionTemplate.execute(request, new ActionCallback() {
                @Override
                public Object call(MetaRequest request, MetaDatasource metaDatasource, SqlMapper sqlMapper, TableMeta tableMeta) {
                    String sql = metaDatasource.getSqlBuilder().buildGet(tableMeta, request);

                    Map<String, Serializable> paramMap = metaDatasource.getParameterBuilder().forModelFindById(tableMeta, request);
                    Map<String, Object> result = sqlMapper.selectOne(sql, paramMap);
                    // 字段名转换
                    result = metaDatasource.getColumnMapper().mapRow(result);
                    return new ResponseResult(null, -1, result);
                }
            }, metaDataFactory, sqlSessionFactory, null);
    }

    @Override
    public MetaResponse query(MetaRequest request) {
        ActionContext actionContext = new ActionContext();
        QueryType queryType = resolveQueryType(request);
        for (QueryAction queryAction : queryActions) {
            if (queryAction.support(queryType)) {
                actionContext.setQueryAction(queryAction);
                break;
            }
        }
        return actionContext.execute(request);
    }

    /**
     * 获取查询类型
     * @param request MetaRequest
     * @return
     */
    private QueryType resolveQueryType(MetaRequest request) {
        QueryType queryType = (QueryType) request.getParam(ParamKeys.QUERY_TYPE);
        if (queryType == null) {
            return QueryType.PageQuery;
        }
        return queryType;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        List<QueryAction> queryActions = new ArrayList<QueryAction>();
        queryActions.add(new AggQueryAction(metaDataFactory, sqlSessionFactory));
        queryActions.add(new PageQueryAction(metaDataFactory, sqlSessionFactory));
        queryActions.add(new SqlQueryAction(metaDataFactory, sqlSessionFactory));
        this.queryActions = queryActions;
    }

}
