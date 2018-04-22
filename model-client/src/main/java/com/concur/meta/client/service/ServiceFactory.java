package com.concur.meta.client.service;

import com.concur.meta.client.service.impl.MetaDataReadServiceImpl;
import com.concur.meta.client.service.server.MetaDataReadServerService;
import com.concur.meta.client.service.server.MetaDataWriteServerService;
import com.concur.meta.client.api.persist.actions.operation.AbstractOperation;
import com.concur.meta.client.service.impl.TairDataServiceImpl;

/**
 * 服务工厂
 * 如果需要独立的server, 请建立WebServer工程,
 * 并且配置RPC调用
 * 建议应用配置spring bean, 在启动时候初始化HSF订阅
 * @author yongfu.cyf
 * @create 2017-09-22 下午4:51
 **/
public class ServiceFactory {

    /**
     * 单例
     */
    private static ServiceFactory instance = new ServiceFactory();
    /**
     * 元数据读服务
     */
    protected MetaDataReadServerService metaDataReadServerService;
    /**
     * 元数据写服务
     */
    protected MetaDataWriteServerService metaDataWriteServerService;
    /**
     * 数据源服务
     */
    protected DataSourceService dataSourceService;

//    /**
//     * Hsf lightapi服务工厂
//     */
//    public final com.taobao.hsf.lightapi.ServiceFactory FACTORY = com.taobao.hsf.lightapi.ServiceFactory.getInstance();


    /**
     * 元数据读服务
     */
    protected final MetaDataReadService metaDataReadService = new MetaDataReadServiceImpl();

    /**
     * Tair读写服务
     */
    private TairDataService tairDataService = new TairDataServiceImpl();

    private ServiceFactory(){
        initServices();
    }

    /**
     * 获取单例
     * @return
     */
    public static ServiceFactory getInstance() {
        return instance;
    }

    /**
     * 初始化HSF订阅服务
     */
    public void initServices() {
        /**
         * 元数据驱动引擎之 数据服务
         * 如果需要独立的server, 请建立WebServer工程,
         * 并且配置RPC调用:MetaDataReadServerService,MetaDataWriteServerService,DataSourceService
         */
//        FACTORY.consumer("metaDataWriteServerService")
//            .service("MetaDataWriteServerService")
//            .version("1.0.0")
//            .subscribe();
//        FACTORY.consumer("metaDataReadServerService")
//            .service("MetaDataReadServerService")
//            .version("1.0.0")
//            .subscribe();
//        FACTORY.consumer("dataSourceService")
//            .service("DataSourceService")
//            .version("1.0.0")
//            .subscribe();
//
//        // 睡眠5秒
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // 依赖注入
//        setMetaDataWriteServerService((MetaDataWriteServerService)
//            FACTORY.consumer("metaDataWriteServerService").subscribe());
//        setMetaDataReadServerService((MetaDataReadServerService)
//            FACTORY.consumer("metaDataReadServerService").subscribe());
//        setDataSourceService((DataSourceService)
//            FACTORY.consumer("dataSourceService").subscribe());
    }


    public MetaDataReadService getMetaDataReadService() {
        return this.metaDataReadService;
    }

    public DataSourceService getDataSourceService() {
        return this.dataSourceService;
    }

    public void setMetaDataWriteServerService(
        MetaDataWriteServerService metaDataWriteServerService) {
        this.metaDataWriteServerService = metaDataWriteServerService;
        AbstractOperation.setMetaDataWriteServerService(metaDataWriteServerService);
    }

    public void setMetaDataReadServerService(
        MetaDataReadServerService metaDataReadServerService) {
        this.metaDataReadServerService = metaDataReadServerService;
    }

    public void setDataSourceService(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
        AbstractOperation.setDataSourceService(dataSourceService);
    }

    public MetaDataWriteServerService getMetaDataWriteServerService() {
        return this.metaDataWriteServerService;
    }

    public MetaDataReadServerService getMetaDataReadServerService() {
        return this.metaDataReadServerService;
    }
}
