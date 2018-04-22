package com.concur.meta.core.dbengine.execute;

import com.concur.meta.client.constants.QueryType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.dataobject.MetaResponse;

/**
 * 查询Action
 *
 * @author yongfu.cyf
 * @create 2017-08-23 上午11:51
 **/
public interface QueryAction {

    /**
     * 是否支持
     * @param type
     * @return
     */
    boolean support(QueryType type);

    /**
     * 执行请求
     * @param request 查询请求
     * @return
     */
    MetaResponse execute(MetaRequest request);

}
