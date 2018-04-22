package com.concur.meta.core.manager.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.concur.meta.client.exception.MetaDataException;
import com.concur.meta.client.result.ServerResultCode;
import com.concur.meta.core.dbengine.meta.TableMeta;
import com.concur.meta.core.dbengine.factory.DataSourceReloadable;
import com.concur.meta.core.manager.TableMetaManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * DB元数据信息管理
 *
 * @author yongfu.cyf
 * @create 2017-07-04 上午10:53
 **/
public class TableMetaManagerImpl implements TableMetaManager, InitializingBean, DataSourceReloadable {

    private static Logger logger = LoggerFactory.getLogger(TableMetaManagerImpl.class);

    private ConcurrentMap<String, TableMeta> tableMetaMap = new ConcurrentHashMap<String, TableMeta>();

    private static final String DEFAULT_TABLE_NAME ="default";

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化配置端数据库信息

    }


    @Override
    public TableMeta getTableMeta(String className) {
        if (StringUtils.isBlank(className)) {
            throw new MetaDataException(ServerResultCode.MATA_CLASS_NAME_IS_BLANK);
        }

        // 获取缓存
        TableMeta tableMeta = tableMetaMap.get(className);
        if (tableMeta != null) {
            return tableMeta;
        }


        return null;
    }



    @Override
    public void onReload(long dataSourceId, List<String> classNames) {
        for (String className : classNames) {
            tableMetaMap.remove(className);
        }
    }

}
