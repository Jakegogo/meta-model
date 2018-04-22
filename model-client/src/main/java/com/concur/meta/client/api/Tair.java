package com.concur.meta.client.api;

import com.concur.meta.client.api.query.TairQuery;

import java.io.Serializable;

/**
 * Tair操作入口
 *
 * @author yongfu.cyf
 * @create 2017-07-28 下午3:26
 **/
public class Tair {

    public static class Query {
        public static  <T extends Serializable> TairQuery<T> create(Class<T> clazz) {
            return TairQuery.create(clazz);
        }
    }

}
