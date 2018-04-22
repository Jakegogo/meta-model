package com.concur.meta.core.dbengine.execute;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.exception.LModelException;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.concur.meta.core.dbengine.factory.MetaDataFactory;
import com.concur.meta.core.dbengine.meta.TableMeta;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action执行模板
 *
 * @author yongfu.cyf
 * @create 2017-09-23 下午4:26
 **/
public abstract class ActionTemplate {

    private static Logger logger = LoggerFactory.getLogger(ActionTemplate.class);

    protected MetaDataFactory metaDataFactory;
    protected SqlSessionFactory sqlSessionFactory;

    /**
     * * 执行内容
     * <li>内部实现不可再调用DB查询</li>
     * @param request 请求对象
     * @param metaDatasource 元数据类型
     * @param sqlMapper SqlMapper
     * @param tableMeta 表元数据信息
     * @return
     */
    protected abstract Object executeInner(MetaRequest request,
                                           MetaDatasource metaDatasource,
                                           SqlMapper sqlMapper,
                                           TableMeta tableMeta);

    /**
     * 获取隔离级别
     * @return
     */
    protected TransactionIsolationLevel getTransactionIsolationLevel() {
        return null;
    }



    /**
     * 执行execute模板
     * @param request
     * @return
     */
    public MetaResponse execute(MetaRequest request) {
        MetaResponse metaResponse = new MetaResponse();

        SqlSession sqlSession = null;
        try {

            // 获取数据源
            MetaDatasource metaDatasource = metaDataFactory.getDataSource(request);

            // 获取表元数据信息(内部有查询数据库的地柜)
            TableMeta tableMeta = getTableMeta(request, metaDatasource);

            // mybatis切换数据源
            ExecuteContext.setShowLog(request.isShowLog());
            ExecuteContext.setDataSource(metaDatasource.getDataSourceKey());

            TransactionIsolationLevel isolationLevel = getTransactionIsolationLevel();
            if (isolationLevel != null) {
                sqlSession = sqlSessionFactory.openSession(isolationLevel);
            } else {
                sqlSession = sqlSessionFactory.openSession();
            }

            SqlMapper sqlMapper = new SqlMapper(sqlSession);
            Object result = executeInner(request, metaDatasource, sqlMapper, tableMeta);
            metaResponse.setResult(result);
            metaResponse.setSuccess(true);
        } catch (RuntimeException e) {
            logger.error("execute error , request:" +
                JSON.toJSONString(request, SerializerFeature.IgnoreNonFieldGetter), e);
            if (e instanceof LModelException) {
                throw ((LModelException)e).setExecuteLog(ExecuteContext.getLog());
            } else {
                throw new ExecuteException(e).setExecuteLog(ExecuteContext.getLog());
            }
        } finally {
            metaResponse.setExecuteLog(ExecuteContext.getLog());
            ExecuteContext.clearDataSource();
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
        return metaResponse;
    }

    /**
     * 获取表元信息
     * @param request MetaRequest
     * @param metaDatasource MetaDatasource
     * @return
     */
    protected TableMeta getTableMeta(MetaRequest request, MetaDatasource metaDatasource) {
        return metaDatasource.getMetaBuilderWithCache(request).build(metaDatasource, request);
    }

    /**
     * 执行execute模板
     * @param request
     * @return
     */
    public static MetaResponse execute(MetaRequest request, final ActionCallback callback,
                                       MetaDataFactory metaDataFactory, SqlSessionFactory sqlSessionFactory,
                                       final TransactionIsolationLevel isolationLevel) {
        return new ActionTemplate() {
            @Override
            protected Object executeInner(MetaRequest request, MetaDatasource metaDatasource, SqlMapper sqlMapper, TableMeta tableMeta) {
                return callback.call(request, metaDatasource, sqlMapper, tableMeta);
            }

            @Override
            protected TransactionIsolationLevel getTransactionIsolationLevel() {
                return isolationLevel;
            }
        }.setMetaDataFactory(metaDataFactory)
            .setSqlSessionFactory(sqlSessionFactory)
            .execute(request);
    }

    /**
     * 执行回调
     */
    public interface ActionCallback {
        /**
         * 模板执行回调方法
         * @param request MetaRequest
         * @param metaDatasource MetaDatasource
         * @param sqlMapper SqlMapper
         * @param tableMeta TableMeta
         * @return
         */
        Object call(MetaRequest request, MetaDatasource metaDatasource, SqlMapper sqlMapper, TableMeta tableMeta);
    }

    public ActionTemplate setMetaDataFactory(MetaDataFactory metaDataFactory) {
        this.metaDataFactory = metaDataFactory;
        return this;
    }

    public ActionTemplate setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        return this;
    }
}
