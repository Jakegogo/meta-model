package com.concur.meta.client.exception;

import com.concur.meta.client.common.IResultCode;

/**
 * 事务执行失败异常
 * 任何LModel内部运行时异常(包括ConsistencyException)都会转化成TransactionFailException
 * 一般都会有脏数据,需要显示catch并rollback
 * @author yongfu.cyf
 * @create 2017-07-28 上午11:52
 **/
public class TransactionFailException extends LModelException {

    private static final long serialVersionUID = 1658681069169052938L;

    public TransactionFailException() {
        super();
    }

    public TransactionFailException(String message) {
        super(message);
    }

    public TransactionFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionFailException(Throwable cause) {
        super(cause);
    }

    public TransactionFailException(IResultCode iResultCode) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage());
    }

    public TransactionFailException(IResultCode iResultCode, Throwable cause) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage(), cause);
    }

    public TransactionFailException(IResultCode iResultCode, String detail, Throwable cause) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage() + ":" + detail, cause);
    }

}
