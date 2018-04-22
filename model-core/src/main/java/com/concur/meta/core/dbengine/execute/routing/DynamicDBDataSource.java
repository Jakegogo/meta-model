package com.concur.meta.core.dbengine.execute.routing;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.concur.meta.core.dbengine.execute.ExecuteContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * DB多数据源
 * @author yongfu.cyf
 * @create 2017-07-05 下午3:41
 **/
public class DynamicDBDataSource extends AbstractRoutingDataSource implements InitializingBean {

    /**
     * 默认数据源
     */
    @Autowired(required = true)
    @Qualifier("dataSource")
    private DataSource defaultDataSource;

    /**
     * 可选数据源映射
     */
    private Map<Object, Object> targetDataSource = new HashMap<Object, Object>();

    @Override
    protected Object determineCurrentLookupKey() {
        return ExecuteContext.getDataSource();
    }

    /**
     * 初始化数据源
     */
    public void initDataSources() {
        targetDataSource.put("default", defaultDataSource);
        this.setTargetDataSources(targetDataSource);
        this.setDefaultTargetDataSource(defaultDataSource);
    }

    public void setDefaultDataSource(DataSource defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

    /**
     * 添加数据源
     * @param key 数据源Key
     * @param dataSource DB数据源
     */
    public void addDataSource(String key, DataSource dataSource) {
        targetDataSource.put(key, dataSource);
        super.afterPropertiesSet();
    }

    @Override
    public void afterPropertiesSet() {
        this.initDataSources();
        super.afterPropertiesSet();
    }

    public DataSource getDefaultDataSource() {
        return defaultDataSource;
    }
}
