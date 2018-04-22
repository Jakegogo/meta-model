package com.concur.meta.client.constants;

/**
 * 参数传递KEY
 *
 * @author yongfu.cyf
 * @create 2017-06-28 下午4:15
 **/
public interface ParamKeys {

    /**
     * DO的类名
     */
    String DO_CLASS_NAME = "DO_CLASS_NAME";

    /**
     * DO的值
     */
    String DO_PROPERTIES = "DO_PROPERTIES";

    /**
     * DO的主键
     */
    String DO_PRIMARY_KEY = "DO_PRIMARY_KEY";

    /**
     * 查询参数
     */
    String QUERY_PARAM = "QUERY_PARAM";

    /**
     * 分页查询参数
     */
    String PAGE_QUERY_PARAM = "PAGE_QUERY_PARAM";
    /**
     * LModel的表名
     */
    String LMODEL_TABLE_NAME = "LMODEL_TABLE_NAME";

    /**
     * LModel的默认的数据源
     */
    String LMODEL_DEFAULT_DATASOURCE = "LMODEL_DEFAULT_DATASOURCE";

    /**
     * 批量参数
     */
    String BATCH_INSTANCE_LIST = "BATCH_INSTANCE_LIST";

    /**
     * 组合请求
     */
    String CONBINE_ACTIONS = "CONBINE_ACTIONS";
    /**
     * 自动回滚
     */
    String AUTO_ROLLBACK = "AUTO_ROLLBACK";
    /**
     * 数据幂等性
     */
    String DATA_CONSISTENCY = "DATA_CONSISTENCY";
    /**
     * 查询类型
     */
    String QUERY_TYPE = "QUERY_TYPE";
    /**
     * 乐观锁属性名
     */
    String MODEL_UPDATE_CAS_VERSION_FIELD = "MODEL_UPDATE_CAS_VERSION_FIELD";
    /**
     * 乐观锁版本号
     */
    String MODEL_UPDATE_CAS_VERSION = "MODEL_UPDATE_CAS_VERSION";
}
