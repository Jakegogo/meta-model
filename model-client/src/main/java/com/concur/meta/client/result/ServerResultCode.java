package com.concur.meta.client.result;

import com.concur.meta.client.common.IResultCode;

/**
 * server端错误码
 *
 * @author yongfu.cyf
 * @create 2017-06-29 下午3:32
 **/
public enum ServerResultCode implements IResultCode {

    MATA_CLASS_NAME_IS_BLANK("meta_request_classname_is_blank", "请求的类名不能为空"),

    GET_TABLE_META_EXCEPTION("get_table_meta_exception", "获取数据表的元数据信息异常"),

    PARAM_CONVERT_ERROR("param_convert_error", "参数转换错误"),

    CONBINE_INVOKE_EXCEPTION("conbine_invoke_exception", "事务提交失败"),

    ROLLBACK_EXCEPTION("rollback_exception", "回滚失败"),

    COLUMN_MAPPING_NOT_FOUND("column_map_not_found", "字段未找到"),

    DATA_SOURCE_NOT_EXISTS("data_source_not_exists", "数据源不存在"),

    DATA_SOURCE_INIT_FAILT("data_source_init_fail", "数据源初始化失败"),

    SERVER_DATA_CONSISTENCY_EXCEPTION("server_data_consistency_exception", "数据不一致异常"),

    DATA_SOURCE_TYPE_NOT_EXISTS("data_source_type_not_exists", "数据源类型暂不支持"),

    DATA_SOURCE_TYPE_NOT_SUPPORT_FOR_THE_OPERATION("data_source_type_not_support_for_the_operation", "该数据源类型暂不支持此操作"),

    SERVER_DATA_SOUCE_GET_ERROR("server_data_souce_get_error", "数据源获取异常"),

    OPERATION_PARAM_MISSION("operation_param_mission", "不支持此类型的操作"),

    ;

    ServerResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;
    private String msg;

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }

}
