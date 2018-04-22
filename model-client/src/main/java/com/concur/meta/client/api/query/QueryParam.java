package com.concur.meta.client.api.query;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.concur.meta.client.result.ClientResultCode;
import com.concur.meta.client.utils.ConvertUtils;
import com.concur.meta.client.common.PageQuery;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.utils.FieldFilter;

/**
 * 查询参数
 * (和数据服务端通信的Object)
 * @author yongfu.cyf
 * @create 2017-06-29 下午4:17
 **/
public class QueryParam<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -6347745199697902377L;
    /**
     * 主键查询
     */
    private Serializable primaryKey;

    /**
     * 条件匹配 where field=XX
     */
    private Map<String, Object> conditions = new LinkedHashMap<String, Object>();

    /**
     * 模糊匹配 where field like '%XX%'
     */
    private Map<String, Object> likes = new LinkedHashMap<String, Object>();

    /**
     * 排序 order by field asc/desc
     */
    private Map<String, SortType> sorts = new LinkedHashMap<String, SortType>();

    /**
     * 范围查询 where field >= start and field <= end
     */
    private Map<String, RangePair> ranges = new LinkedHashMap<String, RangePair>();

    /**
     * 聚合查询
     */
    private Map<AggregateType, Aggregate> aggregates = new LinkedHashMap<AggregateType, Aggregate>();

    /**
     * 分组查询 TODO
     */
    private List<String> groups = new ArrayList<String>();

    /**
     * 分页查询
     */
    private PageQuery pageQuery = new PageQuery();

    /**
     * 注解信息
     */
    private Map<String, Serializable> annotationInfo = new HashMap<String, Serializable>();

    /**
     * 指定Sql语句查询
     */
    private String sql;

    /**
     * 指定Sql语句参数
     */
    private Map<String, Serializable> sqlParams = new HashMap<String, Serializable>();

    public QueryParam() {
    }

    /**
     * 添加过滤条件
     * @param key 属性名

     * @param value 属性值
     * @return
     */
    public QueryParam<T> put(String key, Object value) {
        conditions.put(key, value);
        return this;
    }

    /**
     * 添加过滤条件
     * @param conditions <key 属性名, value 属性值>
     * @return
     */
    public QueryParam<T> putAll(Map<String, Object> conditions) {
        this.conditions.putAll(conditions);
        return this;
    }

    /**
     * 添加过滤条件
     * @param key 属性名
     * @param value 属性值
     * @return
     */
    public QueryParam<T> condition(String key, Object value) {
        conditions.put(key, value);
        return this;
    }

    /**
     * 添加模糊条件
     * @param key 属性名
     * @param value 属性值
     * @return
     */
    public QueryParam<T> like(String key, Object value) {
        likes.put(key, value);
        return this;
    }

    /**
     * 根据Example过滤
     * @param example T
     * @return
     */
    public QueryParam<T> example(Serializable example) {
        try {
            Map<String, Serializable> fieldMap = ConvertUtils.toMapBean(example, new FieldFilter() {
                @Override
                public boolean doFilter(Field field, Object value) {
                    if (value == null) {
                        return false;
                    }
                    if (value.getClass().isArray()) {
                        if (((Object[]) value).length == 0) {
                            return false;
                        }
                    }
                    if (value instanceof Collection) {
                        if (((Collection)value).isEmpty()) {
                            return false;
                        }
                    }
                    if (value instanceof Map) {
                        if (((Map) value).isEmpty()) {
                            return false;
                        }
                    }
                    return true;
                }
            });
            conditions.putAll(fieldMap);
        } catch (Exception e) {
            throw new ExecuteException(ClientResultCode.DATA_CONVERT_EXCEPTION, e);
        }
        return this;
    }

    /**
     * 添加正排序
     * @param key 属性名
     * @return
     */
    public QueryParam<T> asc(String key) {
        sorts.put(key, SortType.ASC);
        return this;
    }

    /**
     * 添加倒排序
     * @param key 属性名
     * @return
     */
    public QueryParam<T> desc(String key) {
        sorts.put(key, SortType.DESC);
        return this;
    }

    /**
     * 添加范围查询
     * @param key 属性名
     * @param start 起始值
     * @param end 结束值
     * @return
     */
    public QueryParam<T> range(String key, Object start, Object end) {
        RangePair rangePair = ranges.get(key);
        if (rangePair == null) {
            rangePair = new RangePair(start, end);
        }
        rangePair.setStart(start);
        rangePair.setEnd(end);
        ranges.put(key, rangePair);
        return this;
    }

    /**
     * 添加范围查询
     * @param key 属性名
     * @param start 起始值
     * @param include 是否包含
     * @return
     */
    public QueryParam<T> rangeStart(String key, Object start, boolean include) {
        RangePair rangePair = ranges.get(key);
        if (rangePair == null) {
            rangePair = new RangePair(start, null);
        }
        rangePair.setStart(start);
        rangePair.setIncludeStart(include);
        ranges.put(key, rangePair);
        return this;
    }

    /**
     * 添加范围查询
     * @param key 属性名
     * @param end 起始值
     * @param include 是否包含
     * @return
     */
    public QueryParam<T> rangeEnd(String key, Object end, boolean include) {
        RangePair rangePair = ranges.get(key);
        if (rangePair == null) {
            rangePair = new RangePair(null, end);
        }
        rangePair.setEnd(end);
        rangePair.setIncludeEnd(include);
        ranges.put(key, rangePair);
        return this;
    }

    /**
     * 统计数量
     * @param key 属性名
     * @return
     */
    public QueryParam<T> count(String... key) {
        this.aggregates.put(AggregateType.COUNT, new Aggregate(AggregateType.COUNT, key));
        return this;
    }

    /**
     * 求和
     * @param key 属性名
     * @return
     */
    public QueryParam<T> sum(String... key) {
        this.aggregates.put(AggregateType.SUM, new Aggregate(AggregateType.SUM, key));
        return this;
    }

    /**
     * 分页查询
     * @param pageNo 页码
     * @return
     */
    public QueryParam<T> page(int pageNo) {
        this.pageQuery.setPageNo(pageNo);
        return this;
    }

    /**
     * 分页大小
     * @param size 分页大小
     * @return
     */
    public QueryParam<T> size(int size) {
        this.pageQuery.setPageSize(size);
        return this;
    }

    /**
     * 起始行
     * @param rowStart 起始行
     */
    public QueryParam<T> rowStart(int rowStart) {
        this.pageQuery.setOffset(rowStart);
        return this;
    }

    /**
     * 分组
     * @param key
     * @return
     */
    public QueryParam<T> groupBy(String key) {
        this.groups.add(key);
        return this;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Serializable> getSqlParams() {
        return sqlParams;
    }

    public void setSqlParams(Map<String, Serializable> sqlParams) {
        this.sqlParams = sqlParams;
    }

    public Map<String, Object> getConditions() {
        return conditions;
    }

    public Map<String, Object> getLikes() {
        return likes;
    }

    public Map<String, SortType> getSorts() {
        return sorts;
    }

    public Map<String, RangePair> getRanges() {
        return ranges;
    }

    public PageQuery getPageQuery() {
        return pageQuery;
    }

    public List<String> getGroups() {
        return groups;
    }

    public Map<AggregateType, Aggregate> getAggregates() {
        return aggregates;
    }

    public Map<String, Serializable> getAnnotationInfo() {
        return annotationInfo;
    }

    public void setAnnotationInfo(Map<String, Serializable> annotationInfo) {
        this.annotationInfo = annotationInfo;
    }

    public Serializable getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(Serializable primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * 是否设定了分页
     * @return
     */
    public boolean isNeedPagenation() {
        if (this.pageQuery == null) {
            return false;
        }
        return this.pageQuery.isNeedPagenation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof QueryParam)) { return false; }

        QueryParam<?> that = (QueryParam<?>)o;

        if (primaryKey != null ? !primaryKey.equals(that.primaryKey) : that.primaryKey != null) { return false; }
        if (conditions != null ? !conditions.equals(that.conditions) : that.conditions != null) { return false; }
        if (sorts != null ? !sorts.equals(that.sorts) : that.sorts != null) { return false; }
        if (ranges != null ? !ranges.equals(that.ranges) : that.ranges != null) { return false; }
        if (aggregates != null ? !aggregates.equals(that.aggregates) : that.aggregates != null) { return false; }
        if (groups != null ? !groups.equals(that.groups) : that.groups != null) { return false; }
        if (pageQuery != null ? !pageQuery.equals(that.pageQuery) : that.pageQuery != null) { return false; }
        return annotationInfo != null ? annotationInfo.equals(that.annotationInfo) : that.annotationInfo == null;
    }

    @Override
    public int hashCode() {
        int result = primaryKey != null ? primaryKey.hashCode() : 0;
        result = 31 * result + (conditions != null ? conditions.hashCode() : 0);
        result = 31 * result + (sorts != null ? sorts.hashCode() : 0);
        result = 31 * result + (ranges != null ? ranges.hashCode() : 0);
        result = 31 * result + (aggregates != null ? aggregates.hashCode() : 0);
        result = 31 * result + (groups != null ? groups.hashCode() : 0);
        result = 31 * result + (pageQuery != null ? pageQuery.hashCode() : 0);
        result = 31 * result + (annotationInfo != null ? annotationInfo.hashCode() : 0);
        return result;
    }


}
