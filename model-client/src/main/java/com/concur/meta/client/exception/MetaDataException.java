package com.concur.meta.client.exception;

import com.concur.meta.client.common.IResultCode;

/**
 * 元数据操作异常
 * 需要对元数据异常特殊处理时捕获此异常
 * @author yongfu.cyf
 * @create 2017-06-29 上午10:02
 **/
public class MetaDataException extends TransactionFailException {

    private static final long serialVersionUID = -323201361656657429L;

    public MetaDataException() {
        super();
    }

    public MetaDataException(String message) {
        super(message);
    }

    public MetaDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetaDataException(Throwable cause) {
        super(cause);
    }

    public MetaDataException(IResultCode iResultCode) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage());
    }

    public MetaDataException(IResultCode iResultCode, String message) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage() + ":" + message);
    }

    public MetaDataException(IResultCode iResultCode, Throwable cause) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage(), cause);
    }

    public MetaDataException(IResultCode iResultCode, String detail, Throwable cause) {
        super(iResultCode.getCode() + ":" + iResultCode.getMessage() + ":" + detail, cause);
    }

}
