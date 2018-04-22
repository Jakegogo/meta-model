package com.concur.meta.client.common;

import java.io.Serializable;

import com.concur.meta.client.domain.ToString;

/**
 * 与具体ORM实现无关的简单分页参数封装.
 *
 * @author yongfu.cyf
 * @create 2017-07-28 下午3:18
 */
public class PageQuery extends ToString implements Serializable {

    private static final long serialVersionUID = -9194739225859268137L;
    /**
     * 默认分页大小
     */
    public static int DEFAULT_PAGE_SIZE = 20;

    /**
     * 当前页页号，注意页号是从1开始的
     */
    protected int pageNo = 1;
    /**
     * 分页模型每页的大小
     */
	protected int pageSize = DEFAULT_PAGE_SIZE;
    /**
     * 偏移量
     */
	private int offset;

    /**
     * 是否需要分页. 默认不分页
     */
	private boolean needPagenation = false;

    /**
     *  构造函数
     */
    public PageQuery() {

	}

	public PageQuery(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
	 */
	public PageQuery setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
		this.needPagenation = true;
		return this;
	}

	/**
	 * 获得每页的记录数量,默认为15.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的记录数量,低于1时自动调整为默认页大小.
	 */
	public PageQuery setPageSize(final int pageSize) {
		this.pageSize = pageSize;

		if (pageSize < 1) {
			this.pageSize = DEFAULT_PAGE_SIZE;
		}
        this.needPagenation = true;
		return this;
	}

	/**
	 * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置.
	 */
	public Integer getFirst() {
		return (getPageNo() > 0 && getPageSize() > 0) ? ((getPageNo() - 1) * getPageSize() + 0) : 0;
	}

	public int getRowStart() {
		return getFirst();
	}

	/**
	 * 根据pageNo和pageSize计算当前页最后一条记录在总结果集中的位置.
	 */
	public Integer getLast() {
		return (getFirst() + getPageSize() - 1);
	}

    /**
     * 获取起始位置
     * @return
     */
	public int getOffset() {
        return getPageSize() * (getPageNo() - 1);
    }

    public void setOffset(int offset) {
        this.offset = offset;
        this.needPagenation = true;
    }

    /**
     * 设法设定了分页
     * @return
     */
    public boolean isNeedPagenation() {
        return needPagenation;
    }
}
