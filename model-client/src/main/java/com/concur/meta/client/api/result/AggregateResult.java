package com.concur.meta.client.api.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.concur.meta.client.api.query.ClientQueryAction;
import com.concur.meta.client.result.ClientResultCode;
import com.concur.meta.client.utils.ConvertUtils;
import com.concur.meta.client.exception.ExecuteException;

/**
 * 聚合结果
 *
 * @author yongfu.cyf
 * @create 2017-08-01 上午9:51
 **/
public class AggregateResult<T> extends BaseResult<T> {

    private static final long serialVersionUID = 4926518731442971540L;
    /**
     * 集合结果集
     */
    protected List<Map<String, Serializable>> aggResult;

    /**
     * 当前页页号，注意页号是从1开始的
     */
    protected int pageNo = 1;
    /**
     * 总记录数
     */
    protected int totalNum = 0;

    public AggregateResult(Class<T> clazz, ClientQueryAction queryAction) {
        this.clazz = clazz;
        this.queryAction = queryAction;
    }

    /**
     * count(*)或count(id)聚合之后, 返回的key为countId
     * sum(num)聚合之后, 返回的key为sumNum
     * @return
     */
    public List<Map<String, Serializable>> getAggResult() {
        return resolveResult().aggResult;
    }

    @Override
    protected AggregateResult<T> resolveResult() {
        if (resolved) {
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
        List<Map<String, Serializable>> doList = new ArrayList<Map<String, Serializable>>();
        this.aggResult = doList;

        boolean oneResult = false;
        if (list != null) {
            for (Map map : list) {
                if (!oneResult) {
                    for (Object col : map.entrySet()) {
                        Map.Entry aggCol = (Map.Entry)col;
                        try {
                            T object = ConvertUtils.convert(aggCol.getValue(), this.clazz);
                            this.one = object;
                            oneResult = true;
                        } catch (Exception e) {
                            throw new ExecuteException(ClientResultCode.DATA_CONVERT_EXCEPTION, e);
                        }
                    }
                }
                doList.add(map);
            }
        }
        resolved = true;
        return this;
    }

    @Override
    protected String buildCacheKey() {
        return null;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getTotalNum() {
        return totalNum;
    }

    /**
     * 转换为目标结果
     * @param clazz 目标类型
     * @param <R>
     * @return
     */
    public <R> List<R> transferTo(Class<R> clazz) {
        List<Map> list = responsePage.getList();
        List<R> pojoList = new ArrayList<R>();

        for (Map map : list) {
            try {
                R object = ConvertUtils.toPoJo(clazz, map);
                pojoList.add(object);
            } catch (Exception e) {
                throw new ExecuteException(ClientResultCode.DATA_CONVERT_EXCEPTION, e);
            }
        }
        return pojoList;
    }

}
