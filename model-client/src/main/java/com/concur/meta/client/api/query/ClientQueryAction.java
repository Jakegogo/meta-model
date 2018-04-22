package com.concur.meta.client.api.query;

import java.util.Map;

import com.concur.meta.client.dataobject.ResponseResult;

/**
 * 查询动作
 *
 * @author yongfu.cyf
 * @create 2017-09-01 下午3:27
 **/
public interface ClientQueryAction {

    /**
     * 执行查询
     * @return
     */
    ResponseResult<Map> doQuery();

}
