package com.concur.meta.client.exception;

/**
 * 服务端执行或返回超时异常
 * 抛出此异常无法保证重试的幂等性, 比如服务端insert成功了, 但是返回时网络异常, 那么第二次重试insert就会主键重复异常
 *
 * @author yongfu.cyf
 * @create 2017-10-30 下午12:07
 **/
public class ResponseTimeoutException extends LModelException {

    public ResponseTimeoutException(Throwable cause) {
        super("服务端执行或返回超时异常", cause);
    }

}
