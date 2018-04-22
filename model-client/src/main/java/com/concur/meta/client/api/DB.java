package com.concur.meta.client.api;

import java.io.Serializable;

/**
 * 数据库操作入口
 *
 * @author yongfu.cyf
 * @create 2017-07-28 下午3:18
 **/
public class DB {

    public static class Query {
        public static  <T extends Serializable> com.concur.meta.client.api.query.Query<T> create(Class<T> clazz) {
            return com.concur.meta.client.api.query.Query.create(clazz);
        }

        public static  <T extends Serializable> com.concur.meta.client.api.query.Query<T> create(T example) {
            return com.concur.meta.client.api.query.Query.create(example);
        }
    }

    public static class Persist {
        public static  <T extends Serializable> com.concur.meta.client.api.persist.Persist<T> create() {
            return com.concur.meta.client.api.persist.Persist.create();
        }

        public static  <T extends Serializable> com.concur.meta.client.api.persist.Persist<T> create(Class<T> clazz) {
            return com.concur.meta.client.api.persist.Persist.create(clazz);
        }
    }

}
