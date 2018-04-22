package com.concur.meta.core.dbengine.execute;

import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.dataobject.MetaResponse;

/**
 * 抽象的写入操作
 *
 * @author yongfu.cyf
 * @create 2017-09-07 上午10:27
 **/
public interface WriteAction {

    /**
     * 是否支数据源
     * @param type
     * @return
     */
    boolean support(DataSourceType type);

    /**
     * 执行请求
     * @param request 查询请求
     * @return
     */
    MetaResponse execute(MetaRequest request);

}
