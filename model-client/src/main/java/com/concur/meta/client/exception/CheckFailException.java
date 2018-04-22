package com.concur.meta.client.exception;

import com.concur.meta.client.common.IResultCode;

/**
 * 校验失败异常
 * 需要对校验异常特殊处理时捕获此异常
 * @author yongfu.cyf
 * @create 2017-09-30 上午10:32
 **/
public class CheckFailException extends TransactionFailException {

    private static final long serialVersionUID = 760052370518979632L;

    public CheckFailException() {
        super();
    }

    public CheckFailException(String message) {
        super(message);
    }

    public CheckFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckFailException(Throwable cause) {
        super(cause);
    }

    public CheckFailException(IResultCode iResultCode) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage());
    }

    public CheckFailException(IResultCode iResultCode, Throwable cause) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage(), cause);
    }

    public CheckFailException(IResultCode iResultCode, String detail, Throwable cause) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage() + ":" + detail, cause);
    }
}
