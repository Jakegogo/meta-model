package com.concur.meta.client.api.persist.actions.operation;

import java.io.Serializable;

import com.concur.meta.client.api.persist.PersistAction;
import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.dataobject.MetaRequestCreator;
import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.exception.ExecuteException;

/**
 * 写入原子操作
 *
 * @author yongfu.cyf
 * @create 2017-09-22 上午12:17
 **/
public class InsertOperation extends AbstractOperation {

    MetaRequest request;

    MetaResponse response;


    public InsertOperation(Long dataSourceId, Serializable instance, PersistAction undoAction) {
        super(dataSourceId);
        MetaRequest request = MetaRequestCreator.create(instance);
        DataSourceType dataSourceType = getDataSourceType();
        request.setDataSourceType(dataSourceType);
        request.setDataSourceId(dataSourceId);
        this.request = request;
        this.undoOperation = undoAction.getUniqueOperation();
    }


    @Override
    public InsertOperation operate() {
        MetaResponse response = getWriteServerService().insert(request);
        if (response.isFailured()) {
            throw new ExecuteException(response.getResultCode(), response.getErrorMsg());
        }
        this.response = response;
        return this;
    }


    @Override
    public MetaResponse getResponse() {
        return this.response;
    }

    @Override
    public boolean isSuccess() {
        return response != null && response.getResult() != null;
    }

    @Override
    public String toDetailMessage() {
        return new StringBuilder("insert result:")
            .append(response != null ? response.getResult():null).toString();
    }

    public InsertOperation setResponse(MetaResponse response) {
        this.response = response;
        return this;
    }
}
