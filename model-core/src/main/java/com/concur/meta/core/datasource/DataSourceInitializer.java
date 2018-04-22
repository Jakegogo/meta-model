package com.concur.meta.core.datasource;

import java.util.Map;

import javax.sql.DataSource;

import com.concur.meta.client.constants.DataSourceType;

/**
 * 数据源初始化器
 *
 * @author yongfu.cyf
 * @create 2017-08-25 上午11:41
 **/
public interface DataSourceInitializer {

    /**
     * 初始化数据源
     * @param attributes 属性
     * @return
     */
    DataSource init(Map<String, Object> attributes);

    /**
     * 声明数据源类型
     * @return
     */
    DataSourceType getDataSourceType();
}
