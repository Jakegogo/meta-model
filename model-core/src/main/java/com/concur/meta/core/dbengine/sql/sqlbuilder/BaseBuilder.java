package com.concur.meta.core.dbengine.sql.sqlbuilder;

import com.concur.meta.client.api.query.QueryParam;
import com.concur.meta.client.constants.ParamKeys;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.dbengine.execute.ExecuteContext;
import com.concur.meta.core.dbengine.meta.TableMeta;
import com.concur.meta.core.dbengine.sql.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mysql的SQL语句构建
 *
 * @author yongfu.cyf
 * @create 2017-06-29 下午8:49
 **/
public class  BaseBuilder implements SqlBuilder {

    private static Logger logger = LoggerFactory.getLogger(BaseBuilder.class);

    private Dialect dialect;

    private BaseBuilder(){}

    public BaseBuilder(Dialect dialect) {
        this.dialect = dialect;
    }

    private void appendLog(String sql) {
        if (logger.isInfoEnabled()) {
            logger.info("build sql:" + sql);
            ExecuteContext.appendLog("BUILD SQL:" + sql);
        }
    }

    @Override
    public String buildGet(TableMeta tableMeta, MetaRequest metaRequest) {
        String sql = dialect.forModelFindById(tableMeta);
        appendLog(sql);
        return sql;
    }

    @Override
    public String buildInsert(TableMeta tableMeta, MetaRequest metaRequest) {
        String sql = dialect.forModelInsert(tableMeta);
        appendLog(sql);
        return sql;
    }

    @Override
    public String buildUpdate(TableMeta tableMeta, MetaRequest metaRequest) {
        String sql = dialect.forModelUpdate(tableMeta, metaRequest);
        appendLog(sql);
        return sql;
    }

    @Override
    public String buildUpdateSelective(TableMeta tableMeta, MetaRequest metaRequest) {
        String sql = dialect.forModelUpdateSelective(tableMeta, metaRequest);
        appendLog(sql);
        return sql;
    }

    @Override
    public String buildDelete(TableMeta tableMeta, MetaRequest metaRequest) {
        String sql = dialect.forModelDeleteById(tableMeta);
        appendLog(sql);
        return sql;
    }

    @Override
    public String buildQuery(TableMeta tableMeta, MetaRequest metaRequest) {
        QueryParam queryParam = (QueryParam) metaRequest.getParam(ParamKeys.QUERY_PARAM);
        String sql = dialect.forModelQuery(tableMeta, queryParam);
        appendLog(sql);
        return sql;
    }

    @Override
    public String buildCount(TableMeta tableMeta, MetaRequest metaRequest) {
        QueryParam queryParam = (QueryParam) metaRequest.getParam(ParamKeys.QUERY_PARAM);
        String sql = dialect.forModelCount(tableMeta, queryParam);
        appendLog(sql);
        return sql;
    }

    @Override
    public String buildBatchInsert(TableMeta tableMeta, MetaRequest metaRequest) {
        String sql = dialect.forModelBatchInsert(tableMeta);
        appendLog(sql);
        return sql;
    }

    @Override
    public String buildBatchDelete(TableMeta tableMeta, MetaRequest metaRequest) {
        String sql = dialect.forModelBatchDelete(tableMeta);
        appendLog(sql);
        return sql;
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }

}
