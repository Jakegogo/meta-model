package com.concur.meta.core.dbengine.meta.metabuilder;

import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.concur.meta.core.dbengine.mapper.ColumnMapper;
import com.concur.meta.core.dbengine.meta.MetaBuilder;
import com.concur.meta.core.dbengine.meta.TableMeta;

/**
 * 注解方式的元数据构建器
 *
 * @author yongfu.cyf
 * @create 2017-06-29 下午8:17
 **/
public class AnnotationMetaBuilder implements MetaBuilder {

    @Override
    public TableMeta build(MetaDatasource metaDatasource, MetaRequest metaRequest) {
        return null;
    }

    @Override
    public ColumnMapper getColumnMapper() {
        return null;
    }

}
