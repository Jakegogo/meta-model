package com.concur.meta.client.api.query;

import java.io.Serializable;

/**
 * 范围参数
 *
 * @author yongfu.cyf
 * @create 2017-06-29 下午4:30
 **/
public class RangePair implements Serializable {

    private static final long serialVersionUID = 7263103860997238176L;
    /**
     * 起始值
     */
    private Object start;
    /**
     * 结束值
     */
    private Object end;

    /**
     * 是否包含起始值
     */
    private boolean includeStart = true;
    /**
     * 是否包含结束值
     */
    private boolean includeEnd = false;

    public boolean isIncludeStart() {
        return includeStart;
    }

    public void setIncludeStart(boolean includeStart) {
        this.includeStart = includeStart;
    }

    public boolean isIncludeEnd() {
        return includeEnd;
    }

    public void setIncludeEnd(boolean includeEnd) {
        this.includeEnd = includeEnd;
    }

    public RangePair(Object start, Object end) {
        this.start = start;
        this.end = end;
    }

    public Object getStart() {
        return start;
    }

    public void setStart(Object start) {
        this.start = start;
    }

    public Object getEnd() {
        return end;
    }

    public void setEnd(Object end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof RangePair)) { return false; }

        RangePair rangePair = (RangePair)o;

        if (includeStart != rangePair.includeStart) { return false; }
        if (includeEnd != rangePair.includeEnd) { return false; }
        if (start != null ? !start.equals(rangePair.start) : rangePair.start != null) { return false; }
        return end != null ? end.equals(rangePair.end) : rangePair.end == null;
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (includeStart ? 1 : 0);
        result = 31 * result + (includeEnd ? 1 : 0);
        return result;
    }
}
