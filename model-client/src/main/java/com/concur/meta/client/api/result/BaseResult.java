package com.concur.meta.client.api.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.concur.meta.client.api.query.ClientQueryAction;
import com.concur.meta.client.dataobject.ResponseResult;
import com.concur.meta.client.result.ClientResultCode;
import com.concur.meta.client.utils.ConvertUtils;
import com.concur.meta.client.exception.ExecuteException;
import org.apache.commons.collections.CollectionUtils;

/**
 * 结果基类
 *
 * @author yongfu.cyf
 * @create 2017-08-01 上午10:37
 **/
public abstract class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = 3765471806312335982L;
    /**
     * 转换的目标类
     */
    protected Class<T> clazz;
    /**
     * 服务端返回的查询结果
     */
    protected ResponseResult<Map> responsePage;

    /**
     * 查询Action
     */
    protected ClientQueryAction queryAction;
    /**
     * 结果是否已经解析
     */
    protected boolean resolved = false;

    /**
     * 解析结果集
     * @return
     */
    protected abstract BaseResult<T> resolveResult();


    /**
     * 单个实体结果
     */
    protected T one;

    /**
     * 获取单个结果
     * @return
     */
    public T getResult() {
        return resolveResult().getOne();
    }

    /**
     * 获取一个实体对象
     * @return
     */
    public T getOne() {
        if (resolveResult().one != null) {
            return resolveResult().one;
        }
        return null;
    }

    /**
     * 转换成DO
     * @param pojo map形式的Pojo
     * @return
     */
    protected T convertToDO(Map pojo) {
        // 转换成DO
        T object;
        try {
            object = ConvertUtils.toPoJo(clazz, pojo);
        } catch (Exception e) {
            throw new ExecuteException(ClientResultCode.DATA_CONVERT_EXCEPTION, e);
        }
        return object;
    }

    /**
     * 转换成DO列表
     * @param list List<Map>
     * @return
     */
    protected List<T> convertToDOList(List<Map> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<T> doList = new ArrayList<T>();
        for (Map map : list) {
            doList.add(convertToDO(map));
        }
        return doList;
    }

    /**
     * 是否本地缓存
     * @return
     */
    public BaseResult<T> withLocalCache() {
        return this;
    }

    /**
     * 使用tair缓存层包装
     * @return
     */
    public BaseResult<T> wrapTairCache() {
        return null;
    }

    /**
     * 构建缓存Key
     * @return
     */
    protected abstract String buildCacheKey();

}
