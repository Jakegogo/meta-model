package com.concur.meta.core.manager;

import com.concur.meta.core.dbengine.meta.TableMeta;

/**
 * DB元数据信息管理接口
 *
 * @author yongfu.cyf
 * @create 2017-07-04 上午10:53
 **/
public interface TableMetaManager {

    /**
     * 获取表元数据信息
     * @param className 类名/模型编码
     * @return TableMeta 返回元数据服务提供的TableMeta
     */
    TableMeta getTableMeta(String className);

}
