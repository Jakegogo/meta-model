package com.concur.meta.client.api.transaction;

import java.util.ArrayList;
import java.util.List;

import com.concur.meta.client.api.persist.PersistAction;
import com.concur.meta.client.api.persist.actions.CombineAction;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.exception.ResponseTimeoutException;
import com.concur.meta.client.exception.TransactionFailException;
import com.concur.meta.client.result.ClientResultCode;
import com.concur.meta.client.result.ServerResultCode;
import com.concur.meta.client.service.ServiceFactory;

/**
 * <h6>事务上下文</h6>
 * <li>暂不支持跨库事务,可支持同库原子性提交,提交异常或失败后可以重试提交或回滚(幂等事务)</li>
 * <li>hsf超时情形暂时无法有效保证一致性。有可能服务端已经执行完成,获取未执行。 建议重试提交</li>
 * <li>由于采用C/S模式及成本考虑,该事务只能支持的读已提交, 但不可重复读</li>
 * <li>start()开启事务, Persist操作不会立即入库(insert除外)
 * <li>调用commit()之后, 才会将请求一次发到服务端执行
 * <li>多个操作中只要有任何一个操作失败,会自动回滚, 并且返回success=false
 * <li>insert操作会在commit之前写入库,commit异常之后,调用rollback可以将本次事务中insert的数据进行删除
 * @author yongfu.cyf
 * @create 2017-07-11 下午9:27
 **/
public class Transaction {

    /**
     * 事务上下文
     */
    static ThreadLocal<Transaction> context = new ThreadLocal<Transaction>() {
        @Override
        protected Transaction initialValue() {
            return new Transaction();
        }
    };

    /**
     * 事务状态
     */
    private TransactionStatus status = TransactionStatus.UNUSE;
    /**
     * 动作
     */
    private List<PersistAction> actions = new ArrayList<PersistAction>();

    /**
     * 数据幂等性
     */
    private boolean dataConsistency = false;

    /**
     * 组合操作
     */
    private transient CombineAction combineAction;

    public Transaction() {
        ServiceFactory.getInstance()
            .getMetaDataWriteServerService();
    }

    /**
     * 开始事务
     * 和当前线程绑定
     * @return
     */
    public static Transaction start() {
        Transaction transaction = context.get();
        transaction.status = TransactionStatus.START;
        transaction.actions.clear();
        return transaction;
    }

    /**
     * 是否开启了事务
     * @return
     */
    public boolean isStart() {
        return status.getOrder() >= TransactionStatus.START.getOrder();
    }

    /**
     * 当前线程是否开启了事务
     * @return
     */
    public static boolean isCurrentStart() {
        if (context.get() == null) {
            return false;
        }
        return context.get().status.getOrder() >= TransactionStatus.START.getOrder();
    }


    public List<PersistAction> getActions() {
        return actions;
    }

    /**
     * 执行单个动作
     * @param action PersistAction
     */
    public static void execute(PersistAction action) {
        context.get().addAction(action);
    }


    /**
     * 执行单个动作
     * @param action PersistAction
     */
    public void addAction(PersistAction action) {
        if (action.isExecuteImmediately()) {
            action.executeWithLog();
            add(action);
        } else if (isStart()) {
            add(action);
        } else {
            action.executeWithLog();
        }
    }

    /**
     * 执行提交
     * 提交失败将抛出ExecuteException 异常
     * 提交失败默认自动rollback
     * 失败可重试调用
     * @param transaction 需要提交的事务
     */
    public static void commit(Transaction transaction) throws ExecuteException {
        if (transaction.isStart()) {
            try {
                transaction.status = TransactionStatus.COMMIT;
                if (transaction.combineAction == null) {
                    // autoRollback设置为true, 保持Lmodel服务端的幂等性
                    transaction.combineAction = new CombineAction(transaction.actions, transaction.dataConsistency, true);
                }
                transaction.combineAction.executeWithLog();
                transaction.status = TransactionStatus.SUCCESS;
            } catch (RuntimeException e) {
                // HSF超时无法获知和保证一致性, 有可能服务端已经执行完成,获取未执行。 建议重试提交
                if (e.getCause() != null && e.getCause().toString().contains("HSFTimeOutException")) {
                    throw new ResponseTimeoutException(e);
                }
                transaction.status = TransactionStatus.FAIL;
                throw new TransactionFailException(ClientResultCode.TRANSACTION_FAIL_EXCEPTION, e);
            }
        }
        context.remove();
    }

    /**
     * 执行回滚
     * 调用rollback以将insert的数据删除
     * 失败可重试调用
     * @param transaction 需要提交的事务
     */
    public static void rollback(Transaction transaction) throws ExecuteException {

        if (transaction.combineAction == null) {
            transaction.combineAction = new CombineAction(transaction.actions, transaction.dataConsistency, false);
        }
        try {
            transaction.combineAction.undo();
            transaction.status = TransactionStatus.ROLLBACK;
        } catch (RuntimeException e) {
            // HSF超时无法获知和保证一致性, 有可能服务端已经执行完成,获取未执行。 建议重试回滚
            if (e.getCause() != null && e.getCause().toString().contains("HSFTimeOutException")) {
                throw new ResponseTimeoutException(e);
            }
            throw new ExecuteException(ServerResultCode.ROLLBACK_EXCEPTION, e);
        }
        context.remove();
    }


    /**
     * 添加动作
     * @param action
     */
    private void add(PersistAction action) {
        this.actions.add(action);
    }

    public boolean isDataConsistency() {
        return dataConsistency;
    }

    public void setDataConsistency(boolean dataConsistency) {
        this.dataConsistency = dataConsistency;
    }

}
