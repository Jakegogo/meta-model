package com.concur.meta.client.api.persist.actions.operation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.concur.meta.client.api.persist.PersistAction;
import com.concur.meta.client.api.persist.ActionStatus;
import com.concur.meta.client.constants.ParamKeys;
import com.concur.meta.client.dataobject.MetaRequest;
import com.concur.meta.client.dataobject.MetaRequestCreator;
import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.exception.ExecuteException;

/**
 * 复合操作
 *
 * @author yongfu.cyf
 * @create 2017-09-22 下午4:01
 **/
public class CombineOperation extends AbstractOperation {

    MetaResponse response;

    private boolean dataConsistency;

    private boolean autoRollback;

    private List<AbstractOperation> operations = new ArrayList<AbstractOperation>();
    private List<AbstractOperation> undoOperations = new ArrayList<AbstractOperation>();

    public CombineOperation(Collection<PersistAction> actions, boolean autoRollback, boolean dataConsistency) {
        super(null);
        this.dataConsistency = dataConsistency;
        this.autoRollback = autoRollback;

        this.resolveOperations(actions);
    }

    /**
     * 获取原子操作集合
     * @param actions
     */
    public void resolveOperations(Collection<PersistAction> actions) {
        List<AbstractOperation> undoOperations = new ArrayList<AbstractOperation>();
        List<AbstractOperation> operations = new ArrayList<AbstractOperation>();
        for (PersistAction persistAction : actions) {
            AbstractOperation operation = persistAction.getUniqueOperation();
            operations.add(operation);
            if (operation.getUndoOperation() != null) {
                undoOperations.add(operation.getUndoOperation());
            }
        }
        this.operations = operations;
        this.undoOperations = undoOperations;
    }

    @Override
    public AbstractOperation operate() {
        MetaRequest request = MetaRequestCreator.valueOfOperations(operations);
        request.addAttach(ParamKeys.AUTO_ROLLBACK, autoRollback);
        request.addAttach(ParamKeys.DATA_CONSISTENCY, dataConsistency);

        MetaResponse response = getWriteServerService().executeAll(request);
        if (response.isFailured()) {
            throw new ExecuteException(response.getResultCode(), response.getErrorMsg());
        }
        this.response = response;

        this.operations = (List<AbstractOperation>) response.getResult();
        return this;
    }

    @Override
    public MetaResponse getResponse() {
        return this.response;
    }


    @Override
    public void undo() {
        // 统计未执行的回滚动作数量
        int count = 0;
        for (AbstractOperation undoAction : this.undoOperations) {
            if (undoAction.getActionStatus() != ActionStatus.OK) {
                count ++;
            }
        }

        // 调用批量执行
        if (count > 0) {
            MetaRequest request = MetaRequestCreator.valueOfOperations(this.undoOperations);
            request.addAttach(ParamKeys.AUTO_ROLLBACK, false);
            request.addAttach(ParamKeys.DATA_CONSISTENCY, false);
            MetaResponse response = getWriteServerService().executeAll(request);
            if (response.isFailured()) {
                throw new ExecuteException(response.getResultCode(), response.getErrorMsg());
            }
            this.undoOperations = (List<AbstractOperation>) response.getResult();
        }
    }

    @Override
    public String toDetailMessage() {
        return "";
    }

    @Override
    public boolean isSuccess() {
        if (this.response == null) {
            return false;
        }
        return this.response.isSuccess();
    }
}
