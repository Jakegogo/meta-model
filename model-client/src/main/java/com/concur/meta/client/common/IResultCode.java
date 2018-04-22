/*
 * Taobao.com Inc.
 * Copyright (c) 2012 All Rights Reserved.
 */
package com.concur.meta.client.common;

import java.io.Serializable;

/**
 * 错误信息接口
 *
 * @author yongfu.cyf
 * @create 2017-07-28 下午3:18
 */
public interface IResultCode extends Serializable {

    /**
     * 错误码
     * @return
     */
	String getCode();

    /**
     * 错误信息
     * @return
     */
	String getMessage();

}
