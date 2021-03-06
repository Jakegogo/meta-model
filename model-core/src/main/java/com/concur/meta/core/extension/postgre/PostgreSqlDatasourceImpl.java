package com.concur.meta.core.extension.postgre;

import javax.sql.DataSource;

import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.dbengine.factory.impl.BaseMetaDatasource;
import com.concur.meta.core.dbengine.sql.sqlbuilder.BaseBuilder;
import com.concur.meta.core.dbengine.meta.MetaBuilder;
import com.concur.meta.core.dbengine.meta.metabuilder.ConfigurableMetaBuilder;
import com.concur.meta.core.dbengine.sql.sqlbuilder.SqlBuilder;

/**
 * PostgreSQL数据源
 *
 * @author yongfu.cyf
 * @create 2017-07-03 下午3:38
 **/
public class PostgreSqlDatasourceImpl extends BaseMetaDatasource {

    public PostgreSqlDatasourceImpl(DataSource dataSource, String dataSourceKey) {
        super(dataSource, dataSourceKey);
    }

    @Override
    public DataSourceType getDataSourceType() {
        return DataSourceType.POSTGRESQL;
    }


    @Override
    public SqlBuilder getSqlBuilder() {
        return new BaseBuilder(new PostgreSqlDialect());
    }

    @Override
    public MetaBuilder getMetaBuilder(MetaRequest metaRequest) {
        if (isAutoMetaBuilder(metaRequest)) {
            return new PostgresSqlAutoMetaBuilder();
        } else {
            return new ConfigurableMetaBuilder(this.tableMetaManager);
        }
    }
}
