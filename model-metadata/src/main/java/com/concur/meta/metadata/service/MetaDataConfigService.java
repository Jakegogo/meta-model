package com.concur.meta.metadata.service;

import javax.sql.DataSource;

/**
 * 元数据配置服务
 *
 * @author yongfu.cyf
 * @create 2017-06-28 下午3:28
 **/
public interface MetaDataConfigService {

    /**
     * 获取配置数据源
     * @return
     */
    DataSource getConfigDataSource();

}
