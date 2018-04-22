package com.concur.meta.client.api.query;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.concur.meta.client.api.result.AggregateResult;
import com.concur.meta.client.constants.QueryType;
import com.concur.meta.client.dataobject.ResponseResult;
import com.concur.meta.client.service.ServiceFactory;
import org.apache.commons.collections.CollectionUtils;

/**
 * 聚合查询
 *
 * @author yongfu.cyf
 * @create 2017-08-01 上午9:41
 **/
public class AggregateQuery<T extends Serializable, R extends Serializable> extends AbstractQuery<T> {

    private static final long serialVersionUID = -6913386358140993312L;
    /**
     * 查询的类
     */
    protected Class<R> resultClass;

    AggregateQuery() {
    }

    AggregateQuery(Query<T> fromQuery, Class<R> toClass) {
        this.clazz = fromQuery.clazz;
        this.resultClass = toClass;
        this.queryParam = (QueryParam<T>) fromQuery.queryParam;
        this.dataSourceId = fromQuery.dataSourceId;
    }

    /**
     * 添加过滤条件
     * @param key 属性名
     * @param values 属性值数组
     * @return
     */
    public AggregateQuery<T, R> putIfNotNull(String key, Object... values) {
        if (values != null && values.length > 0 && values[0] != null) {
            this.queryParam.put(key, values);
        }
        return this;
    }

    /**
     * 添加过滤条件
     * @param conditions <key 属性名, value 属性值>
     * @return
     */
    public AggregateQuery<T, R> putAll(Map<String, Object> conditions) {
        this.queryParam.putAll(conditions);
        return this;
    }


    /**
     * 添加过滤条件
     * 多值使用sql的in(value1,value2...)
     * @param key 属性名
     * @param values 属性值数组
     * @return
     */
    public AggregateQuery<T, R> condition(String key, Object... values) {
        if (values != null && values.length == 1) {
            this.queryParam.put(key, values[0]);
        } else {
            this.queryParam.put(key, values);
        }
        return this;
    }

    /**
     * 添加过滤条件
     * 集合使用sql的in(value1,value2...)
     * @param key 属性名
     * @param values 属性值数组
     * @return
     */
    public AggregateQuery<T, R> condition(String key, Collection<?> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            this.queryParam.put(key, values);
        }
        return this;
    }

    /**
     * 根据Example过滤
     * 只能使用简单值,暂时无法通过数组或集合类型属性过滤
     * @param example T
     * @return
     */
    public AggregateQuery<T, R> example(Serializable example) {
        this.queryParam.example(example);
        return this;
    }

    /**
     * 添加正排序
     * @param key key(属性名)
     * @return
     */
    public AggregateQuery<T, R> asc(String key) {
        this.queryParam.asc(key);
        return this;
    }

    /**
     * 添加倒排序
     * @param key 分组key(属性名)
     * @return
     */
    public AggregateQuery<T, R> desc(String key) {
        this.queryParam.desc(key);
        return this;
    }

    /**
     * 添加范围查询
     * @param key 属性名
     * @param start 起始值
     * @param end 结束值
     * @return
     */
    public AggregateQuery<T, R> range(String key, Object start, Object end) {
        this.queryParam.range(key, start, end);
        return this;
    }

    /**
     * 添加范围查询
     * @param key 属性名
     * @param start 起始值
     * @param include 是否包含
     * @return
     */
    public AggregateQuery<T, R> rangeStart(String key, Object start, boolean include) {
        this.queryParam.rangeStart(key, start, include);
        return this;
    }

    /**
     * 添加范围查询
     * @param key 属性名
     * @param end 起始值
     * @param include 是否包含
     * @return
     */
    public AggregateQuery<T, R> rangeEnd(String key, Object end, boolean include) {
        this.queryParam.rangeEnd(key, end, include);
        return this;
    }

    /**
     * 统计数量
     * count(*)或count(id)聚合之后, 返回的key为countId
     * @param key 属性名
     * @return
     */
    public AggregateQuery<T, R> count(String... key) {
        this.queryParam.count(key);
        return this;
    }

    /**
     * 求和
     * sum(num)聚合之后, 返回的key为sumNum
     * @param key 属性名
     * @return
     */
    public AggregateQuery<T, R> sum(String... key) {
        this.queryParam.sum(key);
        return this;
    }

    /**
     * 分页查询
     * @param pageNo 页码
     * @return
     */
    public AggregateQuery<T, R> page(int pageNo) {
        this.queryParam.page(pageNo);
        return this;
    }

    /**
     * 分页大小
     * @param size 分页大小
     * @return
     */
    public AggregateQuery<T, R> size(int size) {
        this.queryParam.size(size);
        return this;
    }

    /**
     * 分页查询
     * @param rowStart 起始位置
     * @return
     */
    public AggregateQuery<T, R> rowStart(int rowStart) {
        this.queryParam.rowStart(rowStart);
        return this;
    }

    /**
     * 模糊查询
     * @param key 属性名
     * @param value 属性值
     * @return
     */
    public AggregateQuery<T, R> like(String key, Serializable value) {
        this.queryParam.like(key, value);
        return this;
    }

    /**
     * 分组查询
     * @param key 分组key(属性名)
     * @return
     */
    public AggregateQuery<T, R> groupBy(String key) {
        this.queryParam.groupBy(key);
        return this;
    }


    /**
     * JOIN查询
     * @param clazz 查询的目标类型
     * @param column 关联属性
     * @return
     */
    public <S extends Serializable, R extends Serializable> AggregateQuery<S, R> join(Class<S> clazz, String column) {
        // TODO
        return null;
    }

    /**
     * 转换成指定的类型(使用指定类型接收结果)
     * @param aliasClass 别的类型(可以使非DO类型)
     * @param <A>
     * @return
     */
    public <A extends Serializable, R extends Serializable> AggregateQuery<A, R> alias(Class<A> aliasClass) {
        // TODO
        return null;
    }

    /**
     * 执行查询
     * @return
     */
    public AggregateResult<R> execute() {
        return new AggregateResult<R>(this.resultClass, new ClientQueryAction() {
            @Override
            public ResponseResult<Map> doQuery() {
                return ServiceFactory.getInstance()
                    .getMetaDataReadService().query(AggregateQuery.this);
            }
        });
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.AggQuery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof AggregateQuery)) { return false; }
        if (!super.equals(o)) { return false; }

        AggregateQuery<?, ?> that = (AggregateQuery<?, ?>)o;

        return resultClass != null ? resultClass.equals(that.resultClass) : that.resultClass == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (resultClass != null ? resultClass.hashCode() : 0);
        return result;
    }
}
