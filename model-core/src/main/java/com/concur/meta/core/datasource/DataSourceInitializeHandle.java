package com.concur.meta.core.datasource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.result.ServerResultCode;
import com.concur.meta.core.extension.mysql.MysqlDataSourceInitializer;
import com.concur.meta.core.extension.postgre.PostgresSqlDataSourceInitializer;

/**
 * 数据源初始化处理
 *
 * @author yongfu.cyf
 * @create 2017-08-25 上午11:57
 **/
public class DataSourceInitializeHandle {

    private static Map<DataSourceType, DataSourceInitializer> registry
                = new ConcurrentHashMap<DataSourceType, DataSourceInitializer>();

    /**
     * 获取数据源连接
     * @param dataSourceTypeName 数据源类型
     * @param attributes 属性
     * @return
     */
    public static DataSource getOrCreateDataSource(String dataSourceTypeName, Map<String, Object> attributes) {
        DataSourceType dataSourceType = DataSourceType.getByName(dataSourceTypeName);
        DataSourceInitializer dataSourceInitializer = registry.get(dataSourceType);
        if (dataSourceInitializer == null) {
            throw new ExecuteException(ServerResultCode.DATA_SOURCE_NOT_EXISTS, dataSourceTypeName);
        }
        return dataSourceInitializer.init(attributes);
    }

    /**
     * 注册数据源初始化器
     * @param dataSourceInitializer 数据源初始化器
     */
    public static void register(DataSourceInitializer dataSourceInitializer) {
        registry.put(dataSourceInitializer.getDataSourceType(), dataSourceInitializer);
    }

    static {
        // 注册
        register(new MysqlDataSourceInitializer());
        register(new PostgresSqlDataSourceInitializer());
    }

}
