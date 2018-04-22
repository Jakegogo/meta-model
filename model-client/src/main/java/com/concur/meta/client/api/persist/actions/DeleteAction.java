package com.concur.meta.client.api.persist.actions;

import java.io.Serializable;
import java.util.Collection;

import com.concur.meta.client.result.ClientResultCode;
import com.concur.meta.client.api.persist.PersistAction;
import com.concur.meta.client.api.persist.actions.operation.AbstractOperation;
import com.concur.meta.client.api.persist.actions.operation.DeleteOperation;
import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.exception.CheckFailException;

/**
 * 删除动作
 *
 * @author yongfu.cyf
 * @create 2017-07-12 上午11:30
 **/
public class DeleteAction extends PersistAction {

    private static final long serialVersionUID = -6871768239541887716L;
    /**
     * 类型
     */
    private Class<?> clazz;
    /**
     * 删除的ID
     */
    private Serializable id;
    /**
     * 删除的ID集合
     */
    private Collection<?> ids;
    /**
     * 删除的实例
     */
    private Serializable instance;

    public DeleteAction(Class<?> clazz, Serializable id) {
        this.clazz = clazz;
        this.id = id;
    }

    public DeleteAction(Class<?> clazz, Collection<?> ids) {
        this.clazz = clazz;
        this.ids = ids;
    }

    public DeleteAction(Serializable instance) {
        this.clazz = instance.getClass();
        this.instance = instance;
    }

    @Override
    public void execute() {
        if (this.clazz == null) {
            throw new CheckFailException(ClientResultCode.CLASS_CANOT_BE_NULL);
        }

        AbstractOperation abstractOperation = getUniqueOperation();
        abstractOperation.operateWithStatus();
        resolveResult(abstractOperation.getResponse());
    }

    @Override
    public AbstractOperation initOperation() {
        return new DeleteOperation(this.dataSourceId, this.clazz, this.id, this.ids, this.instance);
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
        if (id != null) {
            return this.count >= 1;
        }
        if (ids != null) {
            return this.count >= this.ids.size();
        }
        if (instance != null) {
            return this.count >= 1;
        }
        return false;
    }

    public Serializable getId() {
        return id;
    }

    public Collection<?> getIds() {
        return ids;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Serializable getInstance() {
        return instance;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void setId(Serializable id) {
        this.id = id;
    }

    public void setIds(Collection<?> ids) {
        this.ids = ids;
    }

    public void setInstance(Serializable instance) {
        this.instance = instance;
    }
}
