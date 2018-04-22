package com.concur.meta.client.result;

import com.concur.meta.client.common.IResultCode;

/**
 * client端错误码
 *
 * @author yongfu.cyf
 * @create 2017-06-29 下午3:22
 **/
public enum ClientResultCode implements IResultCode {

    DATA_CONVERT_EXCEPTION("client_data_convert_exception", "数据转换异常"),

    CLASS_CANOT_BE_NULL("client_entity class is null", "实体类不能为空"),

    DATA_SOUCE_GET_ERROR("client_data_source_get_exception", "数据源获取异常"),

    DATA_SOURCE_NOT_EXISTS("client_data_source_not_exists", "数据源不存在"),

    DATA_CONSISTENCY_EXCEPTION("client_data_consistency_exception", "数据不一致异常"),

    TRANSACTION_FAIL_EXCEPTION("client_transaction_fail_exception", "事务执行失败异常"),

    ENTITY_CLASS_MUST_IMPLEMENTS_SERIALIZABLE("client_entity_class_must_implements_serializable",
        "实体类型必须实现Serializable接口"),

    TRANSACTION_NOT_COMMIT_YET("transaction_not_commit_yet", "事务未提交不可回滚"),

    RESPONSE_RESOLVE_ERROR("write_response_resolve_error", "执行结果解析异常,数据可能已经写入"),
    ;

    ClientResultCode(String code, String msg) {
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
