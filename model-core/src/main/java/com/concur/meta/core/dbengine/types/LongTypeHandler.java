package com.concur.meta.core.dbengine.types;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * Long数组转换器
 */
public class LongTypeHandler extends BaseTypeHandler<Long> {
   
    @Override  
    public Long getNullableResult(ResultSet rs, String columnName)
           throws SQLException {  
       return rs.getLong(columnName);
    }  
   
    @Override  
    public Long getNullableResult(ResultSet rs, int columnIndex)
           throws SQLException {
       return rs.getLong(columnIndex);
    }  
   
    @Override  
    public Long getNullableResult(CallableStatement cs, int columnIndex)
           throws SQLException {  
       return cs.getLong(columnIndex);
    }  
   
    @Override  
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    Long parameter, JdbcType jdbcType) throws SQLException {
       ps.setLong(i, parameter);
    }  
     
}