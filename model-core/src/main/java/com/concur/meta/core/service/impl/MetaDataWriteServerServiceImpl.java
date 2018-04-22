package com.concur.meta.core.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.concur.meta.core.dbengine.execute.ExecuteContext;
import com.concur.meta.core.dbengine.execute.SqlMapper;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.google.common.base.Function;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.concur.meta.client.api.persist.actions.operation.AbstractOperation;
import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.constants.ParamKeys;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.exception.ConsistencyException;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.result.ServerResultCode;
import com.concur.meta.client.service.server.MetaDataWriteServerService;
import com.concur.meta.core.dbengine.execute.actions.BaseInsertAction;
import com.concur.meta.core.manager.DataSourceManager;
import com.concur.meta.core.dbengine.execute.ActionTemplate;
import com.concur.meta.core.dbengine.execute.ActionTemplate.ActionCallback;
import com.concur.meta.core.dbengine.execute.WriteAction;
import com.concur.meta.core.dbengine.factory.MetaDataFactory;
import com.concur.meta.core.dbengine.meta.TableMeta;
import com.concur.meta.core.extension.postgre.actions.PostgresSqlInsertAction;
import com.concur.meta.metadata.domain.MetaDataSourceDO;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 数据写服务服务端实现
 *
 * @author yongfu.cyf
 * @create 2017-06-28 下午4:04
 **/
