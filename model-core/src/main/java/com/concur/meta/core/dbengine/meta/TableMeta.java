package com.concur.meta.core.dbengine.meta;

import java.util.List;

/**
 * 表元信息
 *
 * @author yongfu.cyf
 * @create 2017-06-29 上午10:59
 **/
public interface TableMeta {

    /**
     * 获取表名
     * @return
     */
    String getTableName();

    /**
     * 获取主键
     * @return
     */
    ColumnMeta getPrimaryKey();

    /**
     * 获取主键名
     * @return
     */
    String getPrimaryKeyName();

    /**
     * 获取字段名列表
     * @return
     */
    List<String> getColumnNames();

    /**
     * 获取所有的列
     * @return
     */
    List<ColumnMeta> getAllColums();

    /**
     * 获取字段名
     * @param propertyName 属性名
     * @return
     */
    String getColumnName(String propertyName);

    /**
     * 获取属性名
     * @param columnName 字段名
     * @return
     */
    String getPropertyName(String columnName);

    /**
     * 校验是否存在属性
     * @param propertyName 属性名
     * @return  属性名
     */
    String checkPropertyName(String propertyName);
}
