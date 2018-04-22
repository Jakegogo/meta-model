package com.concur.meta.core.dbengine.factory;

import javax.sql.DataSource;

import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.dbengine.mapper.ColumnMapper;
import com.concur.meta.core.dbengine.meta.MetaBuilder;
import com.concur.meta.core.dbengine.param.ParameterBuilder;
import com.concur.meta.core.dbengine.sql.sqlbuilder.SqlBuilder;

/**
 * 元数据引擎 数据源接口
 *
 * @author yongfu.cyf
 * @create 2017-06-28 上午11:56
 **/
public interface MetaDatasource {

    /** 默认数据源类型 */
    DataSourceType DEFAULT_DATASOURCE_TYPE = DataSourceType.MYSQL;

    /**
     * 获取数据源类型
     * @return
     */
    DataSourceType getDataSourceType();

    /**
     * 获取DB数据源
     * @return DataSource
     */
    DataSource getDataSource();

    /**
     * 获取数据源Key
     * @return
     */
    String getDataSourceKey();

    /**
     * 获取表信息构建器
     * @param metaRequest MetaRequest
     * @return
     */
    MetaBuilder getMetaBuilderWithCache(MetaRequest metaRequest);

    /**
     * 获取SQL构建器
     * @return
     */
    SqlBuilder getSqlBuilder();

    /**
     * 获取参数构建器
     * @return
     */
    ParameterBuilder getParameterBuilder();

    /**
     * 获取字段映射器
     * @return
     */
    ColumnMapper getColumnMapper();

}
