package com.concur.meta.core.dbengine.meta.metabuilder;

import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.exception.MetaDataException;
import com.concur.meta.client.result.ServerResultCode;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.concur.meta.core.dbengine.mapper.ColumnMapper;
import com.concur.meta.core.manager.TableMetaManager;
import com.concur.meta.core.dbengine.meta.MetaBuilder;
import com.concur.meta.core.dbengine.meta.TableMeta;
import org.apache.commons.lang.StringUtils;

/**
 * 从配置build元数据信息
 *
 * @author yongfu.cyf
 * @create 2017-06-29 上午10:46
 **/
public class ConfigurableMetaBuilder implements MetaBuilder {

    private TableMetaManager tableMetaManager;

    public ConfigurableMetaBuilder(TableMetaManager tableMetaManager) {
        this.tableMetaManager = tableMetaManager;
    }

    @Override
    public TableMeta build(MetaDatasource metaDatasource, MetaRequest metaRequest) {
        String className = metaRequest.getClassName();
        if (StringUtils.isBlank(className)) {
            throw new MetaDataException(ServerResultCode.MATA_CLASS_NAME_IS_BLANK);
        }




        return null;
    }

    @Override
    public ColumnMapper getColumnMapper() {
        return null;
    }

}
