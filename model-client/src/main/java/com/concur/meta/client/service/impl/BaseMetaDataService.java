package com.concur.meta.client.service.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.domain.dto.DataSourceDTO;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.result.ClientResultCode;
import com.concur.meta.client.service.ServiceFactory;
import com.concur.meta.client.utils.CacheUtils;

/**
 * 元数据服务读写服务基类
 *
 * @author yongfu.cyf
 * @create 2017-07-25 上午10:42
 **/
public abstract class BaseMetaDataService<T extends BaseMetaDataService> {

    /**
     * 数据源缓存
     */
    protected LoadingCache<Long, DataSourceDTO> DATASOURCE_CACHE = CacheBuilder.newBuilder()
        .concurrencyLevel(4)
        .maximumSize(1000)
        .refreshAfterWrite(5, TimeUnit.MINUTES)
        .build(
            new CacheUtils.AsynchronousCacheLoader<Long, DataSourceDTO>() {
                @Override
                public DataSourceDTO load(Long key) {
                    DataSourceDTO dto = ServiceFactory.getInstance()
                        .getDataSourceService().getDataSource(key);
                    if (dto != null) {
                        return dto;
                    }
                    return DataSourceDTO.NULL_DATASOURCE;
                }
            });

    /**
     * 获取数据源类型
     * @param dataSourceId 数据源ID
     * @return
     */
    public DataSourceType getDataSourceType(Long dataSourceId) {
        if (dataSourceId == null) {
            return DataSourceType.NULL;
        }
        DataSourceDTO dataSourceDTO;
        try {
            dataSourceDTO = DATASOURCE_CACHE.get(dataSourceId);
        } catch (ExecutionException e) {
            throw new ExecuteException(ClientResultCode.DATA_SOUCE_GET_ERROR);
        }
        if (dataSourceDTO == null || DataSourceDTO.NULL_DATASOURCE == dataSourceDTO) {
            return DataSourceType.NULL;
        }
        return DataSourceType.getByName(dataSourceDTO.getType());
    }

}
