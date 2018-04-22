package com.concur.meta.client.api.persist.actions.operation;

import java.util.Collection;

import com.concur.meta.client.dataobject.MetaRequestCreator;
import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.exception.ExecuteException;

/**
 * 批量写入原子操作
 *
 * @author yongfu.cyf
 * @create 2017-09-22 下午2:40
 **/
public class BatchInsertOperation extends AbstractOperation {

    MetaRequest request;

    MetaResponse response;

    private int instanceCount;

    public BatchInsertOperation(Long dataSourceId, Collection<?> instances) {
        super(dataSourceId);
        this.instanceCount = instances.size();
        MetaRequest request = MetaRequestCreator.create(instances);
        DataSourceType dataSourceType = getDataSourceType();
        request.setDataSourceType(dataSourceType);
        request.setDataSourceId(dataSourceId);
        this.request = request;
    }

    @Override
    public BatchInsertOperation operate() {
        MetaResponse response = getWriteServerService().batchInsert(request);
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
        if (response == null) {
            return false;
        }
        Integer result = (Integer) response.getResult();
        if (result != null && result >= instanceCount) {
            return true;
        }
        return false;
    }

    @Override
    public String toDetailMessage() {
        return new StringBuilder("batch insert count:")
            .append(response != null ? response.getResult():null).toString();
    }

}
