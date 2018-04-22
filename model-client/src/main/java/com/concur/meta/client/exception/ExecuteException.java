package com.concur.meta.client.exception;

import com.concur.meta.client.common.IResultCode;
import org.apache.commons.lang.StringUtils;

/**
 * 引擎执行异常
 * 需要对执行异常特殊处理时捕获此异常
 * @author yongfu.cyf
 * @create 2017-06-29 上午11:14
 **/
public class ExecuteException extends TransactionFailException {

    private static final long serialVersionUID = -4979297838122534537L;

    public ExecuteException() {
        super();
    }

    public ExecuteException(String message) {
        super(message);
    }

    public ExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecuteException(Throwable cause) {
        super(cause);
    }

    public ExecuteException(IResultCode iResultCode) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage());
    }

    public ExecuteException(IResultCode iResultCode, Throwable cause) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage(), cause);
    }

    public ExecuteException(IResultCode iResultCode, String... message) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage() + ":" + StringUtils.join(message));
    }

    public ExecuteException(IResultCode iResultCode, String detail, Throwable cause) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage() + ":" + detail, cause);
    }

    public ExecuteException(String errorCode, String errorMsg) {
        super(errorCode + ":" + errorMsg);
    }
}
