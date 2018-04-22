package com.concur.meta.core.extension.postgre;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.concur.meta.core.dbengine.meta.metabuilder.DefaultAutoMetaBuilder;

/**
 * 从数据库build元数据信息
 *
 * @author yongfu.cyf
 * @create 2017-09-06 下午9:00
 **/
public class PostgresSqlAutoMetaBuilder extends DefaultAutoMetaBuilder {

    @Override
    protected ResultSet getTablesResultSet(Connection conn, DatabaseMetaData dbMeta, String tableName)
        throws SQLException {
        return dbMeta.getTables(null, null, tableName, new String[]{"TABLE"});	// 不支持 view 生成
    }
}
