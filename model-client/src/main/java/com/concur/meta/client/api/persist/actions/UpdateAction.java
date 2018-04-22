package com.concur.meta.client.api.persist.actions;

import java.io.Serializable;

import com.concur.meta.client.api.persist.PersistAction;
import com.concur.meta.client.api.persist.actions.operation.AbstractOperation;
import com.concur.meta.client.api.persist.actions.operation.UpdateOperation;
import com.concur.meta.client.dataobject.MetaResponse;

/**
 * 更新动作
 *
 * @author yongfu.cyf
 * @create 2017-07-11 下午9:53
 **/
public class UpdateAction<T extends Serializable> extends PersistAction {

    private static final long serialVersionUID = 7420537314298456232L;
    /**
     * 实例对象
     */
    private T instance;
    /**
     * 上一个版本号(用作乐观锁)
     */
    private Serializable lastVersion;

    /**
     * 是否只更新非空字段
     */
    private boolean selective = false;

    /**
     * 构造方法
     * @param instance 更新对象
     */
    public UpdateAction(T instance) {
        this.instance = instance;
    }

    /**
     * 构造方法
     * @param instance 更新对象
     * @param selective 是否只更新非空字段
     */
    public UpdateAction(T instance, boolean selective) {
        this.instance = instance;
        this.selective = selective;
    }

    /**
     * 使用乐观锁
     * @param lastVersion 上一版本的实体
     * @return
     */
    public UpdateAction withCAS(Serializable lastVersion) {
        this.lastVersion = lastVersion;
        return this;
    }

    @Override
    public void execute() {
        if (instance != null) {
            AbstractOperation abstractOperation = getUniqueOperation();
            abstractOperation.operateWithStatus();
            resolveResult(abstractOperation.getResponse());
        }
    }

    @Override
    public AbstractOperation initOperation() {
        return new UpdateOperation(this.dataSourceId, this.instance, this.selective, this.lastVersion);
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
        return this.count >= 1;
    }

    public T getInstance() {
        return instance;
    }

    public boolean isSelective() {
        return selective;
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }

    public void setSelective(boolean selective) {
        this.selective = selective;
    }

    public Serializable getLastVersion() {
        return lastVersion;
    }
}
