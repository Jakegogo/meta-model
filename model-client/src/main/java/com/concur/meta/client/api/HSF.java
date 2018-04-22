package com.concur.meta.client.api;

import java.io.Serializable;

import com.concur.meta.client.api.query.HSFQuery;

/**
 * 远程接口操作入口
 *
 * @author yongfu.cyf
 * @create 2017-09-25 下午10:48
 **/
public class HSF {

    public static class Query {
        public static <T extends Serializable> HSFQuery<T> create(Class<T> clazz) {
            return HSFQuery.create(clazz);
        }
    }

    public static Dependency addDependency(String groupId) {
        Dependency dependency = new Dependency(groupId);
        return dependency;
    }

    /**
     * 依赖二方库
     */
    public static class Dependency {
        private String groupId;
        private String artifactId;
        private String version;

        public Dependency(String groupId) {
            this.groupId = groupId;
        }
        public Dependency artifactId(String artifactId) {
            this.artifactId = artifactId;
            return this;
        }
        public Dependency version(String version) {
            this.version = version;
            return this;
        }

        /**
         * 导入依赖
         */
        public void imports() {

        }
    }

}
