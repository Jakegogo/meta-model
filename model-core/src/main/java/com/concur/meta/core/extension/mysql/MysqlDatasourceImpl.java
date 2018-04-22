package com.concur.meta.core.extension.mysql;

import javax.sql.DataSource;

import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.dbengine.factory.impl.BaseMetaDatasource;
import com.concur.meta.core.dbengine.meta.metabuilder.ConfigurableMetaBuilder;
import com.concur.meta.core.dbengine.meta.metabuilder.DefaultAutoMetaBuilder;
import com.concur.meta.core.dbengine.sql.sqlbuilder.BaseBuilder;
import com.concur.meta.core.dbengine.sql.sqlbuilder.SqlBuilder;
import com.concur.meta.core.dbengine.meta.MetaBuilder;

/**
 * Mysql数据源
 *
 * @author yongfu.cyf
 * @create 2017-07-03 下午3:38
 **/
public class MysqlDatasourceImpl extends BaseMetaDatasource {

    public MysqlDatasourceImpl(DataSource dataSource, String dataSourceKey) {
        super(dataSource, dataSourceKey);
    }

    @Override
    public MetaBuilder getMetaBuilder(MetaRequest metaRequest) {
        if (isAutoMetaBuilder(metaRequest)) {
            return new DefaultAutoMetaBuilder();
        } else {
            return new ConfigurableMetaBuilder(this.tableMetaManager);
        }
    }

    @Override
    public DataSourceType getDataSourceType() {
        return DataSourceType.MYSQL;
    }

    @Override
    public SqlBuilder getSqlBuilder() {
        return new BaseBuilder(new MysqlDialect());
    }

}
