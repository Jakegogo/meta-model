package com.concur.meta.core.dbengine.execute.actions;

import java.io.Serializable;
import java.util.Map;

import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.core.dbengine.execute.ActionTemplate;
import com.concur.meta.core.dbengine.execute.SqlMapper;
import com.concur.meta.core.dbengine.execute.WriteAction;
import com.concur.meta.core.dbengine.factory.MetaDataFactory;
import com.concur.meta.core.dbengine.factory.MetaDatasource;
import com.concur.meta.core.dbengine.meta.ColumnMeta;
import com.concur.meta.core.dbengine.meta.TableMeta;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;

/**
 * Mysql写入操作
 *
 * @author yongfu.cyf
 * @create 2017-09-07 上午10:29
 **/
public class BaseInsertAction extends ActionTemplate implements WriteAction {

    public BaseInsertAction(MetaDataFactory metaDataFactory, SqlSessionFactory sqlSessionFactory) {
        this.metaDataFactory = metaDataFactory;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public boolean support(DataSourceType type) {
        return true;
    }

    @Override
    protected TransactionIsolationLevel getTransactionIsolationLevel() {
        return TransactionIsolationLevel.READ_COMMITTED;
    }

    @Override
    public Object executeInner(MetaRequest request, MetaDatasource metaDatasource,
                               SqlMapper sqlMapper, TableMeta tableMeta) {
        String sql = metaDatasource.getSqlBuilder().buildInsert(tableMeta, request);

        Map<String, Serializable> paramMap = metaDatasource.getParameterBuilder().forModelInsert(tableMeta, request);
        ColumnMeta primaryKey = tableMeta.getPrimaryKey();

        int count = sqlMapper.insert(sql, paramMap, primaryKey.getColumnName());
        sqlMapper.commit();
        if (count > 0) {
            // 获取主键
            Serializable primaryValue = paramMap.get(primaryKey.getColumnName());
            paramMap.put(primaryKey.getPropertyName(), primaryValue);
            return paramMap;
        }
        return null;
    }

}
