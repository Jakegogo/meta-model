package com.concur.meta.core.extension.mysql;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.concur.meta.client.api.query.Aggregate;
import com.concur.meta.client.api.query.AggregateType;
import com.concur.meta.client.api.query.QueryParam;
import com.concur.meta.client.api.query.RangePair;
import com.concur.meta.client.api.query.SortType;
import com.concur.meta.client.constants.ParamKeys;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.dbengine.meta.ColumnMeta;
import com.concur.meta.core.dbengine.meta.TableMeta;
import com.concur.meta.core.dbengine.sql.dialect.Dialect;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

/**
 * Mysql sql语句生成实现
 *
 * @author yongfu.cyf
 * @create 2017-06-28 上午11:15
 **/
public class MysqlDialect extends Dialect {

    @Override
    public String forModelInsert(TableMeta tableMeta) {
        StringBuilder sql = new StringBuilder("insert into `");
        sql.append(tableMeta.getTableName()).append("`(");
        StringBuilder valuePart = new StringBuilder(") values(");

        int i = 0;
        for (ColumnMeta columnMeta : tableMeta.getAllColums()) {
            if (i++ > 0) {
                sql.append(", ");
                valuePart.append(", ");
            }
            sql.append('`').append(columnMeta.getColumnName()).append('`');
            valuePart.append("#{").append(columnMeta.getPropertyName()).append("}");
        }
        sql.append(valuePart.toString()).append(')');
        return sql.toString();
    }

    @Override
    public String forModelBatchInsert(TableMeta tableMeta) {
        StringBuilder sql = new StringBuilder("insert into `");
        sql.append(tableMeta.getTableName()).append("`(");

        int i = 0;
        for (ColumnMeta columnMeta : tableMeta.getAllColums()) {
            if (i++ > 0) {
                sql.append(", ");
            }
            sql.append('`').append(columnMeta.getColumnName()).append('`');
        }

        sql.append(") values");

        i = 0;
        sql.append("<foreach collection=\"list\" item=\"item\" separator=\",\">");
        sql.append("(");
        for (ColumnMeta columnMeta : tableMeta.getAllColums()) {
            if (i++ > 0) {
                sql.append(", ");
            }
            sql.append("#{item.").append(columnMeta.getPropertyName()).append("}");
        }
        sql.append(")");
        sql.append("</foreach>");
        return wrapScript(sql.toString());
    }

    @Override
    public String forModelUpdate(TableMeta tableMeta, MetaRequest metaRequest) {
        String versionField = (String)
            metaRequest.getParam(ParamKeys.MODEL_UPDATE_CAS_VERSION_FIELD);

        StringBuilder sql = new StringBuilder("update `");
        sql.append(tableMeta.getTableName()).append("` set ");

        ColumnMeta primaryKey = tableMeta.getPrimaryKey();
        int i = 0;
        for (ColumnMeta columnMeta : tableMeta.getAllColums()) {
            String colName = columnMeta.getColumnName();
            if (!primaryKey.getColumnName().equals(colName) &&
                !columnMeta.getPropertyName().equals(versionField)) {
                if (i++ > 0) {
                    sql.append(", ");
                }
                sql.append('`').append(colName).append("` = ")
                    .append("#{").append(columnMeta.getPropertyName()).append("}");
            }
        }

        // 乐观锁Sql实现
        if (versionField != null) {
            String versionColumn = tableMeta.getColumnName(versionField);
            if (versionColumn != null) {
                sql.append(", ").append('`').append(versionColumn).append("` = `")
                    .append(versionColumn).append("` + 1");
            }
        }

        sql.append(" where ");

        sql.append('`').append(primaryKey.getColumnName())
            .append("` = #{").append(primaryKey.getPropertyName()).append("}");

        // 乐观锁Sql实现
        if (versionField != null) {
            String versionColumn = tableMeta.getColumnName(versionField);
            if (versionColumn != null) {
                Serializable versionValue = metaRequest.getParam(ParamKeys.MODEL_UPDATE_CAS_VERSION);
                if (versionValue != null) {
                    sql.append(" and `").append(versionColumn)
                        .append("` = #{").append(ParamKeys.MODEL_UPDATE_CAS_VERSION).append("}");
                }
            }
        }
        return sql.toString();
    }

