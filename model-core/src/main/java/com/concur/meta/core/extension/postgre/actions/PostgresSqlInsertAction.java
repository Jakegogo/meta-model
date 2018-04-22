package com.concur.meta.core.extension.postgre.actions;

import java.io.Serializable;
import java.util.Map;

import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.dbengine.execute.SqlMapper;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.concur.meta.core.dbengine.execute.ActionTemplate;
import com.concur.meta.core.dbengine.execute.WriteAction;
import com.concur.meta.core.dbengine.factory.MetaDataFactory;
import com.concur.meta.core.dbengine.meta.TableMeta;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;

/**
 * PostgreSql写入操作
 * PG 不支持return ID
 *
 * @author yongfu.cyf
 * @create 2017-09-07 上午10:29
 **/
public class PostgresSqlInsertAction extends ActionTemplate implements WriteAction {

    public PostgresSqlInsertAction(MetaDataFactory metaDataFactory, SqlSessionFactory sqlSessionFactory) {
        this.metaDataFactory = metaDataFactory;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public boolean support(DataSourceType type) {
        return DataSourceType.POSTGRESQL.equals(type);
    }

    @Override
    protected TransactionIsolationLevel getTransactionIsolationLevel() {
        return TransactionIsolationLevel.READ_COMMITTED;
    }

    @Override
    protected Object executeInner(MetaRequest request, MetaDatasource metaDatasource,
                                  SqlMapper sqlMapper, TableMeta tableMeta) {
        // 获取表元数据信息
        String sql = metaDatasource.getSqlBuilder().buildInsert(tableMeta, request);

        Map<String, Serializable> paramMap = metaDatasource.getParameterBuilder().forModelInsert(tableMeta, request);

        // PG 不支持return ID (keyProperty传null)
        int count = sqlMapper.insert(sql, paramMap, null);
        sqlMapper.commit();
        if (count > 0) {
            return paramMap;
        }
        return null;
    }

}
