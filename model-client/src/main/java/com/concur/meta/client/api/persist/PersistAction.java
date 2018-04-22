package com.concur.meta.client.api.persist;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import com.concur.meta.client.logger.ExecuteLogger;
import com.concur.meta.client.api.persist.actions.operation.AbstractOperation;
import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaResponse;

/**
 * 持久化Action(客户端调用执行)
 * <li>实现执行的前端调用和结果解析</li>
 *
 * @author yongfu.cyf
 * @create 2017-07-11 下午9:51
 **/
public abstract class PersistAction<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -7916123914509050904L;

    private static final AtomicLong ID_GEN = new AtomicLong(1);
    public static final int CURRENT_GAP = 1000;
    private final long actionId = genUniqueId();

    /**
     * 生成唯一的ID
     * @return
     */
    private long genUniqueId() {
        long id =  ID_GEN.incrementAndGet();
        if (id >= Long.MAX_VALUE - CURRENT_GAP) {
            ID_GEN.compareAndSet(id, 0);
            id =  ID_GEN.incrementAndGet();
        }
        return id;
    }

    /**
     * 数据源类型
     */
    protected DataSourceType dataSourceType = DataSourceType.MYSQL;
    /**
     * 数据源ID
     */
    protected Long dataSourceId;
    /**
     * 执行结果
     */
    protected T result;
    /**
     * 执行成功数量
     */
    protected int count = -1;

    /**
     * 是否立即执行
     */
    protected boolean executeImmediately = false;

    /**
     * 撤销操作
     */
    protected PersistAction undoAction;

    /**
     * 获取执行的结果
     * 支持Insert操作,会返回写入后带自增主键的DO
     * @return
     */
    public T getResult() {
        return result;
    }

    /**
     * 执行生效的行数
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * 是否执行成功, 判断条件比如updateCount > 0
     * @return
     */
    public abstract boolean isSuccess();

    /**
     * 对应的原子操作
     */
    private AbstractOperation operation;

    /**
     * 初始化原子操作
     * 默认值只获取一次,getUniqueOperation()会缓存结果, 也可以用setOPeration()进行覆盖
     * @return
     */
    public abstract AbstractOperation initOperation();


    /**
     * 获取带唯一标识的操作对象
     * @return
     */
    public AbstractOperation getUniqueOperation() {
        if (operation != null) {
            operation.setActionId(actionId);
            return operation;
        }
        AbstractOperation operation = initOperation();
        operation.setActionId(actionId);
        return operation;
    }

    /**
     * 执行
     */
    public abstract void execute();

    /**
     * 解析结果
     * @param metaResponse
     */
    public abstract void resolveResult(MetaResponse metaResponse);

    /**
     * 执行(打印日志)
     */
    public void executeWithLog() {
        try {
            execute();
        } catch (RuntimeException e) {
            ExecuteLogger.doLogger(e);
            throw e;
        } finally {
            ExecuteLogger.doLogger(getUniqueOperation().getResponse());
        }
    }

    /**
     * 包含状态更改的撤销
     */
    public void undo() {
        if (this.getUniqueOperation() != null) {
            try {
                this.getUniqueOperation().undoWithStatus();
            } catch (RuntimeException e) {
                ExecuteLogger.doLogger(e);
                throw e;
            } finally {
                ExecuteLogger.doLogger(getUniqueOperation().getResponse());
            }
        }
    }

    /**
     * 立即执行入库
     * @return
     */
    public PersistAction<T> executeImmediately() {
        this.executeImmediately = true;
        this.executeWithLog();
        return this;
    }

    /**
     * 解析结果
     */
    public void resolveResults() {
        if (this.getUniqueOperation() != null && this.getUniqueOperation().getResponse() != null) {
            resolveResult(this.getUniqueOperation().getResponse());
        }
    }


    /**
     * 获得回滚的Action
     * @return
     */
    public PersistAction getUndoAction() {
        return this.undoAction;
    }

    /**
     * 是否立即执行
     * @return
     */
    public boolean isExecuteImmediately() {
        return this.executeImmediately;
    }

    /**
     * 是否需要检测一致性
     * @return
     */
    public boolean needCheckConsistency() {
        if (this.getUniqueOperation() != null) {
            return this.getUniqueOperation().needCheckConsistency();
        }
        return true;
    }


    public void setExecuteImmediately(boolean executeImmediately) {
        this.executeImmediately = executeImmediately;
    }


    public ActionStatus getActionStatus() {
        if (this.getUniqueOperation() != null) {
            return this.getUniqueOperation().getActionStatus();
        }
        return ActionStatus.INIT;
    }

    public void setActionStatus(ActionStatus actionStatus) {
        if (this.getUniqueOperation() != null) {
            this.getUniqueOperation().setActionStatus(actionStatus);
        }
    }

    public void setResult(T result) {
        this.result = result;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setUndoAction(PersistAction undoAction) {
        this.undoAction = undoAction;
    }

    public DataSourceType getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public Long getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public long getActionId() {
        return actionId;
    }

    public void setOperation(AbstractOperation operation) {
        this.operation = operation;
    }
}
