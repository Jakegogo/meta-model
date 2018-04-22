package com.concur.meta.core.dbengine.meta;

import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.concur.meta.core.dbengine.mapper.ColumnMapper;

/**
 * 元数据信息builder
 *
 * @author yongfu.cyf
 * @create 2017-06-29 上午10:44
 **/
public interface MetaBuilder {

    /**
     * 构建元数据信息
     * @param metaDatasource DataSource 对象所在的数据源
     * @param metaRequest MetaRequest 数据请求
     * @return
     */
    TableMeta build(MetaDatasource metaDatasource, MetaRequest metaRequest);

    /**
     * 获取字段映射器
     * @return
     */
    ColumnMapper getColumnMapper();
}
