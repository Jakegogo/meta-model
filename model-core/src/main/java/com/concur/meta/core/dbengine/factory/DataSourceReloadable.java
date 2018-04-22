package com.concur.meta.core.dbengine.factory;

import java.util.List;

/**
 * 可重新加载的数据源
 *
 * @author yongfu.cyf
 * @create 2017-07-06 上午10:50
 **/
public interface DataSourceReloadable {

    /**
     * 重新加载触发执行方法
     * @param dataSourceId 数据源ID
     * @param classNames 类集合
     */
    void onReload(long dataSourceId, List<String> classNames);

}
