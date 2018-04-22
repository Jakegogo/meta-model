package com.concur.meta.core.dbengine.factory.impl;

import javax.sql.DataSource;

import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.dbengine.meta.MetaBuilder;
import com.concur.meta.core.dbengine.sql.sqlbuilder.BaseBuilder;
import com.concur.meta.core.dbengine.meta.metabuilder.DefaultAutoMetaBuilder;
import com.concur.meta.core.dbengine.sql.sqlbuilder.SqlBuilder;
import com.concur.meta.core.extension.mysql.MysqlDialect;
import com.concur.meta.metadata.util.ServiceUtil;

/**
 * 默认数据源(未指定数据源ID情况下)
 *
 * @author yongfu.cyf
 * @create 2017-09-22 下午11:46
 **/
public class NullMetaDataSource extends BaseMetaDatasource {

    private static final String DATASOURCEKEY = "default";

    public NullMetaDataSource() {
        super(null, DATASOURCEKEY);
    }

    @Override
    public DataSourceType getDataSourceType() {
        return DataSourceType.MYSQL;
    }

    @Override
    public SqlBuilder getSqlBuilder() {
        return new BaseBuilder(new MysqlDialect());
    }

    @Override
    public MetaBuilder getMetaBuilder(MetaRequest metaRequest) {
        return new DefaultAutoMetaBuilder();
    }

    @Override
    public String getDataSourceKey() {
        return DATASOURCEKEY;
    }

    @Override
    public DataSource getDataSource() {
        return ServiceUtil.getLmodelConfigDataSource();
    }
}
