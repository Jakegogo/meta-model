package com.concur.meta.core.dbengine.mapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.concur.meta.client.utils.FieldUtil;
import org.apache.commons.collections.CollectionUtils;

/**
 * 自动映射
 * 驼峰转下划线方式转换字段名
 * @author yongfu.cyf
 * @create 2017-07-07 下午2:42
 **/
public class AutoColumnMapper implements ColumnMapper {

    @Override
    public String getColumnName(String propertyName) {
        return FieldUtil.underscoreName(propertyName);
    }

    @Override
    public String getPropertyName(String columnName) {
        return FieldUtil.camelCaseName(columnName);
    }

    @Override
    public Map<String, Serializable> mapModel(Map<String, Serializable> model) {
        if (model == null) {
            return null;
        }
        Map<String, Serializable> row = new HashMap<String, Serializable>();
        for (Entry<String, Serializable> entry : model.entrySet()) {
            row.put(FieldUtil.underscoreName(entry.getKey()), entry.getValue());
        }
        return row;
    }

    @Override
    public List<Map<String, Serializable>> mapModels(List<Map<String, Serializable>> models) {
        List<Map<String, Serializable>> rows = new ArrayList<Map<String, Serializable>>();
        if (CollectionUtils.isEmpty(models)) {
            return rows;
        }
        for (Map<String, Serializable> model : models) {
            rows.add(mapModel(model));
        }
        return rows;
    }

    @Override
    public Map<String, Object> mapRow(Map<String, Object> row) {
        if (row == null) {
            return null;
        }
        Map<String, Object> model = new HashMap<String, Object>();
        for (Entry<String, Object> entry : row.entrySet()) {
            model.put(FieldUtil.camelCaseName(entry.getKey()), entry.getValue());
        }
        return model;
    }

    @Override
    public List<Map<String, Object>> mapRows(List<Map<String, Object>> rows) {
        List<Map<String, Object>> models = new ArrayList<Map<String, Object>>();
        if (CollectionUtils.isEmpty(rows)) {
            return models;
        }

        for (Map<String, Object> row : rows) {
            models.add(mapRow(row));
        }
        return models;
    }

}
