package com.concur.meta.core.dbengine.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.concur.meta.client.exception.MetaDataException;
import com.concur.meta.client.result.ServerResultCode;
import org.apache.commons.collections.CollectionUtils;

/**
 * 表格元数据信息
 *
 * @author yongfu.cyf
 * @create 2017-07-03 下午5:58
 **/
public class TableMetaImpl implements TableMeta {

    /**
     * 表名
     */
    private String tableName;
    /**
     * 主键
     */
    private ColumnMeta primaryKey;
    /**
     * 字段列表
     */
    private List<ColumnMeta> colums;
    /**
     * 索引信息
     */
    private List<IndexMeta> indexes;

    /**
     * 属性map
     * 属性 - ColumnMeta
     */
    private transient Map<String, ColumnMeta> propertyMap = new HashMap<String, ColumnMeta>();

    /**
     * 字段map
     * 字段 - ColumnMeta
     */
    private transient Map<String, ColumnMeta> columnMap = new HashMap<String, ColumnMeta>();

    @Override
    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public ColumnMeta getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public String getPrimaryKeyName() {
        return primaryKey.getColumnName();
    }

    public void setPrimaryKey(ColumnMeta primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<ColumnMeta> getColums() {
        return colums;
    }

    public void setColums(List<ColumnMeta> colums) {
        this.colums = colums;
        this.initMap(colums);
    }

    /**
     * 初始化MAP
     * @param colums 字段列表
     */
    private void initMap(List<ColumnMeta> colums) {
        if (CollectionUtils.isNotEmpty(colums)) {
            Map<String, ColumnMeta> propertyMap = new HashMap<String, ColumnMeta>();
            Map<String, ColumnMeta> columnMap = new HashMap<String, ColumnMeta>();
            for (ColumnMeta columnMeta : colums) {
                propertyMap.put(columnMeta.getPropertyName(), columnMeta);
                columnMap.put(columnMeta.getColumnName(), columnMeta);
            }
            this.propertyMap = propertyMap;
            this.columnMap = columnMap;
        }
    }

    @Override
    public List<String> getColumnNames() {
        List<String> columns = new ArrayList<String>();
        for (ColumnMeta columnMeta : this.colums) {
            columns.add(columnMeta.getColumnName());
        }
        return columns;
    }

    @Override
    public List<ColumnMeta> getAllColums() {
        return this.colums;
    }

    @Override
    public String getColumnName(String propertyName) {
        ColumnMeta columnMeta = propertyMap.get(propertyName);
        if (columnMeta == null) {
            throw new MetaDataException(ServerResultCode.COLUMN_MAPPING_NOT_FOUND, "propertyName:" + propertyName);
        }
        return columnMeta.getColumnName();
    }

    @Override
    public String getPropertyName(String columnName) {
        ColumnMeta columnMeta = columnMap.get(columnName);
        if (columnMeta == null) {
            throw new MetaDataException(ServerResultCode.COLUMN_MAPPING_NOT_FOUND, "columnName:" + columnName);
        }
        return columnMeta.getColumnName();
    }

    @Override
    public String checkPropertyName(String propertyName) {
        ColumnMeta columnMeta = propertyMap.get(propertyName);
        if (columnMeta == null) {
            throw new MetaDataException(ServerResultCode.COLUMN_MAPPING_NOT_FOUND, "propertyName:" + propertyName);
        }
        return propertyName;
    }

    public List<IndexMeta> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<IndexMeta> indexes) {
        this.indexes = indexes;
    }
}
