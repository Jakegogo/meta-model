package com.concur.meta.core.dbengine.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 字符串数组转换器
 */
public class StringArrayTypeHandler extends BaseTypeHandler<String[]> {
   
    @Override  
    public String[] getNullableResult(ResultSet rs, String columnName)
           throws SQLException {  
       return getStringArray(rs.getString(columnName));  
    }  
   
    @Override  
    public String[] getNullableResult(ResultSet rs, int columnIndex)  
           throws SQLException {
       return this.getStringArray(rs.getString(columnIndex));  
    }  
   
    @Override  
    public String[] getNullableResult(CallableStatement cs, int columnIndex)
           throws SQLException {  
       return this.getStringArray(cs.getString(columnIndex));  
    }  
   
    @Override  
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    String[] parameter, JdbcType jdbcType) throws SQLException {
       StringBuffer result = new StringBuffer();
        for (String value : parameter) { result.append(value).append(","); }
       result.deleteCharAt(result.length()-1);  
       ps.setString(i, result.toString());  
    }  

    private String[] getStringArray(String columnValue) {
        if (columnValue == null) { return null; }
       return columnValue.split(",");  
    }  
}  