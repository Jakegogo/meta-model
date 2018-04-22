package com.concur.meta.client.service.server;

import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.dataobject.MetaRequest;

/**
 * 元数据服务端写接口
 *
 * @author yongfu.cyf
 * @create 2017-06-28 下午4:07
 **/
public interface MetaDataWriteServerService {

    /**
     * 更新数据
     * @param request 更新参数
     * @return
     */
    MetaResponse update(MetaRequest request);

    /**
     * 部分字段更新
     * @param request 更新参数
     * @return
     */
    MetaResponse updateSelective(MetaRequest request);

    /**
     * 添加数据
     * @param request 保存参数
     * @return
     */
    MetaResponse insert(MetaRequest request);

    /**
     * 删除实体
     * @param request 删除参数
     * @return
     */
    MetaResponse delete(MetaRequest request);

    /**
     * 批量添加
     * @param request
     * @return
     */
    MetaResponse batchInsert(MetaRequest request);

    /**
     * 批量删除
     * @param request
     * @return
     */
    MetaResponse batchDelete(MetaRequest request);

    /**
     * 执行所有操作
     * @param request
     * @return
     */
    MetaResponse executeAll(MetaRequest request);
}
