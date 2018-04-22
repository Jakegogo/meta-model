package com.concur.meta.client.exception;

import com.concur.meta.client.common.IResultCode;

/**
 * 数据幂等性异常
 * 需要对幂等异常特殊处理时捕获此异常
 * @author yongfu.cyf
 * @create 2017-07-28 上午11:55
 **/
public class ConsistencyException extends TransactionFailException {

    private static final long serialVersionUID = -1932571583221069108L;

    public ConsistencyException() {
        super();
    }

    public ConsistencyException(String message) {
        super(message);
    }

    public ConsistencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsistencyException(Throwable cause) {
        super(cause);
    }

    public ConsistencyException(IResultCode iResultCode) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage());
    }

    public ConsistencyException(IResultCode iResultCode, String detail) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage() + ":" + detail);
    }

    public ConsistencyException(IResultCode iResultCode, Throwable cause) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage(), cause);
    }

    public ConsistencyException(IResultCode iResultCode, String detail, Throwable cause) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage() + ":" + detail, cause);
    }

}
