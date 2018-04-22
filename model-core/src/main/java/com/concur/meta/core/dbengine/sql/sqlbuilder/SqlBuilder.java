package com.concur.meta.core.dbengine.sql.sqlbuilder;

import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.dbengine.sql.dialect.Dialect;
import com.concur.meta.core.dbengine.meta.TableMeta;

/**
 * SQL生成器接口
 *
 * @author yongfu.cyf
 * @create 2017-06-28 上午10:57
 **/
public interface SqlBuilder {

    /**
     * 生成Get的SQL语句
     * @param tableMeta 表元信息
     * @param metaRequest 元数据请求
     * @return SQL语句
     */
    String buildGet(TableMeta tableMeta, MetaRequest metaRequest);

    /**
     * 生成添加的SQL语句
     * @param tableMeta 表元信息
     * @param metaRequest 元数据请求
     * @return SQL语句
     */
    String buildInsert(TableMeta tableMeta, MetaRequest metaRequest);

    /**
     * 生成更新的SQL语句
     * @param tableMeta 表元信息
     * @param metaRequest 元数据请求
     * @return SQL语句
     */
    String buildUpdate(TableMeta tableMeta, MetaRequest metaRequest);

    /**
     * 生成部分字段更新的SQL语句
     * @param tableMeta 表元信息
     * @param metaRequest 元数据请求
     * @return SQL语句
     */
    String buildUpdateSelective(TableMeta tableMeta, MetaRequest metaRequest);

    /**
     * 生成删除的SQL语句
     * @param tableMeta 表元信息
     * @param metaRequest 元数据请求
     * @return SQL语句
     */
    String buildDelete(TableMeta tableMeta, MetaRequest metaRequest);

    /**
     * 生成分页查询的SQL语句
     * @param tableMeta 表元信息
     * @param metaRequest 元数据请求
     * @return SQL语句
     */
    String buildQuery(TableMeta tableMeta, MetaRequest metaRequest);

    /**
     * 生成统计数量查询的SQL语句
     * @param tableMeta 表元信息
     * @param metaRequest 元数据请求
     * @return SQL语句
     */
    String buildCount(TableMeta tableMeta, MetaRequest metaRequest);

    /**
     * 生成批量添加的SQL语句
     * @param tableMeta 表元信息
     * @param metaRequest 元数据请求
     * @return SQL语句
     */
    String buildBatchInsert(TableMeta tableMeta, MetaRequest metaRequest);

    /**
     * 生成批量删除的SQL语句
     * @param tableMeta 表元信息
     * @param metaRequest 元数据请求
     * @return SQL语句
     */
    String buildBatchDelete(TableMeta tableMeta, MetaRequest metaRequest);

    /**
     * 获取字典
     * @return 当前使用的Dialect
     */
    Dialect getDialect();


}
