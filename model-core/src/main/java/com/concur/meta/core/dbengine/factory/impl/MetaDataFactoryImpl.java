package com.concur.meta.core.dbengine.factory.impl;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.concur.meta.core.dbengine.factory.DataSourceReloadable;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.exception.MetaDataException;
import com.concur.meta.client.result.ServerResultCode;
import com.concur.meta.core.dbengine.execute.ExecuteContext;
import com.concur.meta.core.extension.postgre.PostgreSqlDatasourceImpl;
import com.concur.meta.core.dbengine.execute.routing.DynamicDBDataSource;
import com.concur.meta.core.dbengine.factory.MetaDataFactory;
import com.concur.meta.core.extension.mysql.MysqlDatasourceImpl;
import com.concur.meta.core.manager.DataSourceManager;
import com.concur.meta.core.manager.TableMetaManager;
import com.concur.meta.metadata.domain.MetaDataSourceDO;
import com.concur.meta.metadata.domain.dto.MetaDataSourceDTO;
import com.concur.meta.metadata.domain.dto.MetaModelDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 元数据工厂实现
 *
 * @author yongfu.cyf
 * @create 2017-07-03 下午6:07
 **/
public class MetaDataFactoryImpl implements MetaDataFactory, DataSourceReloadable, ApplicationContextAware {

    @Resource
    private TableMetaManager tableMetaManager;
    @Resource
    private DynamicDBDataSource dynamicDBDataSource;
    @Resource
    private DataSourceManager dataSourceManager;

    /**
     * 数据源映射配置Map <数据源ID - MetaDatasource>
     */
    protected Cache<Long, MetaDatasource> dataSourceCache = CacheBuilder.newBuilder()
        .concurrencyLevel(4)
        .maximumSize(1000)
        .expireAfterWrite(5, TimeUnit.MINUTES)
        .build();

    /**
     * 未指定数据源则使用默认数据源
     */
    private volatile BaseMetaDatasource nullMetaDataSource = new NullMetaDataSource();

    @Override
    public MetaDatasource getDataSource(final MetaRequest metaRequest) {
        String className = metaRequest.getClassName();
        if (StringUtils.isBlank(className)) {
            throw new MetaDataException(ServerResultCode.MATA_CLASS_NAME_IS_BLANK);
        }
        Long dataSourceId = metaRequest.getDataSourceId();
        return getMetaDatasourceWithCache(dataSourceId);
    }

    private MetaDatasource getMetaDatasourceWithCache(final Long dataSourceId) {
        if (dataSourceId == null) {
            return nullMetaDataSource;
        }
        try {
            return dataSourceCache.get(dataSourceId,
                new Callable<MetaDatasource>() {
                    @Override
                    public MetaDatasource call() throws Exception {
                        return doGetDataSource(dataSourceId);
                    }
                });
        } catch (ExecutionException e) {
            throw new ExecuteException(e.getCause());
        }
    }

    /**
     * 构建元数据源
     * @param dataSourceId Long
     * @return
     */
    protected MetaDatasource doGetDataSource(Long dataSourceId) {


        if (dataSourceId == null) {
            return nullMetaDataSource;
        }

        // 校验数据源ID
        MetaDataSourceDO metaDataSourceDO = dataSourceManager.getMetaDataSource(dataSourceId);
        if (metaDataSourceDO == null) {
            throw new ExecuteException(ServerResultCode.DATA_SOURCE_NOT_EXISTS);
        }

        // 添加到动态数据源路由
        String datasourceKey = String.valueOf(dataSourceId);
        DataSource dataSource = dataSourceManager.getDatasource(dataSourceId);
        dynamicDBDataSource.addDataSource(datasourceKey, dataSource);


        // 类型工厂创建
        DataSourceType dataSourceType = DataSourceType.getByName(metaDataSourceDO.getType());
        switch (dataSourceType) {
            case MYSQL:
                return new MysqlDatasourceImpl(dataSource, datasourceKey).setTableMetaManager(tableMetaManager);
            case POSTGRESQL:
                return new PostgreSqlDatasourceImpl(dataSource, datasourceKey).setTableMetaManager(tableMetaManager);
        }
        throw new ExecuteException(ServerResultCode.DATA_SOURCE_TYPE_NOT_EXISTS);
    }

    @Override
    public void reloadDataSource(List<MetaDataSourceDTO> dataSourceList, List<MetaModelDTO> lmodelList) {
        // TODO 从配置加载数据源到缓存
    }

    @Override
    public void preSetDataSource(Long dataSourceId) {
        MetaDatasource metaDatasource = getMetaDatasourceWithCache(dataSourceId);
        if (metaDatasource != null) {
            ExecuteContext.setDataSource(metaDatasource.getDataSourceKey());
        }
    }

    @Override
    public void onReload(long dataSourceId, List<String> classNames) {
        dataSourceCache.invalidate(dataSourceId);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        nullMetaDataSource.setTableMetaManager(tableMetaManager);
    }
}
