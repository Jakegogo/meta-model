package com.concur.meta.client.api.transaction;

import java.io.Serializable;

/**
 * 事务状态
 *
 * @author yongfu.cyf
 * @create 2017-07-11 下午9:30
 **/
public enum TransactionStatus implements Serializable {

    /**
     * 未使用事务,默认使用此值
     */
    UNUSE("unuse", "未使用", 0),

    /**
     * 调用Transaction.start()方法之后置为此状态
     */
    START("start", "开启了事务", 1),

    /**
     * 调用了提交(不代表成功)
     */
    COMMIT("commit", "提交了事务", 2),

    /**
     * 事务提交成功了
     */
    SUCCESS("success", "执行成功", 3),

    /**
     * 事务提交失败了
     */
    FAIL("fail", "执行失败,未回滚", 4),

    /**
     * 事务回滚成功了
     */
    ROLLBACK("rollback", "回滚了事务", 5);

    TransactionStatus(String name, String desc, int order) {
        this.name = name;
        this.desc = desc;
        this.order = order;
    }

    private String name;
    private String desc;
    private int order;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getOrder() {
        return order;
    }
}
