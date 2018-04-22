package com.concur.meta.core.dbengine.meta.metabuilder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.sql.DataSource;

import com.concur.meta.client.constants.ParamKeys;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.exception.MetaDataException;
import com.concur.meta.client.result.ServerResultCode;
import com.concur.meta.client.utils.FieldUtil;
import com.concur.meta.core.dbengine.factory.DataSourceReloadable;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.concur.meta.core.dbengine.mapper.AutoColumnMapper;
import com.concur.meta.core.dbengine.mapper.ColumnMapper;
import com.concur.meta.core.dbengine.meta.ColumnMeta;
import com.concur.meta.core.dbengine.meta.MetaBuilder;
import com.concur.meta.core.dbengine.meta.TableMeta;
import com.concur.meta.core.dbengine.meta.TableMetaImpl;
import com.concur.meta.core.dbengine.sql.dialect.Dialect;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 从数据库build元数据信息
 *
 * @author yongfu.cyf
 * @create 2017-06-29 上午10:46
 **/
public class DefaultAutoMetaBuilder implements MetaBuilder, DataSourceReloadable {

    private static Logger logger = LoggerFactory.getLogger(DefaultAutoMetaBuilder.class);

    private static final String DEFAULT_TABLE_NAME ="default";

    private ColumnMapper columnMapper = new AutoColumnMapper();

    @Override
    public TableMeta build(MetaDatasource metaDatasource, MetaRequest metaRequest) {
        // TODO 字段名转换成属性名
        return getTableMeta(metaRequest, metaDatasource.getDataSource(), metaDatasource.getSqlBuilder().getDialect());
    }

    /**
     * TableMeta缓存
     */
    private ConcurrentMap<String, TableMeta> tableMetaMap = new ConcurrentHashMap<String, TableMeta>();


    public TableMeta getTableMeta(MetaRequest metaRequest, DataSource dataSource, Dialect dialect) {
        String className = metaRequest.getClassName();
        if (StringUtils.isBlank(className)) {
            throw new MetaDataException(ServerResultCode.MATA_CLASS_NAME_IS_BLANK);
        }

        // 获取缓存
        TableMeta tableMeta = tableMetaMap.get(className);
        if (tableMeta != null) {
            return tableMeta;
        }

        // 获取表名
        String tableName = (String) metaRequest.getParam(ParamKeys.LMODEL_TABLE_NAME);
        if (StringUtils.isBlank(tableName) || DEFAULT_TABLE_NAME.equals(tableName)) {
            String simpleName = className.substring(className.lastIndexOf('.') + 1);
            tableName = FieldUtil.underscoreName(simpleName);
        }

        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData dbMeta = conn.getMetaData();
            rs = getTablesResultSet(conn, dbMeta, tableName);
            while (rs.next()) {
                String tName = rs.getString("TABLE_NAME");

                if (!tableName.equals(tName)) {
                    continue;
                }

                TableMetaImpl tableMetaImpl = new TableMetaImpl();
                tableMetaImpl.setTableName(tName);
                // 构建主键信息
                this.buildPrimaryKey(conn, dbMeta, tableMetaImpl);
                // 构建字段信息
                this.buildColumnMetas(conn, tableMetaImpl, dialect);
                tableMetaMap.put(className, tableMetaImpl);
                break;
            }
        } catch (SQLException e) {
            throw new MetaDataException(ServerResultCode.GET_TABLE_META_EXCEPTION, tableName, e);
        } finally {
            closeResultSet(rs);
            release(conn);
        }

        tableMeta = tableMetaMap.get(className);
        if (tableMeta == null) {
            throw new MetaDataException(ServerResultCode.GET_TABLE_META_EXCEPTION,
                "表" + tableName + "不存在");
        }
        return tableMeta;
    }


    /**

     * 不同数据库 dbMeta.getTables(...) 的 schemaPattern 参数意义不同

     * 1：oracle 数据库这个参数代表 dbMeta.getUserName()

     * 2：postgresql 数据库中需要在 jdbcUrl中配置 schemaPatter，例如：

     *   jdbc:postgresql://localhost:15432/djpt?currentSchema=public,sys,app

     *   最后的参数就是搜索schema的顺序，DruidPlugin 下测试成功

     * 3：开发者若在其它库中发现工作不正常，可通过继承 MetaBuilder并覆盖此方法来实现功能

     */
    protected ResultSet getTablesResultSet(Connection conn, DatabaseMetaData dbMeta, String tableName) throws SQLException {
        String schemaPattern = dbMeta.getUserName();
        // 不支持 view 生成
        return dbMeta.getTables(null, schemaPattern, tableName, new String[]{"TABLE"});

    }

    /**
     * 构建主键
     * @param conn
     * @param dbMeta
     * @param tableMeta
     * @throws SQLException
     */
    protected void buildPrimaryKey(Connection conn, DatabaseMetaData dbMeta, TableMetaImpl tableMeta) throws SQLException {
        ResultSet rs = null;
        try {
            rs = dbMeta.getPrimaryKeys(null, null, tableMeta.getTableName());

            String primaryKey = "";
            int index = 0;
            while (rs.next()) {
                if (index++ > 0) {
                    primaryKey += ",";
                }
                primaryKey += rs.getString("COLUMN_NAME");
            }
            if (StringUtils.isBlank(primaryKey)) {
                throw new MetaDataException("primaryKey of table \"" + tableMeta.getTableName() + "\" required");
            }
            ColumnMeta columnMeta = new ColumnMeta(primaryKey);
            columnMeta.setPropertyName(columnMapper.getPropertyName(primaryKey));
            tableMeta.setPrimaryKey(columnMeta);
            rs.close();
        } finally {
            closeResultSet(rs);
        }
    }

    /**
     * 构建字段元数据信息
     * @param conn
     * @param tableMeta
     * @param dialect
     * @throws SQLException
     */
    protected void buildColumnMetas(Connection conn, TableMetaImpl tableMeta, Dialect dialect) throws SQLException {
        ResultSet rs = null;
        Statement stm = null;
        try {
            String sql = dialect.forTableMetaBuild(tableMeta.getTableName());
            stm = conn.createStatement();
            rs = stm.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();

            List<ColumnMeta> colums = new ArrayList<ColumnMeta>();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                ColumnMeta cm = new ColumnMeta();
                String columnName = rsmd.getColumnName(i);
                cm.setColumnName(columnName);
                cm.setPropertyName(columnMapper.getPropertyName(columnName));
                colums.add(cm);
            }
            tableMeta.setColums(colums);
        } finally {
            closeResultSet(rs);
            closeStatement(stm);
        }
    }

    /**
     * 关闭语句
     * @param stmt
     */
    private void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error("TableMetaManager closeStatement error :" + e.getMessage());
            }
        }
    }

    /**
     * 关闭结果集
     * @param rs
     */
    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("TableMetaManager rs close error :" + e.getMessage());
            }
        }
    }

    /**
     * 释放连接
     */
    void release(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("TableMetaManager close connection error :" + e.getMessage());
            }
        }
    }

    @Override
    public void onReload(long dataSourceId, List<String> classNames) {
        for (String className : classNames) {
            tableMetaMap.remove(className);
        }
    }

    @Override
    public ColumnMapper getColumnMapper() {
        return columnMapper;
    }

    public void setColumnMapper(ColumnMapper columnMapper) {
        this.columnMapper = columnMapper;
    }

}
