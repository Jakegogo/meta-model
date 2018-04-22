package com.concur.meta.client.api.transaction;

import com.concur.meta.client.exception.ConsistencyException;
import com.concur.meta.client.exception.ResponseTimeoutException;
import com.concur.meta.client.exception.TransactionFailException;

/**
 * <h5>事务模板</h5>
 * <p>使用事务之后, 多个持久化操作会合并提交。<br/>
 * 好处:</p>
 * <li>合并提交提升性能</li>
 * <li>绝大部分情况下可保证数据一致性, 可以重试提交</li>
 * <p>注意: 当抛出ResponseTimeoutException无法保证幂等性,
 * 因涉及分布式事务问题, 实现成本较高且实际价值有待考量,后续使用过程中可以尝试从业务角度优化这个问题.
 * </p>
 * @author yongfu.cyf
 * @create 2017-09-30 上午11:31
 **/
public class TransactionTemplate {

    /**
     * 执行单库事务模板
     * <li>默认往外抛异常,或者覆写TransactionCallback.onFailed(RuntimeException e)自己处理异常</li>
     * @param transactionCallback 执行回调业务
     * @return
     */
    public static <T> ExecuteStatus execute(TransactionCallback<T> transactionCallback) {
        return execute(transactionCallback, false);
    }

    /**
     * 执行单库事务模板
     * <li>默认往外抛异常,或者覆写TransactionCallback.onFailed(RuntimeException e)自己处理异常</li>
     * @param transactionCallback 执行回调业务
     * @param dataConsistency 是否开启一致性
     * @return
     */
    public static <T> ExecuteStatus execute(TransactionCallback<T> transactionCallback, boolean dataConsistency) {
        // 开启事务
        Transaction transaction = Transaction.start();
        // 需要支持数据幂等性,设置后可捕获DataConsistencyException
        transaction.setDataConsistency(dataConsistency);

        ExecuteStatus executeStatus = new ExecuteStatus();
        // 执行回调方法
        T result = null;
        try {
            result = transactionCallback.doInTransaction(executeStatus);
            // 设置返回结果
            executeStatus.setResult(result);

        } catch (ResponseTimeoutException e) {
            executeStatus.setSuccess(false);
            // 执行失败回调方法
            // 抛出此异常无法保证重试的幂等性
            transactionCallback.onFailed(e);
        } catch (RuntimeException e) {
            // 运行时异常,调用回滚
            Transaction.rollback(transaction);
            executeStatus.setSuccess(false);
            // 执行失败回调方法
            transactionCallback.onFailed(e);
        }

        // 执行提交
        if (executeStatus.isRollbackOnly()) {
            // 调用回滚
            Transaction.rollback(transaction);
            // 执行失败回调方法
            transactionCallback.onFailed(null);
        } else {
            try {
                // 提交到数据库执行更新和删除操作
                Transaction.commit(transaction);
                // 提交成功, 返回成功状态
                executeStatus.setSuccess(true);
                // 执行成功回调方法
                transactionCallback.onSuccess(result);
                return executeStatus;
            } catch (ResponseTimeoutException e) {
                // 执行失败回调方法
                // 抛出此异常无法保证重试的幂等性
                transactionCallback.onFailed(e);
            } catch (ConsistencyException e) {
                // 幂等性异常,比如updateCount==0时抛出; 如果要求幂等性则调用回滚
                Transaction.rollback(transaction);
                // 执行失败回调方法
                transactionCallback.onFailed(e);
            } catch (TransactionFailException e1) {
                // 事务异常 (一般都会有脏数据)
                // 回滚insert的数据(通过deletebyId)
                Transaction.rollback(transaction);
                // 执行失败回调方法
                transactionCallback.onFailed(e1);
            } catch (RuntimeException e2) {// 运行时异常
                // 调用回滚
                Transaction.rollback(transaction);
                // 执行失败回调方法
                transactionCallback.onFailed(e2);
            }
        }

        executeStatus.setSuccess(false);
        return executeStatus;
    }

    /**
     * 模板执行回调
     * @param <T>
     */
    public static abstract class TransactionCallback<T> {
        /**
         * 模板执行回调方法
         * @param executeStatus ExecuteStatus
         * @return
         */
        protected abstract T doInTransaction(ExecuteStatus<T> executeStatus);

        /**
         * 事务执行成功后的回调
         */
        protected void onSuccess(T result) {
        }

        /**
         * 执行失败回调
         * 默认往外抛异常
         * @param e RuntimeException
         */
        protected void onFailed(RuntimeException e) {
            if (e != null) {
                throw e;
            }
        }

    }


    /**
     * 模板执行回调
     */
    public static class ExecuteStatus<T> {
        /**
         * 执行结果
         */
        private T result;

        /**
         * 置为失败, 只允许回滚
         */
        private boolean rollbackOnly = false;
        /**
         * 是否提交成功
         */
        private boolean success;
        /**
         * 错误信息
         */
        private String errorMessage;

        protected boolean isRollbackOnly() {
            return rollbackOnly;
        }

        /**
         * 设置为仅回滚
         */
        public void setRollbackOnly() {
            this.rollbackOnly = true;
            this.success = false;
        }

        protected void setResult(T result) {
            this.result = result;
        }

        /**
         * 获取结果
         * @return
         */
        public T getResult() {
            return result;
        }

        /**
         * 事务是否成功
         * @return
         */
        public boolean isSuccess() {
            return success;
        }

        protected void setSuccess(boolean success) {
            this.success = success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

}
