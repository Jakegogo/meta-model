package com.concur.meta.client.api.result;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.concur.meta.client.api.query.AbstractQuery;
import com.concur.meta.client.api.query.ClientQueryAction;
import org.apache.commons.collections.CollectionUtils;

/**
 * 查询结果
 * <li>懒加载解析结果</li>
 * @author yongfu.cyf
 * @create 2017-06-29 上午11:48
 **/
public class QueryResult<T> extends BaseResult<T> {

    private static final long serialVersionUID = 8261473086611917022L;
    /**
     * 当前页页号，注意页号是从1开始的
     */
    protected int pageNo = 1;
    /**
     * 总记录数
     */
    protected int totalNum = 0;
    /**
     * list结果
     */
    protected List<T> list;

    public QueryResult(Class<T> clazz, ClientQueryAction queryAction) {
        this.clazz = clazz;
        this.queryAction = queryAction;
    }

    public QueryResult(T one) {
        this.one = one;
    }

    public List<T> getList() {
        if (resolveResult().list == null) {
            return Collections.emptyList();
        }
        return resolveResult().list;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getTotalNum() {
        return totalNum;
    }

    /**
     * 获取一个实体对象
     * @return
     */
    @Override
    public T getOne() {
        if (this.one != null) {
            return this.one;
        }
        if (resolveResult().one != null) {
            return resolveResult().one;
        }
        if (CollectionUtils.isEmpty(resolveResult().getList())) {
            return null;
        }
        return resolveResult().getList().get(0);
    }

    @Override
    protected String buildCacheKey() {
        return null;
    }

    /**
     * 处理结果
     * @return
     */
    @Override
    protected QueryResult<T> resolveResult() {
        if (resolved) {
            return this;
        }
        if (queryAction == null) {
            return this;
        }

        // 执行查询
        this.responsePage = queryAction.doQuery();
        if (this.responsePage == null) {
            return this;
        }
        this.pageNo = responsePage.getPageNo();
        this.totalNum = responsePage.getTotalNum();

        List<Map> list = responsePage.getList();
        List<T> doList = super.convertToDOList(list);
        this.list = doList;
        resolved = true;
        return this;
    }

    /**
     * 返回结果合并操作, 服务端执行?
     * @param query
     * @return
     */
    public <R extends Serializable, O extends Serializable> QueryResult<T>
            conbine(AbstractQuery<O> query, Class<R> toClass) {
        return null;
    }

}
