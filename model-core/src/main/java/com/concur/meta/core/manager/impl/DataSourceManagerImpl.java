package com.concur.meta.core.manager.impl;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import com.concur.meta.core.datasource.DataSourceInitializeHandle;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.concur.meta.client.api.query.Query;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.result.ServerResultCode;
import com.concur.meta.client.utils.CacheUtils;
import com.concur.meta.core.manager.DataSourceManager;
import com.concur.meta.metadata.domain.MetaDataSourceDO;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 数据源管理实现
 *
 * @author yongfu.cyf
 * @create 2017-07-05 上午11:59
 **/
public class DataSourceManagerImpl implements DataSourceManager, ApplicationContextAware {

    private static final MetaDataSourceDO NULL_DATASOURCE = new MetaDataSourceDO();

    /**
     * 数据源缓存
     * 查询DB操作在事务之外
     */
    protected LoadingCache<Long, MetaDataSourceDO> dataSourceCache = CacheBuilder.newBuilder()
        .concurrencyLevel(4)
        .maximumSize(1000)
        .refreshAfterWrite(5, TimeUnit.MINUTES)
        .build(
            new CacheUtils.AsynchronousCacheLoader<Long, MetaDataSourceDO>() {
                @Override
                public MetaDataSourceDO load(Long key) {
                    MetaDataSourceDO dto = getMetaDataSourceDO(key);
                    if (dto != null) {
                        return dto;
                    }
                    return NULL_DATASOURCE;
                }
            });

    private MetaDataSourceDO getMetaDataSourceDO(Long dataSourceId) {
        return Query.create(MetaDataSourceDO.class).get(dataSourceId).execute().getOne();
    }

    @Override
    public DataSource getDatasource(Long dataSourceId) {

        MetaDataSourceDO metaDataSourceDO = null;
        try {
            metaDataSourceDO = dataSourceCache.get(dataSourceId);
        } catch (ExecutionException e) {
            throw new ExecuteException(ServerResultCode.SERVER_DATA_SOUCE_GET_ERROR);
        }
        if (metaDataSourceDO == NULL_DATASOURCE) {
            throw new ExecuteException(ServerResultCode.DATA_SOURCE_NOT_EXISTS);
        }

        Map<String, Object> attributes = metaDataSourceDO.getAttributes();
        return DataSourceInitializeHandle.getOrCreateDataSource(metaDataSourceDO.getType(), attributes);
    }


    @Override
    public MetaDataSourceDO getMetaDataSource(Long dataSourceId) {
        try {
            return dataSourceCache.get(dataSourceId);
        } catch (ExecutionException e) {
            throw new ExecuteException(ServerResultCode.SERVER_DATA_SOUCE_GET_ERROR);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }
}
