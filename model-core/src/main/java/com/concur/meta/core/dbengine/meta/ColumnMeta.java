package com.concur.meta.core.dbengine.meta;

/**
 * 字段信息
 *
 * @author yongfu.cyf
 * @create 2017-06-29 上午11:00
 **/
public class ColumnMeta {

    /**
     * 字段名称
     */
    private String columnName;
    /**
     * 属性名称
     */
    private String propertyName;
    /**
     * Java数据类型
     */
    private String javaType;
    /**
     * Jdbc类型
     */
    private String jdbcType;

    public ColumnMeta() {}

    public ColumnMeta(String columnName) {
        this.columnName = columnName;
    }


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ColumnMeta)) { return false; }

        ColumnMeta that = (ColumnMeta)o;

        return columnName.equals(that.columnName);
    }

    @Override
    public int hashCode() {
        return columnName.hashCode();
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }
}
