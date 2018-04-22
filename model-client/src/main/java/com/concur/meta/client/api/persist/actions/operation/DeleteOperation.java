package com.concur.meta.client.api.persist.actions.operation;

import java.io.Serializable;
import java.util.Collection;

import com.concur.meta.client.dataobject.MetaRequestCreator;
import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.result.ServerResultCode;

/**
 * 删除原子操作
 * 删除操作不做一致性校验(目的仅为了删除)
 * 并且因需要支持重试幂等的特性,不支持通过删除结果来判断是否为本次的删除的
 * 如果需要判断是否为本次删除的场景, 请使用update标记的方式实现逻辑删除
 *
 * @author yongfu.cyf
 * @create 2017-09-22 下午3:07
 **/
public class DeleteOperation extends AbstractOperation {

    MetaRequest request;

    MetaResponse response;

    /**
     * 删除的ID
     */
    private boolean deleteById;
    /**
     * 删除的ID集合
     */
    private boolean deleteByIds;
    /**
     * ID集合大小
     */
    private int idCount;
    /**
     * 删除的实例
     */
    private boolean deleteByInstance;


    public DeleteOperation(Long dataSourceId, Class<?> clazz, Serializable id, Collection<?> ids, Serializable instance) {
        super(dataSourceId);
        if (id != null) {
            MetaRequest request = MetaRequestCreator.create(clazz, id);
            DataSourceType dataSourceType = getDataSourceType();
            request.setDataSourceType(dataSourceType);
            request.setDataSourceId(dataSourceId);
            this.request = request;
            this.deleteById = true;
        } else if (ids != null) {
            MetaRequest request = MetaRequestCreator.create(clazz, ids);
            DataSourceType dataSourceType = getDataSourceType();
            request.setDataSourceType(dataSourceType);
            request.setDataSourceId(dataSourceId);
            this.request = request;
            this.deleteByIds = true;
            this.idCount = ids.size();
        } else if (instance != null) {
            MetaRequest request = MetaRequestCreator.create(instance);
            DataSourceType dataSourceType = getDataSourceType();
            request.setDataSourceType(dataSourceType);
            request.setDataSourceId(dataSourceId);
            this.request = request;
            this.deleteByInstance = true;
        }

    }

    @Override
    public AbstractOperation operate() {

        if (deleteById || deleteByInstance) {
            MetaResponse response = getWriteServerService().delete(request);
            this.response = response;
        } else if (deleteByIds) {
            MetaResponse response = getWriteServerService().batchDelete(request);
            this.response = response;
        } else {
            throw new ExecuteException(ServerResultCode.OPERATION_PARAM_MISSION, new UnsupportedOperationException());
        }
        if (this.response.isFailured()) {
            throw new ExecuteException(this.response.getResultCode(), this.response.getErrorMsg());
        }
        return this;
    }

    @Override
    public MetaResponse getResponse() {
        return response;
    }

    @Override
    public boolean isSuccess() {
        if (response == null) {
            return false;
        }
        Integer count = (Integer) response.getResult();
        if (count == null) {
            return false;
        }
        if (deleteById) {
            return count >= 1;
        }
        if (deleteByIds) {
            return count >= idCount;
        }
        if (deleteByInstance) {
            return count >= 1;
        }
        return false;
    }

    @Override
    public boolean needCheckConsistency() {
        // 删除操作不做一致性校验(目的仅为了删除)
        // 并且因需要支持重试幂等的特性,不支持通过删除结果来判断是否为本次的删除的
        // 如果需要判断是否为本次删除的场景, 请使用update标记的方式实现逻辑删除
        return false;
    }

    @Override
    public String toDetailMessage() {
        return new StringBuilder("delete count:")
            .append(response != null ? response.getResult():null).toString();
    }
}
