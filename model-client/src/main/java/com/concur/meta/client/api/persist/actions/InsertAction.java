package com.concur.meta.client.api.persist.actions;

import java.io.Serializable;
import java.util.Map;

import com.concur.meta.client.api.persist.ActionStatus;
import com.concur.meta.client.api.persist.PersistAction;
import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.result.ClientResultCode;
import com.concur.meta.client.utils.ConvertUtils;
import com.concur.meta.client.api.persist.actions.operation.AbstractOperation;
import com.concur.meta.client.api.persist.actions.operation.InsertOperation;
import com.concur.meta.client.exception.ExecuteException;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 添加动作
 *
 * @author yongfu.cyf
 * @create 2017-07-12 上午11:00
 **/
public class InsertAction<T extends Serializable> extends PersistAction<T> {

    private static final long serialVersionUID = -4306043524495847970L;
    /**
     * 实例对象
     */
    private T instance;

    /**
     * 执行状态
     */
    protected ActionStatus actionStatus = ActionStatus.INIT;

    /**
     * 构造方法
     * @param instance 添加对象
     */
    public InsertAction(T instance) {
        this.instance = instance;
        // 初始化撤销操作
        this.undoAction = new DeleteAction(this.instance);
    }

    @Override
    public void execute() {
        if (this.instance != null) {
            AbstractOperation abstractOperation = getUniqueOperation();
            abstractOperation.operateWithStatus();
            resolveResult(abstractOperation.getResponse());
            // 刷新本地的undoAction内容(预先提交类型的Action才需要刷新)
            this.actionStatus = ActionStatus.OK;
            this.setOperation(new InsertOperation(this.dataSourceId, this.instance, this.undoAction)
                .setResponse(abstractOperation.getResponse())
                .setActionStatus(actionStatus));
        }
    }

    @Override
    public AbstractOperation initOperation() {
        return new InsertOperation(this.dataSourceId, this.instance, this.undoAction);
    }

    /**
     * 解析结果
     * @param metaResponse MetaResponse
     */
    @Override
    public void resolveResult(MetaResponse metaResponse) {
        if (metaResponse == null) {
            this.count = 0;
            return;
        }
        // 转换成DO
        Serializable object;
        try {
            object = ConvertUtils.toPoJo(instance.getClass(), (Map) metaResponse.getResult());
            // 将新的属性拷贝到原来的DO
            BeanUtils.copyProperties(instance, object);
        } catch (Exception e) {
            throw new ExecuteException(ClientResultCode.DATA_CONVERT_EXCEPTION, e);
        }
        this.result = this.instance = (T) object;
        if (result != null) {
            this.count = 1;
        }
    }


    @Override
    public boolean isExecuteImmediately() {
        return true;
    }

    @Override
    public boolean isSuccess() {
        return this.count >= 1;
    }

    public T getInstance() {
        return instance;
    }

    public void setInstance(T instance) {
        this.instance = instance;
    }
}


