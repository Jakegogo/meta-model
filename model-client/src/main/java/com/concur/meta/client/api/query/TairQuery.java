package com.concur.meta.client.api.query;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Tair查询
 *
 * @author yongfu.cyf
 * @create 2017-07-31 上午10:45
 **/
public class TairQuery<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -5039310318509444695L;
    /**
     * 是否使用本地缓存
     */
    private boolean withLocalCache = false;
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    /**
     * DB查询参数
     */
    private transient Query<T> dbQuery;

    /**
     * 创建查询
     * @param clazz 类型
     * @param <T>
     * @return
     */
    public static <T extends Serializable> TairQuery<T> create(Class<T> clazz) {

        return null;
    }

    /**
     * 创建查询
     * @param query DB查询参数
     * @param <T>
     * @return
     */
    public static <T extends Serializable> TairQuery<T> create(Query<T> query) {

        return null;
    }

    /**
     * 指定数据源ID
     * @param dataSourceId Long
     * @return
     */
    public TairQuery<T> dataSource(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
        return this;
    }

    /**
     * 是否本地缓存
     * @return
     */
    public TairQuery<T> withLocalCache() {
        this.withLocalCache = true;
        return this;
    }

    /**
     * 设置超时
     * @param timeOut 超时
     * @param timeUnit 超时单位
     * @return
     */
    public TairQuery<T> timeout(long timeOut, TimeUnit timeUnit) {

        return this;
    }

    /**
     * 查询
     * @param key 查询Key
     * @return
     */
    public T get(String namespace, Serializable key) {

        return null;
    }

    /**
     * 写入
     * @param key 查询Key
     * @param value 值
     * @return
     */
    public T put(String namespace, Serializable key, Serializable value) {

        return null;
    }

    /**
     * 清除缓存
     * @param key
     * @return
     */
    public boolean evict(Serializable key) {

        return false;
    }

    public Long getDataSourceId() {
        return dataSourceId;
    }
}
