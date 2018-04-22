package com.concur.meta.client.dataobject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.concur.meta.client.api.query.QueryParam;

/**
 * 查询结果
 *
 * @author yongfu.cyf
 * @create 2017-06-29 上午11:48
 **/
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 8261473086611917022L;
    /**
     * 当前页页号，注意页号是从1开始的
     */
    protected int pageNo = 1;
    /**
     * 总记录数
     */
    protected int totalNum = -1;
    /**
     * list结果
     */
    protected List<T> list;

    public ResponseResult(QueryParam queryParam, int totalNum, T one) {
        this(queryParam, totalNum, Arrays.asList(one));
    }

    public ResponseResult(QueryParam queryParam, int totalNum, List<T> list) {
        if (queryParam != null && queryParam.getPageQuery() != null && queryParam.isNeedPagenation()) {
            this.pageNo = queryParam.getPageQuery().getPageNo();
            int pageSize = queryParam.getPageQuery().getPageSize();
            // fix pageNo
            int totalPageNum = (totalNum  +  pageSize  - 1) / pageSize;
            if (this.pageNo > totalPageNum) {
                this.pageNo = totalPageNum;
            }
            this.totalNum = totalNum;
        }
        this.list = list;
    }

    public List<T> getList() {
        if (this.list == null) {
            return Collections.emptyList();
        }
        return this.list;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getTotalNum() {
        return totalNum;
    }

}
