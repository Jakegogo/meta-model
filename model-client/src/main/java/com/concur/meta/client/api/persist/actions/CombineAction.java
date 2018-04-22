package com.concur.meta.client.api.persist.actions;

import java.util.ArrayList;
import java.util.List;

import com.concur.meta.client.api.persist.ActionStatus;
import com.concur.meta.client.api.persist.PersistAction;
import com.concur.meta.client.api.persist.actions.operation.AbstractOperation;
import com.concur.meta.client.api.persist.actions.operation.CombineOperation;
import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.exception.ConsistencyException;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.result.ClientResultCode;
import org.apache.commons.collections.CollectionUtils;

/**
 * 合并动作
 *
 * @author yongfu.cyf
 * @create 2017-09-22 下午3:48
 **/
public class CombineAction extends PersistAction {

    private List<PersistAction> actions;

    private boolean dataConsistency;

    private boolean autoRollback;

    public CombineAction(List<PersistAction> actions, boolean dataConsistency, boolean autoRollback) {
        this.actions = actions;
        this.dataConsistency = dataConsistency;
        this.autoRollback = autoRollback;
    }

    @Override
    public boolean isSuccess() {
        return this.getActionStatus() == ActionStatus.OK;
    }

    @Override
    public AbstractOperation initOperation() {
        AbstractOperation operation = new CombineOperation(actions, autoRollback, dataConsistency);
        setOperation(operation);
        return operation;
    }

    @Override
    public void execute() {
        // 调用执行
        if (getUniqueOperation() != null) {
            getUniqueOperation().operateWithStatus();
            resolveResult(getUniqueOperation().getResponse());
        }
    }

    @Override
    public void resolveResult(MetaResponse metaResponse) {
        if (metaResponse == null) {
            this.count = 0;
            return;
        }
        List<AbstractOperation> operations = (List<AbstractOperation>) metaResponse.getResult();
        try {

            // 一般情况这里不能出异常, 否则会出现数据不一致的情况
            if (!CollectionUtils.isEmpty(operations)) {
                List<String> executeLog = new ArrayList<String>();
                // 刷新原子操作状态到本地Action
                for (AbstractOperation operation : operations) {
                    long actionId = operation.getActionId();
                    for (PersistAction persistAction : this.actions) {
                        if (actionId == persistAction.getActionId()) {
                            // 将返回operation合并到Action
                            persistAction.setOperation(operation);
                            // 解析结果 -- 1
                            persistAction.resolveResults();
                            // 将返回undoOperation合并到UndoAction -- 2
                            AbstractOperation undoOperation = operation.getUndoOperation();
                            if (undoOperation != null && persistAction.getUndoAction() != null) {
                                persistAction.getUndoAction().setOperation(undoOperation);
                                // 解析UndoAction结果
                                persistAction.getUndoAction().resolveResults();
                            }
                        }
                    }
                    // 合并日志
                    if (operation.getResponse() != null && operation.getResponse().getExecuteLog() != null) {
                        executeLog.addAll(operation.getResponse().getExecuteLog());
                    }
                }
                metaResponse.setExecuteLog(executeLog);
                ((CombineOperation) getUniqueOperation()).resolveOperations(this.actions);
            }
        } catch (RuntimeException e) {
            // 此处抛异常一般是处理代码逻辑问题
            throw new ExecuteException(ClientResultCode.RESPONSE_RESOLVE_ERROR, e);
        }

        // 抛出异常
        if (metaResponse.getException() != null) {
            throw metaResponse.getException();
        }

        // 检查数据一致性
        if (dataConsistency) {
            for (AbstractOperation operation : operations) {
                if (operation.needCheckConsistency() && !operation.isSuccess()) {
                    throw new ConsistencyException(ClientResultCode.DATA_CONSISTENCY_EXCEPTION);
                }
            }
        }
    }

}
