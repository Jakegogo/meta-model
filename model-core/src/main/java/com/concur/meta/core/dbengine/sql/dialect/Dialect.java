package com.concur.meta.core.dbengine.sql.dialect;

import com.concur.meta.client.api.query.QueryParam;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.extension.mysql.MysqlDialect;
import com.concur.meta.core.dbengine.meta.TableMeta;

/**
 * SQL字典接口
 *
 * @author yongfu.cyf
 * @create 2017-06-28 上午11:06
 **/
public abstract class Dialect {

    /**
     * 模型新增语句生成
     * @param tableMeta TableMeta
     *
     */
    public abstract String forModelInsert(TableMeta tableMeta);

    /**
     * 模型批量新增语句生成
     * @param tableMeta TableMeta
     *
     *
     */
    public abstract String forModelBatchInsert(TableMeta tableMeta);

    /**
     * 模型更新语句生成
     * @param tableMeta TableMeta
     * @param metaRequest
     *
     */
    public abstract String forModelUpdate(TableMeta tableMeta, MetaRequest metaRequest);

    /**
     * 模型更新指定字段语句生成
     * @param tableMeta TableMeta
     *
     */
    public abstract String forModelUpdateSelective(TableMeta tableMeta, MetaRequest metaRequest);

    /**
     * 模型删除语句生成
     * @param tableMeta TableMeta
     * @return
     */
    public abstract String forModelDeleteById(TableMeta tableMeta);

    /**
     * 模型批量删除语句生成
     * @param tableMeta TableMeta
     * @return
     */
    public abstract String forModelBatchDelete(TableMeta tableMeta);

    /**
     * 模型删除语句生成
     * @param tableMeta TableMeta
     * @param cloumns
     * @return
     */
    public abstract String forModelDeleteByColumn(TableMeta tableMeta, String[] cloumns);

    /**
     * 从查询参数生成语句
     * @param tableMeta TableMeta
     * @param queryParam
     * @return
     */
    public abstract String forModelQuery(TableMeta tableMeta, QueryParam queryParam);

    /**
     * 从查询参数生成统计数量语句
     * @param tableMeta TableMeta
     * @param queryParam
     * @return
     */
    public abstract String forModelCount(TableMeta tableMeta, QueryParam queryParam);

    /**
     * 模型查找语句生成
     * @param tableMeta TableMeta
     * @return
     */
    public abstract String forModelFindById(TableMeta tableMeta);

    /**
     * 模型批量主键查找语句生成
     * @param tableMeta TableMeta
     * @return
     */
    public abstract String forModelFindByIds(TableMeta tableMeta);

    /**
     * 模型按列查找语句生成
     * @param tableMeta TableMeta
     * @param columnName
     * @return
     */
    public abstract String forModelFindByColumn(TableMeta tableMeta, String... columnName);

    /**
     * 构建查询表元数据信息的sql语句
     * @param tableName String
     * @return
     */
    public abstract String forTableMetaBuild(String tableName);

    public boolean isOracle() {
        return false;
    }

    public static Dialect getDefaultDialect() {
        return new MysqlDialect();
    }


}
