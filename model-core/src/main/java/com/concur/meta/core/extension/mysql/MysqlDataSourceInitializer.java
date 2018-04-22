package com.concur.meta.core.extension.mysql;

import java.util.Map;

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
import org.springframework.util.MethodInvoker;

/**
 * Mysql初始化器
 *
 * @author yongfu.cyf
 * @create 2017-08-25 上午11:43
 **/
public class MysqlDataSourceInitializer implements DataSourceInitializer {

    private static Logger logger = LoggerFactory.getLogger(MysqlDataSourceInitializer.class);

    private static final String ATTR_CLASS = "driverClass";
    private static final String ATTR_CONFIG = "config";
    private static final String ATTR_INIT = "init";

    @Override
    public DataSource init(Map<String, Object> attributes) {
        String dataSourceClass = (String) attributes.get(ATTR_CLASS);
        String dataSourceConfig = (String) attributes.get(ATTR_CONFIG);
        String dataSourceInit = (String) attributes.get(ATTR_INIT);

        try {
            logger.info("开始初始化数据源:" + JSON.toJSONString(attributes));
            Class<?> driveClass = Class.forName(dataSourceClass);
            DataSource dataSource = (DataSource) driveClass.newInstance();
            PropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(dataSource);

            if (StringUtils.isNotBlank(dataSourceConfig)) {
                Map<String, String> properties = ExtraUtil.fromString(dataSourceConfig);
                accessor.setPropertyValues(properties);
            }

            if (StringUtils.isNotBlank(dataSourceInit)) {
                MethodInvoker initMethodInvoker = new MethodInvoker();
                initMethodInvoker.setTargetClass(driveClass);
                initMethodInvoker.setTargetMethod(dataSourceInit);
                initMethodInvoker.setTargetObject(dataSource);
                initMethodInvoker.prepare();
                initMethodInvoker.invoke();
            }

            logger.info("dataSourceClass={}", new Object[] { dataSourceClass});
            return dataSource;
        } catch (Exception e) {
            throw new ExecuteException(ServerResultCode.DATA_SOURCE_INIT_FAILT, e);
        }
    }

    @Override
    public DataSourceType getDataSourceType() {
        return DataSourceType.MYSQL;
    }
}
