package com.concur.meta.core.dbengine.factory;


import java.util.List;

import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.metadata.domain.dto.MetaDataSourceDTO;
import com.concur.meta.metadata.domain.dto.MetaModelDTO;

/**
 * 元数据工厂
 *
 * @author yongfu.cyf
 * @create 2017-06-28 上午11:58
 **/
public interface MetaDataFactory {

    /**
     * 获取数据源
     * @param metaRequest 元数据请求
     * @return
     */
    MetaDatasource getDataSource(MetaRequest metaRequest);

    /**
     * 重新缓存数据源
     * @param dataSourceList  List<MetaDataSourceDO>
     * @param lmodelList  List<MetaModelDO>
     */
    void reloadDataSource(List<MetaDataSourceDTO> dataSourceList, List<MetaModelDTO> lmodelList);

    /**
     * 预先设置数据源
     * @param dataSourceId
     */
    void preSetDataSource(Long dataSourceId);
}
