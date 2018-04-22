package com.concur.meta.metadata.service.impl;

import com.concur.meta.client.api.query.Query;
import com.concur.meta.client.domain.dto.DataSourceDTO;
import com.concur.meta.client.service.DataSourceService;
import com.concur.meta.metadata.domain.MetaDataSourceDO;
import org.springframework.beans.BeanUtils;

/**
 * 数据源服务实现
 *
 * @author yongfu.cyf
 * @create 2017-07-24 下午8:17
 **/
public class DataSourceServiceImpl implements DataSourceService {

    @Override
    public DataSourceDTO getDataSource(long dataSourceId) {
        MetaDataSourceDO metaDataSourceDO =  Query.create(MetaDataSourceDO.class)
            .get(dataSourceId).execute().getOne();
        if (metaDataSourceDO == null) {
            return null;
        }
        DataSourceDTO dto = new DataSourceDTO();
        BeanUtils.copyProperties(metaDataSourceDO, dto);
        return dto;
    }

}
