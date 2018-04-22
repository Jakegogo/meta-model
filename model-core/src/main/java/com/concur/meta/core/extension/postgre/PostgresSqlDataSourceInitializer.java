package com.concur.meta.core.extension.postgre;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import com.alibaba.fastjson.JSON;

import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.result.ServerResultCode;
import com.concur.meta.client.utils.ExtraUtil;
import com.concur.meta.core.datasource.DataSourceInitializer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

/**
 * Mysql初始化器
 *
 * @author yongfu.cyf
 * @create 2017-08-25 上午11:43
 **/
public class PostgresSqlDataSourceInitializer implements DataSourceInitializer {

    private static Logger logger = LoggerFactory.getLogger(PostgresSqlDataSourceInitializer.class);

    private static final String ATTR_CLASS = "driverClass";
    private static final String ATTR_CONFIG = "config";
    private static final String ATTR_PROPERTIES = "properties";

    @Override
    public DataSource init(Map<String, Object> attributes) {
        String dataSourceClass = (String) attributes.get(ATTR_CLASS);
        String dataSourceConfig = (String) attributes.get(ATTR_CONFIG);
        String properties = (String) attributes.get(ATTR_PROPERTIES);

        try {
            logger.info("开始初始化数据源:" + JSON.toJSONString(attributes));
            Class<?> driveClass = Class.forName(dataSourceClass);
            DataSource dataSource = (DataSource) driveClass.newInstance();
            PropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(dataSource);

            if (StringUtils.isNotBlank(dataSourceConfig)) {
                Map<String, String> configMap = ExtraUtil.fromString(dataSourceConfig);
                accessor.setPropertyValues(configMap);
            }

            if (StringUtils.isNotBlank(properties)) {
                Map<String, String> propertyMap = ExtraUtil.fromString(properties);
                Method setPropertyMethod = driveClass.getMethod("setProperty", String.class, String.class);
                for (Entry<String, String> entry : propertyMap.entrySet()) {
                    setPropertyMethod.invoke(dataSource, entry.getKey(), entry.getValue());
                }
            }

            logger.info("dataSourceClass={}", new Object[] { dataSourceClass});
            return dataSource;
        } catch (Exception e) {
            throw new ExecuteException(ServerResultCode.DATA_SOURCE_INIT_FAILT, e);
        }
    }

    @Override
    public DataSourceType getDataSourceType() {
        return DataSourceType.POSTGRESQL;
    }
}
