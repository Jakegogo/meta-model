package com.concur.meta.core.dbengine.factory.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.utils.CacheUtils;
import com.concur.meta.core.dbengine.meta.MetaBuilder;
import com.concur.meta.core.dbengine.param.BaseParameterBuilder;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.concur.meta.core.dbengine.mapper.AutoColumnMapper;
import com.concur.meta.core.dbengine.mapper.ColumnMapper;
import com.concur.meta.core.dbengine.param.ParameterBuilder;
import com.concur.meta.core.manager.TableMetaManager;
import com.concur.meta.metadata.domain.MetaModelColDO;
import com.concur.meta.metadata.util.ServiceUtil;
import org.apache.commons.collections.CollectionUtils;

/**
 * 元数据源基类
 *
 * @author yongfu.cyf
 * @create 2017-09-22 下午11:30
 **/
public abstract class BaseMetaDatasource implements MetaDatasource {

    /**
     * 表元数据管理器
     */
    protected TableMetaManager tableMetaManager;

    /**
     * 数据源链接
     */
    protected DataSource dataSource;
    /**
     * 元数据构建器
     */
    protected MetaBuilder metaBuilder;
    /**
     * 动态数据源Key
     */
    protected String dataSourceKey;

    /**
     * 模型配置异步缓存
     * 查询DB操作在事务之外
     */
    protected final LoadingCache<String, Boolean> LMODEL_COL_SETTING = CacheBuilder.newBuilder()
        .concurrencyLevel(4)
        .maximumSize(1000)
        .refreshAfterWrite(5, TimeUnit.MINUTES)
        .build(
            new CacheUtils.AsynchronousCacheLoader<String, Boolean>() {
                @Override
                public Boolean load(String key) {
                    List<MetaModelColDO> cols = ServiceUtil.getlMetaService().listColByClass(key);
                    if (CollectionUtils.isEmpty(cols)) {
                        return true;
                    }
                    return false;
                }
            });

    public BaseMetaDatasource(DataSource dataSource, String dataSourceKey) {
        this.dataSource = dataSource;
        this.dataSourceKey = dataSourceKey;
    }


    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public String getDataSourceKey() {
        return this.dataSourceKey;
    }

    @Override
    public MetaBuilder getMetaBuilderWithCache(MetaRequest metaRequest) {
        if (this.metaBuilder != null) {
            return this.metaBuilder;
        }
        MetaBuilder metaBuilder = this.getMetaBuilder(metaRequest);
        this.metaBuilder = metaBuilder;
        return metaBuilder;
    }

    /**
     * 获取元数据信息构建器(构建给执行器使用)
     * @param metaRequest
     * @return
     */
    protected abstract MetaBuilder getMetaBuilder(MetaRequest metaRequest);

    @Override
    public ParameterBuilder getParameterBuilder() {
        return new BaseParameterBuilder();
    }

    @Override
    public ColumnMapper getColumnMapper() {
        if (metaBuilder == null) {
            return new AutoColumnMapper();
        }
        return metaBuilder.getColumnMapper();
    }

    /**
     * 获取元数据信息构建器
     * @param metaRequest MetaRequest
     * @return
     */
    protected boolean isAutoMetaBuilder(MetaRequest metaRequest) {
        if (metaRequest.getDataSourceId() == null) {
            return true;
        }
        String className = metaRequest.getClassName();
        try {
            return LMODEL_COL_SETTING.get(className);
        } catch (ExecutionException e) {
            return false;
        }
    }

    public MetaDatasource setTableMetaManager(TableMetaManager tableMetaManager) {
        this.tableMetaManager = tableMetaManager;
        return this;
    }
}
