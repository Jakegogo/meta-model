package com.concur.meta.core.dbengine.meta.metabuilder;

import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.concur.meta.core.dbengine.mapper.ColumnMapper;
import com.concur.meta.core.dbengine.meta.MetaBuilder;
import com.concur.meta.core.dbengine.meta.TableMeta;

/**
 * 从配置build元数据信息
 *
 * @author yongfu.cyf
 * @create 2017-06-29 上午10:53
 **/
public class ConfigableMetaBuilder implements MetaBuilder {

    @Override
    public TableMeta build(MetaDatasource metaDatasource, MetaRequest metaRequest) {
        return null;
    }

    @Override
    public ColumnMapper getColumnMapper() {
        return null;
    }

}
