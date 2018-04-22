package com.concur.meta.client.service.impl;

import java.io.Serializable;
import java.util.Map;

import com.concur.meta.client.api.query.AbstractQuery;
import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.dataobject.MetaRequestCreator;
import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.dataobject.ResponseResult;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.logger.ExecuteLogger;
import com.concur.meta.client.service.MetaDataReadService;
import com.concur.meta.client.service.ServiceFactory;

/**
 * 元数据服务读接口实现
 *
 * @author yongfu.cyf
 * @create 2017-06-28 下午3:52
 **/
public class MetaDataReadServiceImpl extends BaseMetaDataService<MetaDataReadServiceImpl> implements MetaDataReadService {


    @Override
    public <T extends Serializable> ResponseResult<Map> get(Long dataSourceId, Class<T> clazz, Serializable id) {

        MetaRequest request = MetaRequestCreator.create(clazz, id);
        DataSourceType dataSourceType = getDataSourceType(dataSourceId);
        request.setDataSourceType(dataSourceType);
        request.setDataSourceId(dataSourceId);

        MetaResponse response = null;
        try {
            response = ServiceFactory.getInstance()
                .getMetaDataReadServerService().get(request);
        } catch (RuntimeException e) {
            ExecuteLogger.doLogger(e);
            throw e;
        } finally {
            ExecuteLogger.doLogger(response);
        }
        if (response.isFailured()) {
            throw new ExecuteException(response.getResultCode(), response.getErrorMsg());
        }
        ResponseResult<Map> responseResult = (ResponseResult<Map>) response.getResult();
        return responseResult;
    }


    @Override
    public <T extends Serializable> ResponseResult<Map> query(AbstractQuery<T> query) {
        MetaRequest request = MetaRequestCreator.create(query);
        DataSourceType dataSourceType = getDataSourceType(query.getDataSourceId());
        request.setDataSourceType(dataSourceType);

        MetaResponse response = null;
        try {
            response = ServiceFactory.getInstance()
                .getMetaDataReadServerService().query(request);
        } catch (RuntimeException e) {
            ExecuteLogger.doLogger(e);
            throw e;
        } finally {
            ExecuteLogger.doLogger(response);
        }
        if (response.isFailured()) {
            throw new ExecuteException(response.getResultCode(), response.getErrorMsg());
        }

        ResponseResult<Map> responsePage = (ResponseResult<Map>)response.getResult();
        return responsePage;
    }

}
