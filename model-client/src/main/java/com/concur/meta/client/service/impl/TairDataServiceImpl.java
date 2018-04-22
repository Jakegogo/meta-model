package com.concur.meta.client.service.impl;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import com.concur.meta.client.service.TairDataService;

/**
 * Tair数据读写服务实现
 *
 * @author yongfu.cyf
 * @create 2017-07-31 下午12:12
 **/
public class TairDataServiceImpl extends BaseMetaDataService<TairDataServiceImpl> implements TairDataService {

    @Override
    public <T extends Serializable> T get(Class<T> clazz, Serializable key) {
        return null;
    }

    @Override
    public boolean put(Class<?> clazz, Serializable key, Serializable value, long timeOut, TimeUnit timeUnit) {
        return false;
    }

    @Override
    public boolean delete(Class<?> clazz, Serializable key) {
        return false;
    }
}
