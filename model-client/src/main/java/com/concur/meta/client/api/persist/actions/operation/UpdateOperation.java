package com.concur.meta.client.api.persist.actions.operation;

import java.io.Serializable;

import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.dataobject.MetaRequestCreator;
import com.concur.meta.client.exception.ExecuteException;

/**
 * 更新原子性操作
 *
 * @author yongfu.cyf
 * @create 2017-09-22 下午3:27
 **/
public class UpdateOperation extends AbstractOperation {

    MetaRequest request;

    MetaResponse response;

    private boolean selective;

    public <T extends Serializable> UpdateOperation(Long dataSourceId, T instance, boolean selective, Serializable lastVersion) {
        super(dataSourceId);
        this.selective = selective;
        MetaRequest request = MetaRequestCreator.create(instance, lastVersion);
        DataSourceType dataSourceType = getDataSourceType();
        request.setDataSourceType(dataSourceType);
        request.setDataSourceId(dataSourceId);
        this.request = request;
    }

    @Override
    public AbstractOperation operate() {
        if (this.selective) {
            this.response = getWriteServerService().updateSelective(request);
        } else {
            this.response = getWriteServerService().update(request);
        }
        if (this.response.isFailured()) {
            throw new ExecuteException(this.response.getResultCode(), this.response.getErrorMsg());
        }
        return this;
    }

    @Override
    public MetaResponse getResponse() {
        return this.response;
    }

    @Override
    public boolean isSuccess() {
        if (response == null) {
            return false;
        }
        Integer result = (Integer) response.getResult();
        if (result == null) {
            return false;
        }
        return result >= 1;
    }

    @Override
    public String toDetailMessage() {
        return new StringBuilder("update count:")
            .append(response != null ? response.getResult():null).toString();
    }

}
