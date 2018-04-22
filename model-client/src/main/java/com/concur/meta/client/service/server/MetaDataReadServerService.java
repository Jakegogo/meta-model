package com.concur.meta.client.service.server;


import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.dataobject.MetaResponse;

/**
 * 元数据服务端读接口
 *
 * @author yongfu.cyf
 * @create 2017-06-28 下午4:06
 **/
public interface MetaDataReadServerService {

    /**
     * 根据主键获取数据
     * @param request 查询参数
     * @return
     */
    MetaResponse get(MetaRequest request);

    /**
     * 分页查询
     * @param request 查询参数
     * @return
     */
    MetaResponse query(MetaRequest request);

}
