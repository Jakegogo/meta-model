package com.concur.meta.metadata.service.impl;

import javax.sql.DataSource;

import com.concur.meta.metadata.service.MetaDataConfigService;
import com.concur.meta.metadata.util.ServiceUtil;

/**
 * 元数据配置服务实现
 *
 * @author yongfu.cyf
 * @create 2017-07-03 下午4:19
 **/
public class MetaDataConfigServiceImpl implements MetaDataConfigService {

    @Override
    public DataSource getConfigDataSource() {
        return ServiceUtil.getLmodelConfigDataSource();
    }
}
