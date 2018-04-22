package com.concur.meta.client.api.persist.actions;

import java.util.Collection;

import com.concur.meta.client.api.persist.PersistAction;
import com.concur.meta.client.api.persist.actions.operation.AbstractOperation;
import com.concur.meta.client.api.persist.actions.operation.BatchInsertOperation;
import com.concur.meta.client.dataobject.MetaResponse;
import org.apache.commons.collections.CollectionUtils;

/**
 * 批量添加动作
 *
 * @author yongfu.cyf
 * @create 2017-07-12 上午11:00
 **/
public class BatchInsertAction extends PersistAction {

    private static final long serialVersionUID = 2590278481602205628L;
    /**
     * 实例集合
     */
    private Collection<?> instances;

    /**
     * 构造方法
     * @param instances 添加对象集合
     */
    public BatchInsertAction(Collection<?> instances) {
        this.instances = instances;
    }

    @Override
    public void execute() {
        if (!CollectionUtils.isEmpty(instances)) {
            AbstractOperation abstractOperation = getUniqueOperation();
            abstractOperation.operateWithStatus();
            resolveResult(abstractOperation.getResponse());
        }
    }

    @Override
    public AbstractOperation initOperation() {
        return new BatchInsertOperation(this.dataSourceId, this.instances);
    }

    @Override
    public void resolveResult(MetaResponse response) {
        if (response == null) {
            this.count = 0;
            return;
        }
        Integer result = (Integer) response.getResult();
        if (result != null) {
            this.count = result;
        } else {
            this.count = 0;
        }
        this.result = this.count;
    }

    @Override
    public boolean isSuccess() {
        return this.count >= this.instances.size();
    }

    public Collection<?> getInstances() {
        return instances;
    }

    public void setInstances(Collection<?> instances) {
        this.instances = instances;
    }
}
