package com.concur.meta.client.exception;

import java.util.List;

import com.concur.meta.client.common.IResultCode;

/**
 * LModel异常
 * 所有自定义异常的超类
 * @author yongfu.cyf
 * @create 2017-06-29 上午10:01
 **/
public abstract class LModelException extends RuntimeException {

    private static final long serialVersionUID = 2672538489232908376L;

    /**
     * 执行日志
     */
    private List<String> executeLog;

    public LModelException() {
        super();
    }

    public LModelException(String message) {
        super(message);
    }

    public LModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public LModelException(Throwable cause) {
        super(cause);
    }

    public LModelException(IResultCode iResultCode) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage());
    }

    public LModelException(IResultCode iResultCode, Throwable cause) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage(), cause);
    }

    public LModelException(IResultCode iResultCode, String detail, Throwable cause) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage() + ":" + detail, cause);
    }

    public List<String> getExecuteLog() {
        return executeLog;
    }

    public LModelException setExecuteLog(List<String> executeLog) {
        this.executeLog = executeLog;
        return this;
    }
}
