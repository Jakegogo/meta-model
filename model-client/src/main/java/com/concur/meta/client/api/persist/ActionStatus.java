package com.concur.meta.client.api.persist;

import java.io.Serializable;

/**
 * 操作状态
 *
 * @author yongfu.cyf
 * @create 2017-07-12 下午8:06
 **/
public enum ActionStatus implements Serializable {

    /**
     * 初始化状态
     */
    INIT,

    /**
     * 执行成功
     */
    OK,

    /**
     * 已经撤销
     */
    UNDO

}
