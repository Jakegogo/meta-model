package com.concur.meta.core.manager;

import javax.sql.DataSource;

import com.concur.meta.metadata.domain.MetaDataSourceDO;

/**
 * 数据源管理接口
 *
 * @author yongfu.cyf
 * @create 2017-07-05 上午11:58
 **/
public interface DataSourceManager {

    /**
     * 获取(初始化)数据源
     * @param dataSourceId 数据源ID
     * @return
     */
    DataSource getDatasource(Long dataSourceId);

    /**
     * 获取数据源DO (带缓存)
     * @param dataSourceId 数据源ID
     * @return
     */
    MetaDataSourceDO getMetaDataSource(Long dataSourceId);
}
