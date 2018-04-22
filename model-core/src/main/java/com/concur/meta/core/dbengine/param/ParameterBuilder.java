package com.concur.meta.core.dbengine.param;

import java.io.Serializable;
import java.util.Map;

import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.dbengine.meta.TableMeta;

/**
 * 执行参数构造器
 *
 * @author yongfu.cyf
 * @create 2017-06-29 下午9:11
 **/
public interface ParameterBuilder {

    /**
     * 获取参数
     * @param tableMeta TableMeta
     * @param request MetaRequest
     * @return
     */
    Map<String, Serializable> forModelQuery(TableMeta tableMeta, MetaRequest request);

    /**
     * 获取统计参数
     * @param tableMeta TableMeta
     * @param request MetaRequest
     * @return
     */
    Map<String, Serializable> forModelCount(TableMeta tableMeta, MetaRequest request);

    /**
     * 模型更新保存生成
     * @param tableMeta TableMeta
     * @param request MetaRequest
     * @return
     */
    Map<String, Serializable> forModelInsert(TableMeta tableMeta, MetaRequest request);

    /**
     * 模型更新参数生成
     * @param tableMeta TableMeta
     * @param request MetaRequest
     * @return
     */
    Map<String, Serializable> forModelUpdate(TableMeta tableMeta, MetaRequest request);

    /**
     * 模型删除参数生成
     * @param tableMeta TableMeta
     * @param request MetaRequest
     * @return
     */
    Map<String, Serializable> forModelDeleteById(TableMeta tableMeta, MetaRequest request);

    /**
     * 模型删除参数生成
     * @param tableMeta TableMeta
     * @param request MetaRequest
     * @param cloumns 字段列表
     * @return
     */
    Map<String, Serializable> forModelDeleteByColumn(TableMeta tableMeta, MetaRequest request, String[] cloumns);


    /**
     * 模型查找参数生成
     * @param tableMeta TableMeta
     * @param request MetaRequest
     * @return
     */
    Map<String, Serializable> forModelFindById(TableMeta tableMeta, MetaRequest request);

    /**
     * 模型批量主键查找参数生成
     * @param tableMeta TableMeta
     * @param request MetaRequest
     * @return
     */
    Map<String, Serializable> forModelFindByIds(TableMeta tableMeta, MetaRequest request);

    /**
     * 模型按列查找参数生成
     * @param tableMeta TableMeta
     * @param request MetaRequest
     * @return
     */
    Map<String, Serializable> forModelFindByColumn(TableMeta tableMeta, MetaRequest request, String... columnName);

    /**
     * 模型批量更新保存生成
     * @param tableMeta TableMeta
     * @param request MetaRequest
     * @return
     */
    Map<String,Serializable> forModelBatchInsert(TableMeta tableMeta, MetaRequest request);

    /**
     * 模型批量删除参数生成
     * @param tableMeta TableMeta
     * @param request MetaRequest
     * @return
     */
    Map<String,Serializable> forModelBatchDeleteById(TableMeta tableMeta, MetaRequest request);
}
