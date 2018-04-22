package com.concur.meta.core.dbengine.execute;

import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.dataobject.MetaResponse;

/**
 * 执行上下文
 *
 * @author yongfu.cyf
 * @create 2017-08-23 下午2:46
 **/
public class ActionContext {

    /**
     * 查询Action
     */
    private QueryAction queryAction;

    /**
     * 执行请求
     * @param request MetaRequest
     */
    public MetaResponse execute(MetaRequest request) {
        return this.queryAction.execute(request);
    }

    /**
     * 设置查询Action
     * @param queryAction
     */
    public void setQueryAction(QueryAction queryAction) {
        this.queryAction = queryAction;
    }

}