public class MetaDataWriteServerServiceImpl implements MetaDataWriteServerService, ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(MetaDataWriteServerServiceImpl.class);

    @Resource
    private MetaDataFactory metaDataFactory;
    
    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private DataSourceManager dataSourceManager;

    /**
     * Insert Action
     */
    private List<WriteAction> insertActions = new ArrayList<WriteAction>();

    @Override
    public MetaResponse update(MetaRequest request) {
        return ActionTemplate.execute(request, new ActionCallback() {
            @Override
            public Object call(MetaRequest request, MetaDatasource metaDatasource, SqlMapper sqlMapper, TableMeta tableMeta) {
                String sql = metaDatasource.getSqlBuilder().buildUpdate(tableMeta, request);

                Map<String, Serializable> paramMap = metaDatasource.getParameterBuilder().forModelUpdate(tableMeta,
                    request);

                int update = sqlMapper.update(sql, paramMap);
                sqlMapper.commit();
                return update;
            }
        }, metaDataFactory, sqlSessionFactory, TransactionIsolationLevel.READ_COMMITTED);
    }

    @Override
    public MetaResponse updateSelective(MetaRequest request) {
        return ActionTemplate.execute(request, new ActionCallback() {
            @Override
            public Object call(MetaRequest request, MetaDatasource metaDatasource, SqlMapper sqlMapper, TableMeta tableMeta) {
                String sql = metaDatasource.getSqlBuilder().buildUpdateSelective(tableMeta, request);

                Map<String, Serializable> paramMap = metaDatasource.getParameterBuilder().forModelUpdate(tableMeta,
                    request);
                int update = sqlMapper.update(sql, paramMap);
                sqlMapper.commit();
                return update;
            }
        }, metaDataFactory, sqlSessionFactory, TransactionIsolationLevel.READ_COMMITTED);
    }

    @Override
    public MetaResponse insert(MetaRequest request) {
        DataSourceType dataSourceType = resolveDataSourceType(request);
        for (WriteAction insertAction : insertActions) {
            if (insertAction.support(dataSourceType)) {
                return insertAction.execute(request);
            }
        }
        throw new ExecuteException(ServerResultCode.DATA_SOURCE_TYPE_NOT_SUPPORT_FOR_THE_OPERATION, dataSourceType.getName(), "insert");
    }

    /**
     * 获取数据源类型
     * @param request MetaRequest
     * @return
     */
    private DataSourceType resolveDataSourceType(MetaRequest request) {
        if (request.getDataSourceId() == null) {
            return DataSourceType.MYSQL;
        }
        MetaDataSourceDO metaDataSourceDO = dataSourceManager.getMetaDataSource(request.getDataSourceId());
        DataSourceType dataSourceType = DataSourceType.getByName(metaDataSourceDO.getType());
        if (dataSourceType == null) {
            throw new ExecuteException(ServerResultCode.DATA_SOURCE_TYPE_NOT_EXISTS);
        }
        return dataSourceType;
    }

    @Override
    public MetaResponse delete(MetaRequest request) {
        return ActionTemplate.execute(request, new ActionCallback() {
            @Override
            public Object call(MetaRequest request, MetaDatasource metaDatasource, SqlMapper sqlMapper, TableMeta tableMeta) {
                String sql = metaDatasource.getSqlBuilder().buildDelete(tableMeta, request);

                Map<String, Serializable> paramMap = metaDatasource.getParameterBuilder().forModelDeleteById(
                    tableMeta, request);
                int delete = sqlMapper.delete(sql, paramMap);
                sqlMapper.commit();
                return delete;
            }
        }, metaDataFactory, sqlSessionFactory, TransactionIsolationLevel.READ_COMMITTED);
    }

    @Override
    public MetaResponse batchInsert(MetaRequest request) {
        return ActionTemplate.execute(request, new ActionCallback() {
            @Override
            public Object call(MetaRequest request, MetaDatasource metaDatasource, SqlMapper sqlMapper, TableMeta tableMeta) {
                String sql = metaDatasource.getSqlBuilder().buildBatchInsert(tableMeta, request);

                Map<String, Serializable> paramMap = metaDatasource.getParameterBuilder().forModelBatchInsert(
                    tableMeta, request);
                int count = sqlMapper.insert(sql, paramMap, null);
                sqlMapper.commit();

                return count;
            }
        }, metaDataFactory, sqlSessionFactory, TransactionIsolationLevel.READ_COMMITTED);
    }

    @Override
    public MetaResponse batchDelete(MetaRequest request) {
        return ActionTemplate.execute(request, new ActionCallback() {
            @Override
            public Object call(MetaRequest request, MetaDatasource metaDatasource, SqlMapper sqlMapper, TableMeta tableMeta) {
                String sql = metaDatasource.getSqlBuilder().buildBatchDelete(tableMeta, request);

                Map<String, Serializable> paramMap = metaDatasource.getParameterBuilder().forModelBatchDeleteById(
                    tableMeta, request);
                int delete = sqlMapper.delete(sql, paramMap);
                sqlMapper.commit();
                return delete;
            }
        }, metaDataFactory, sqlSessionFactory, TransactionIsolationLevel.READ_COMMITTED);
    }

    @Override
    public MetaResponse executeAll(MetaRequest request) {
        final MetaResponse metaResponse = new MetaResponse();
        metaResponse.setSuccess(true);

        // 服务端执行失败后是否自动回滚
        boolean autoRollback = MapUtils.getBoolean(request.getObject(), ParamKeys.AUTO_ROLLBACK, false);
        // 是否要使用幂等支持
        final boolean dataConsistency = MapUtils.getBoolean(request.getObject(), ParamKeys.DATA_CONSISTENCY, false);
        List<AbstractOperation> actions = (List<AbstractOperation>) request.getParam(ParamKeys.CONBINE_ACTIONS);

        // 根据数据源分组
        Multimap<Long, AbstractOperation> groups =
            Multimaps.index(actions, new Function<AbstractOperation, Long>() {
                private final Long PLACEHOLDER = 0L;
                @Override
                public Long apply(AbstractOperation action) {
                    Long dataSourceId =  action.getDataSourceId();
                    if (dataSourceId == null) {
                        return PLACEHOLDER;
                    }
                    return dataSourceId;
                }
            });

        // 设置日志展示
        ExecuteContext.setShowLog(request.isShowLog());

        for (final Collection<AbstractOperation> subActions : groups.asMap().values()) {

            boolean success;
            RuntimeException executeException = null;
            try {
                // 开启事务
                metaDataFactory.preSetDataSource(subActions.iterator().next().getDataSourceId());
                success = transactionTemplate.execute(new TransactionCallback<Boolean>() {
                    @Override
                    public Boolean doInTransaction(TransactionStatus transactionStatus) {

                        for (AbstractOperation action : subActions) {
                            try {
                                action.operateWithStatus();
                            } catch (RuntimeException e) {
                                logger.error("executeAll execute error, action:" + action.toDetailMessage(), e);
                                throw e;
                            }
                            if (dataConsistency && action.needCheckConsistency() && !action.isSuccess()) {
                                throw new ConsistencyException(ServerResultCode.SERVER_DATA_CONSISTENCY_EXCEPTION);
                            }
                        }
                        return true;
                    }
                });
                // 关闭事务
            } catch (RuntimeException e) {
                success = false;
                executeException = e;
                logger.error("executeAll execute error," +
                    JSON.toJSONString(request, SerializerFeature.IgnoreNonFieldGetter), e);
            }

            if (!success) {
                // 自动逆向补偿 - 回滚
                if (autoRollback) {
                    try {
                        // 一般用于insert回滚
                        for (AbstractOperation action : subActions) {
                            action.undoWithStatus();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // 结束提交
                metaResponse.setSuccess(false);
                if (executeException != null) {
                    metaResponse.setErrorResult(ServerResultCode.CONBINE_INVOKE_EXCEPTION, executeException.getMessage(), executeException);
                } else {
                    metaResponse.setErrorResult(ServerResultCode.CONBINE_INVOKE_EXCEPTION, "success=false");
                }
                break;
            }
        }

        metaResponse.setResult(actions);
        return metaResponse;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        List<WriteAction> insertActions = new ArrayList<WriteAction>();
        insertActions.add(new PostgresSqlInsertAction(metaDataFactory, sqlSessionFactory));
        // 默认使用的Insert指令执行器
        insertActions.add(new BaseInsertAction(metaDataFactory, sqlSessionFactory));
        this.insertActions = insertActions;
    }
}