    @Override
    public String forModelUpdateSelective(TableMeta tableMeta, MetaRequest metaRequest) {
        Map<String, Serializable> params = metaRequest.getObject();
        String versionField = (String)
            params.get(ParamKeys.MODEL_UPDATE_CAS_VERSION_FIELD);

        StringBuilder sql = new StringBuilder("update `");
        sql.append(tableMeta.getTableName()).append("` set ");

        ColumnMeta primaryKey = tableMeta.getPrimaryKey();
        int i = 0;
        for (ColumnMeta columnMeta : tableMeta.getAllColums()) {
            if (params.get(columnMeta.getPropertyName()) == null) {
                continue;
            }

            String colName = columnMeta.getColumnName();
            if (!primaryKey.getColumnName().equals(colName) &&
                !columnMeta.getPropertyName().equals(versionField)) {
                if (i++ > 0) {
                    sql.append(", ");
                }
                sql.append('`').append(colName).append("` = ")
                    .append("#{").append(columnMeta.getPropertyName()).append("}");
            }
        }

        // 乐观锁Sql实现
        if (versionField != null) {
            String versionColumn = tableMeta.getColumnName(versionField);
            if (versionColumn != null) {
                sql.append(", ").append('`').append(versionColumn).append("` = ")
                    .append(versionColumn).append("+1");
            }
        }

        sql.append(" where ");

        sql.append('`').append(primaryKey.getColumnName())
            .append("` = #{").append(primaryKey.getPropertyName()).append("}");

        // 乐观锁Sql实现
        if (versionField != null) {
            String versionColumn = tableMeta.getColumnName(versionField);
            if (versionColumn != null) {
                Serializable versionValue = metaRequest.getParam(ParamKeys.MODEL_UPDATE_CAS_VERSION);
                if (versionValue != null) {
                    sql.append(" and `").append(versionColumn)
                        .append("` = #{").append(ParamKeys.MODEL_UPDATE_CAS_VERSION).append("}");
                }
            }
        }

        return sql.toString();
    }

    @Override
    public String forModelDeleteById(TableMeta tableMeta) {
        StringBuilder sql = new StringBuilder(100);
        sql.append("delete from `");
        sql.append(tableMeta.getTableName());
        sql.append("` where ");

        ColumnMeta primaryKey = tableMeta.getPrimaryKey();
        sql.append('`').append(primaryKey.getColumnName())
            .append("` = #{").append(primaryKey.getPropertyName()).append("}");
        return sql.toString();
    }

    @Override
    public String forModelBatchDelete(TableMeta tableMeta) {
        StringBuilder sql = new StringBuilder(200);
        sql.append("delete from `");
        sql.append(tableMeta.getTableName());
        sql.append("` where ");

        ColumnMeta primaryKey = tableMeta.getPrimaryKey();
        sql.append('`').append(primaryKey.getColumnName())
            .append("` in ")
            .append("<foreach item=\"item\" index=\"index\" collection=\"list\"")
            .append(" open=\"(\" separator=\",\" close=\")\" >")
            .append(" #{item} ")
            .append("</foreach>");
        return wrapScript(sql.toString());
    }

    @Override
    public String forModelDeleteByColumn(TableMeta tableMeta, String[] cloumns) {
        return null;
    }

    @Override
    public String forModelFindById(TableMeta tableMeta) {
        StringBuilder sql = new StringBuilder("select ");
        List<String> columns = tableMeta.getColumnNames();
        for (int i = 0;i < columns.size(); i++) {
            if (i > 0) {
                sql.append(',');
            }
            String column = columns.get(i);
            sql.append(column);
        }
        sql.append(" from `");
        sql.append(tableMeta.getTableName());
        sql.append("` where ");

        ColumnMeta pKey = tableMeta.getPrimaryKey();
        sql.append('`').append(pKey.getColumnName())
            .append("` = #{").append(pKey.getPropertyName()).append("}");
        return sql.toString();
    }

    @Override
    public String forModelFindByIds(TableMeta tableMeta) {
        return null;
    }

    @Override
    public String forModelFindByColumn(TableMeta tableMeta, String... columnName) {
        return null;
    }

    @Override
    public String forModelQuery(TableMeta tableMeta, QueryParam queryParam) {
        boolean useScript = false;

        StringBuilder sql = new StringBuilder("select ");
        // 聚合查询
        if (queryParam != null && MapUtils.isNotEmpty(queryParam.getAggregates())) {
            int index = 0;
            for (Object entry : queryParam.getAggregates().entrySet()) {
                Map.Entry<AggregateType, Aggregate> aggregateEntry = (Map.Entry<AggregateType, Aggregate>) entry;
                Aggregate aggregate = aggregateEntry.getValue();
                if (index ++ > 0) {
                    sql.append(',');
                }
                sql.append(aggregate.getSqlPart(tableMeta.getPrimaryKeyName()));
            }
        } else {
            List<String> columns = tableMeta.getColumnNames();
            for (int i = 0; i < columns.size(); i++) {
                if (i > 0) {
                    sql.append(',');
                }
                String column = columns.get(i);
                sql.append(column);
            }
        }

        // 构建过滤条件
        useScript |= buildFilter(tableMeta, queryParam, sql);

        // 分页
        if (queryParam != null &&
            queryParam.isNeedPagenation()) {
            sql.append(" limit ").append("#{pageQuery.offset}")
                .append(", ").append("#{pageQuery.pageSize}");
        }

        if (useScript) {
            return wrapScript(sql.toString());
        } else {
            return sql.toString();
        }
    }

    /**
     * 使用<script>包装
     * @param s
     * @return
     */
    private String wrapScript(String s) {
        return new StringBuilder("<script>").append(s).append("</script>").toString();
    }

