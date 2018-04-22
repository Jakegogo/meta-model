package com.concur.meta.core.dbengine.mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 属性-字段映射
 *
 * @author yongfu.cyf
 * @create 2017-07-07 下午2:40
 **/
public interface ColumnMapper {


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
     * DO映射转换成表字段
     * @param model
     * @return
     */
    Map<String, Serializable> mapModel(Map<String, Serializable> model);

    /**
     * 结果集行转换DO属性
     * @param models
     * @return
     */
    List<Map<String, Serializable>> mapModels(List<Map<String, Serializable>> models);

    /**
     * 单行结果转换DO
     * @param row
     * @return
     */
    Map<String, Object> mapRow(Map<String, Object> row);

    /**
     * 结果集列表转换DO
     * @param rows
     * @return
     */
    List<Map<String, Object>> mapRows(List<Map<String, Object>> rows);

}
