package com.concur.meta.client.service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Tair数据读写服务接口
 *
 * @author yongfu.cyf
 * @create 2017-07-31 下午12:11
 **/
public interface TairDataService {

    /**
     * 获取tair缓存值
     * @param clazz Class<?>
     * @param key 缓存Key
     * @param <T>
     * @return
     */
    <T extends Serializable> T get(Class<T> clazz, Serializable key);

    /**
     * 写入tair缓存
     * @param clazz Class<?>
     * @param key 缓存Key
     * @param value 缓存值
     * @param timeOut 失效时长
     * @param timeUnit 失效单位
     * @return
     */
    boolean put(Class<?> clazz, Serializable key, Serializable value, long timeOut, TimeUnit timeUnit);

    /**
     * 移除tair缓存值
     * @param clazz Class<?>
     * @param key 缓存Key
     * @return
     */
    boolean delete(Class<?> clazz, Serializable key);

}
