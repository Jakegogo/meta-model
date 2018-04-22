package com.concur.meta.client.api.query;

import java.io.Serializable;
import java.util.Map;

/**
 * 远程接口查询
 *
 * @author yongfu.cyf
 * @create 2017-09-25 下午10:51
 **/
public class HSFQuery<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -8824286705328452490L;

    /**
     * 查询的类
     */
    protected Class<T> clazz;

    /**
     * 服务名称
     */
    protected String serviceName;

    /**
     * 服务接口
     */
    protected String serviceMethod;

    /**
     * 参数
     */
    private Map<String, Object> params;

    /**
     * 根据类型创建
     * @param clazz Class<T>
     * @param <T>
     * @return
     */
    public static <T extends Serializable> HSFQuery<T> create(Class<T> clazz) {
        HSFQuery<T> query = new HSFQuery<T>();
        query.clazz = clazz;
        return query;
    }

    /**
     * 设置参数
     * @param params
     * @return
     */
    public HSFQuery<T> params(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    /**
     * 设置参数
     * @param param
     * @return
     */
    public <P extends Serializable> HSFQuery<T> params(P param) {
        this.params = params;
        return this;
    }



}