    /**
     * 构建条件过滤
     * @param tableMeta
     * @param queryParam
     * @param sql
     * @return needScript是否使用了script
     */
    private boolean buildFilter(TableMeta tableMeta, QueryParam queryParam, StringBuilder sql) {
        boolean useScript = false;

        sql.append(" from `");
        sql.append(tableMeta.getTableName())
        .append("` ");

        if (queryParam == null) {
            return false;
        }
        // 条件查询
        int i = 0;
        if (MapUtils.isNotEmpty(queryParam.getConditions())) {
            if (i == 0) {
                sql.append(" where ");
            }
            Map<String, Object> conditions = queryParam.getConditions();
            for (Entry<String, Object> entry : conditions.entrySet()) {
                if (i++ > 0) {
                    sql.append(" and ");
                }

                Object value = entry.getValue();
                if (value == null) {
                    sql.append('`').append(tableMeta.getColumnName(entry.getKey()))
                        .append("` IS NULL ");
                    continue;
                }

                if (value.getClass().isArray()
                    || Collection.class.isAssignableFrom(value.getClass())) {
                    sql.append('`').append(tableMeta.getColumnName(entry.getKey()))
                        .append("` in ")
                        .append("<foreach item=\"item\" index=\"index\" collection=\"conditions.")
                        .append(entry.getKey()).append("\" open=\"(\" separator=\",\" close=\")\" >")
                        .append("  #{item} ")
                        .append("</foreach>");
                    useScript = true;
                } else {
                    sql.append('`').append(tableMeta.getColumnName(entry.getKey()))
                        .append("` = #{conditions.")
                        .append(entry.getKey()).append("}");
                }
            }
        }
        // 范围查询
        if (MapUtils.isNotEmpty(queryParam.getRanges())) {
            if (i == 0) {
                sql.append(" where ");
            }
            Map<String, RangePair> ranges = queryParam.getRanges();
            for (Entry<String, RangePair> entry : ranges.entrySet()) {
                RangePair rangePair = entry.getValue();
                if (rangePair.getStart() != null) {
                    if (i++ > 0) {
                        sql.append(" and ");
                    }
                    sql.append('`').append(tableMeta.getColumnName(entry.getKey()));
                    if (rangePair.isIncludeStart()) {
                        sql.append("` <![CDATA[>=]]> #{ranges.").append(entry.getKey()).append(".start}");
                    } else {
                        sql.append("` <![CDATA[>]]> #{ranges.").append(entry.getKey()).append(".start}");
                    }
                    useScript = true;
                }
                if (rangePair.getEnd() != null) {
                    if (i++ > 0) {
                        sql.append(" and ");
                    }
                    sql.append('`').append(tableMeta.getColumnName(entry.getKey()));
                    if (rangePair.isIncludeEnd()) {
                        sql.append("` <![CDATA[<=]]> #{ranges.").append(entry.getKey()).append(".end}");
                    } else {
                        sql.append("` <![CDATA[<]]> #{ranges.").append(entry.getKey()).append(".end}");
                    }
                    useScript = true;
                }
            }
        }

        // 模糊查询
        if (MapUtils.isNotEmpty(queryParam.getLikes())) {
            if (i == 0) {
                sql.append(" where ");
            }
            Map<String, Object> likes = queryParam.getLikes();
            for (Entry<String, Object> entry : likes.entrySet()) {
                if (i++ > 0) {
                    sql.append(" and ");
                }

                Object value = entry.getValue();
                sql.append('`').append(tableMeta.getColumnName(entry.getKey()))
                    .append("` like CONCAT('%',")
                    .append("#{likes.").append(entry.getKey()).append("},")
                    .append("'%')");
            }
        }

        // 分组查询
        if (CollectionUtils.isNotEmpty(queryParam.getGroups())) {
            sql.append(" group by ");
            List<String> groups = queryParam.getGroups();
            for (String group : groups) {
                sql.append(group).append(' ');
            }
        }

        // 排序
        if (MapUtils.isNotEmpty(queryParam.getSorts())) {
            Map<String, SortType> sorts = queryParam.getSorts();
            sql.append(" order by ");
            int j = 0;
            for (Entry<String, SortType> entry : sorts.entrySet()) {
                if (j++ > 0) {
                    sql.append(" , ");
                }
                sql.append(tableMeta.getColumnName(entry.getKey()))
                    .append(" ").append(entry.getValue().getType());
            }
        }
        return useScript;
    }

    @Override
    public String forModelCount(TableMeta tableMeta, QueryParam queryParam) {
        boolean useScript = false;

        String primaryColumn = tableMeta.getPrimaryKey().getColumnName();
        StringBuilder sql = new StringBuilder("select count(")
            .append(primaryColumn).append(") as total ");
        // 构建过滤条件
        useScript |= buildFilter(tableMeta, queryParam, sql);
        if (useScript) {
            return wrapScript(sql.toString());
        } else {
            return sql.toString();
        }
    }

    @Override
    public String forTableMetaBuild(String tableName) {
        return "select * from `" + tableName + "` where 1 = 2";
    }

}
