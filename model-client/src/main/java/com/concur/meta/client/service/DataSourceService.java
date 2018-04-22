package com.concur.meta.client.service;

import com.concur.meta.client.domain.dto.DataSourceDTO;

/**
 * 数据源服务接口
 *
 * @author yongfu.cyf
 * @create 2017-07-24 下午8:16
 **/
public interface DataSourceService {

    /**
     * 获取数据源
     * @param dataSourceId
     * @return
     */
    DataSourceDTO getDataSource(long dataSourceId);

}
