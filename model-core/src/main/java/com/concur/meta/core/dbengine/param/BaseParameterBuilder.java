package com.concur.meta.core.dbengine.param;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.concur.meta.client.api.query.QueryParam;
import com.concur.meta.client.constants.ParamKeys;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.result.ServerResultCode;
import com.concur.meta.client.utils.ConvertUtils;
import com.concur.meta.core.dbengine.meta.TableMeta;

/**
 * 执行参数构造器基类
 *
 * @author yongfu.cyf
 * @create 2017-07-03 下午3:52
 **/
public class BaseParameterBuilder implements ParameterBuilder {

    public static final String BATCH_PROCESS_KEY = "list";

    @Override
    public Map<String, Serializable> forModelQuery(TableMeta tableMeta, MetaRequest request) {
        QueryParam queryParam = (QueryParam) request.getParam(ParamKeys.QUERY_PARAM);
        try {
            return ConvertUtils.toMap(queryParam);
        } catch (Exception e) {
            throw new ExecuteException(ServerResultCode.PARAM_CONVERT_ERROR, e);
        }
    }

    @Override
    public Map<String, Serializable> forModelCount(TableMeta tableMeta, MetaRequest request) {
        QueryParam queryParam = (QueryParam) request.getParam(ParamKeys.QUERY_PARAM);
        try {
            return ConvertUtils.toMap(queryParam);
        } catch (Exception e) {
            throw new ExecuteException(ServerResultCode.PARAM_CONVERT_ERROR, e);
        }
    }

    @Override
    public Map<String, Serializable> forModelInsert(TableMeta tableMeta, MetaRequest request) {
        return request.getObject();
    }

    @Override
    public Map<String, Serializable> forModelUpdate(TableMeta tableMeta, MetaRequest request) {
        return request.getObject();
    }

    @Override
    public Map<String, Serializable> forModelDeleteById(TableMeta tableMeta, MetaRequest request) {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        if (request.getObject() == null) {
            return params;
        }
        String primaryKey = tableMeta.getPrimaryKey().getColumnName();
        Serializable pkValue = request.getParam(primaryKey);
        if (pkValue == null) {
            pkValue = request.getParam(ParamKeys.DO_PRIMARY_KEY);
        }
        params.put(primaryKey, pkValue);
        return params;
    }

    @Override
    public Map<String, Serializable> forModelDeleteByColumn(TableMeta tableMeta, MetaRequest request, String[] cloumns) {
        return null;
    }

    @Override
    public Map<String, Serializable> forModelFindById(TableMeta tableMeta, MetaRequest request) {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        if (request.getObject() == null) {
            return params;
        }
        Serializable pkValue = request.getParam(ParamKeys.DO_PRIMARY_KEY);
        params.put(tableMeta.getPrimaryKey().getColumnName(), pkValue);
        return params;
    }

    @Override
    public Map<String, Serializable> forModelFindByIds(TableMeta tableMeta, MetaRequest request) {
        return null;
    }

    @Override
    public Map<String, Serializable> forModelFindByColumn(TableMeta tableMeta, MetaRequest request, String... columnName) {
        return null;
    }

    @Override
    public Map<String, Serializable> forModelBatchInsert(TableMeta tableMeta, MetaRequest request) {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(BATCH_PROCESS_KEY, request.getParam(ParamKeys.BATCH_INSTANCE_LIST));
        return params;
    }

    @Override
    public Map<String, Serializable> forModelBatchDeleteById(TableMeta tableMeta, MetaRequest request) {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(BATCH_PROCESS_KEY, request.getParam(ParamKeys.BATCH_INSTANCE_LIST));
        return params;
    }

}
