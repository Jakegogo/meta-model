package com.concur.meta.client.service;

import java.io.Serializable;
import java.util.Map;

import com.concur.meta.client.api.query.AbstractQuery;
import com.concur.meta.client.dataobject.ResponseResult;

/**
 * 元数据服务读接口
 * 
 * @author xinjie.qxj
 *
 */
public interface MetaDataReadService {

    /**
     * 根据主键获取数据
     * @param dataSourceId 数据源ID
     * @param clazz 数据定义类
     * @param id 主键值
     * @param <T> 类型泛型
     * @return
     */
    <T extends Serializable> ResponseResult<Map> get(Long dataSourceId, Class<T> clazz, Serializable id);

    /**
     * 分页查询
     * @param <T>
     * @param query 查询参数
     * @return
     */
    <T extends Serializable> ResponseResult<Map> query(AbstractQuery<T> query);

}
