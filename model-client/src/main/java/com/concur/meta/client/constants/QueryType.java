package com.concur.meta.client.constants;

/**
 * 查询类型
 *
 * @author yongfu.cyf
 * @create 2017-08-23 下午2:42
 **/
public enum QueryType {

    /**
     * 聚合查询
     */
    AggQuery(1),

    /**
     * 分页查询
     */
    PageQuery(2),

    /**
     * 指定SQL查询
     */
    SqlQuery(3);

    private final int id;

    QueryType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
