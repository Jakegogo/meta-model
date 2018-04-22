package com.concur.meta.client.api.query;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.concur.meta.client.dataobject.ResponseResult;
import com.concur.meta.client.result.ClientResultCode;
import com.concur.meta.client.service.ServiceFactory;
import com.concur.meta.client.annotation.parser.LModelAnnoParser;
import com.concur.meta.client.api.result.QueryResult;
import com.concur.meta.client.constants.QueryType;
import com.concur.meta.client.exception.CheckFailException;
import org.apache.commons.collections.CollectionUtils;

/**
 * 查询对象API
 *
 * @author yongfu.cyf
 * @create 2017-06-29 下午4:36
 **/
public class Query<T extends Serializable> extends AbstractQuery<T> {

    private static final long serialVersionUID = 1174987136593994725L;

    Query() {
    }

    Query(Query<?> fromQuery, Class<T> toClass) {
        this.clazz = toClass;
        this.queryParam = (QueryParam<T>) fromQuery.queryParam;
        this.dataSourceId = fromQuery.dataSourceId;
    }

    /**
     * 根据类型创建
     * @param clazz Class<T>
     * @param <T>
     * @return
     */
    public static <T extends Serializable> Query<T> create(Class<T> clazz) {
        Query<T> query = new Query<T>();
        query.clazz = clazz;
        QueryParam queryParam = new QueryParam();
        query.queryParam = queryParam;
        return query;
    }

    /**
     * 根据code创建 TODO
     * @param code String 模型编码
     * @param <T>
     * @return
     */
    public static <T extends Serializable> Query<T> create(String code) {
        Query<T> query = new Query<T>();
        query.code = code;
        query.queryParam = new QueryParam();
        return query;
    }

    /**
     * 根据example创建
     * @param example T
     * @param <T>
     * @return
     */
    public static <T extends Serializable> Query<T> create(T example) {
        if (example.getClass() == Class.class) {
            throw new CheckFailException(ClientResultCode.ENTITY_CLASS_MUST_IMPLEMENTS_SERIALIZABLE);
        }

        Query<T> query = new Query<T>();
        query.clazz = (Class<T>) example.getClass();
        QueryParam queryParam = new QueryParam();
        queryParam.example(example);
        queryParam.setAnnotationInfo(LModelAnnoParser.parse(example.getClass()));
        query.queryParam = queryParam;
        return query;
    }

    /**
     * 指定数据源ID
     * @param dataSourceId Long
     * @return
     */
    public Query<T> dataSource(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
        return this;
    }


    /**
     * 根据主键获取
     * @param id 主键
     * @return
     */
    public Query<T> get(Serializable id) {
        this.queryParam.setPrimaryKey(id);
        return this;
    }


    /**
     * 执行查询
     * @return
     */
    public QueryResult<T> execute() {
        return new QueryResult<T>(this.clazz, new ClientQueryAction() {
            @Override
            public ResponseResult<Map> doQuery() {
                Serializable id = queryParam.getPrimaryKey();
                if (id != null) {
                    return ServiceFactory.getInstance()
                        .getMetaDataReadService().get(dataSourceId, clazz, id);
                }
                return ServiceFactory.getInstance()
                    .getMetaDataReadService().query(Query.this);
            }
        });
    }


    /**
     * 添加过滤条件
     * 相同key重复设置会覆盖
     * @param key 属性名
     * @param values 属性值数组
     * @return
     */
    public Query<T> putIfNotNull(String key, Object... values) {
        if (values != null && values.length > 0 && values[0] != null) {
            this.queryParam.put(key, values);
        }
        return this;
    }

    /**
     * 添加过滤条件
     * 相同key重复设置会覆盖
     * @param conditions <key 属性名, value 属性值>
     * @return
     */
    public Query<T> putAll(Map<String, Object> conditions) {
        this.queryParam.putAll(conditions);
        return this;
    }


