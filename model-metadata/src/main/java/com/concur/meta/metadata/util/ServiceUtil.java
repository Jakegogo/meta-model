package com.concur.meta.metadata.util;

import javax.sql.DataSource;

import com.concur.meta.metadata.service.LMetaService;

/**
 * 服务工厂工具类
 * <li>lmodel-core 依赖lmodel-metadata的元数据服务,</li>
 * <li>lmodel-metadata可以使用lmodel-core的查询服务,但不需要指定datasourceId</li>
 * <li>问题:互相依赖会不会死循环? 答:不会。就好像递归一样, 只要末尾判断处理好就能得到计算结果</li>
 *
 * @author yongfu.cyf
 * @create 2017-07-03 下午4:26
 **/
public class ServiceUtil {

    /**
     * LModel配置数据库数据源
     */
    static DataSource lmodelConfigDataSource;
    /**
     * LMetaService
     */
    static LMetaService lMetaService;

    static {
        lmodelConfigDataSource = (DataSource) ApplicationContextUtils.getContext().getBean("dataSource");
        lMetaService = (LMetaService) ApplicationContextUtils.getContext().getBean("lMetaService");
    }

    public static DataSource getLmodelConfigDataSource() {
        return lmodelConfigDataSource;
    }

    public static LMetaService getlMetaService() {
        return lMetaService;
    }

}