    /**
     * 添加过滤条件
     * 多值使用sql的in(value1,value2...)语句
     * 相同key重复设置会覆盖
     * @param key 属性名
     * @param values 属性值数组
     * @return
     */
    public Query<T> condition(String key, Object... values) {
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
     * 相同key重复设置会覆盖
     * @param key 属性名
     * @param values 属性值数组
     * @return
     */
    public Query<T> condition(String key, Collection<?> values) {
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
    public Query<T> example(Serializable example) {
        this.queryParam.example(example);
        return this;
    }

    /**
     * 添加正排序
     * @param key 属性名
     * @return
     */
    public Query<T> asc(String key) {
        this.queryParam.asc(key);
        return this;
    }

    /**
     * 添加倒排序
     * @param key 属性名
     * @return
     */
    public Query<T> desc(String key) {
        this.queryParam.desc(key);
        return this;
    }

    /**
     * 添加范围查询
     * 相同key重复设置会覆盖
     * @param key 属性名
     * @param start 起始值
     * @param end 结束值
     * @return
     */
    public Query<T> range(String key, Object start, Object end) {
        this.queryParam.range(key, start, end);
        return this;
    }

    /**
     * 添加范围查询
     * 相同key重复设置会覆盖
     * @param key 属性名
     * @param start 起始值
     * @param include 是否包含
     * @return
     */
    public Query<T> rangeStart(String key, Object start, boolean include) {
        this.queryParam.rangeStart(key, start, include);
        return this;
    }

    /**
     * 添加范围查询
     * 相同key重复设置会覆盖
     * @param key 属性名
     * @param end 起始值
     * @param include 是否包含
     * @return
     */
    public Query<T> rangeEnd(String key, Object end, boolean include) {
        this.queryParam.rangeEnd(key, end, include);
        return this;
    }

    /**
     * 统计数量
     * count(*)或count(id)聚合之后, 返回的key为countId
     * @param key 属性名
     * @return
     */
    public AggregateQuery<T, Integer> count(String... key) {
        this.queryParam.count(key);
        return new AggregateQuery<T, Integer>(this, Integer.class);
    }

    /**
     * 求和
     * sum(num)聚合之后, 返回的key为sumNum
     * @param key 属性名
     * @return
     */
    public AggregateQuery<T, Integer> sum(String... key) {
        this.queryParam.sum(key);
        return new AggregateQuery<T, Integer>(this, Integer.class);
    }

    /**
     * 分页查询
     * @param pageNo 页码
     * @return
     */
    public Query<T> page(int pageNo) {
        this.queryParam.page(pageNo);
        return this;
    }

    /**
     * 分页大小
     * @param size 分页大小
     * @return
     */
    public Query<T> size(int size) {
        this.queryParam.size(size);
        return this;
    }

    /**
     * 分页查询
     * @param rowStart 起始位置
     * @return
     */
    public Query<T> rowStart(int rowStart) {
        this.queryParam.rowStart(rowStart);
        return this;
    }

    /**
     * 模糊查询
     * @param key 属性名
     * @param value 属性值
     * @return
     */
    public Query<T> like(String key, Serializable value) {
        this.queryParam.like(key, value);
        return this;
    }

    /**
     * 根据SQL查询
     * sql占位符用mybatis的#{key}, key对应params的key
     * @param sql sql
     * @return
     */
    public SqlQuery<T> listBySql(String sql) {
        return new SqlQuery(this, sql);
    }

    /**
     * JOIN查询
     * @param clazz 查询的目标类型
     * @param column 关联属性
     * @return
     */
    public <S extends Serializable> Query<S> join(Class<S> clazz, String column) {
        // TODO
        return null;
    }

    /**
     * 转换成指定的类型(使用指定类型接收结果)
     * @param aliasClass 别的类型(可以使非DO类型)
     * @param <A>
     * @return
     */
    public <A extends Serializable> Query<A> alias(Class<A> aliasClass) {
        // TODO
        return null;
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.PageQuery;
    }

}
