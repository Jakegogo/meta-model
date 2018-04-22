package com.concur.meta.client.dataobject;

import java.io.Serializable;
import java.util.List;

import com.concur.meta.client.common.ResultDO;

/**
 * 元数据存取服务返回结果
 *
 * @author yongfu.cyf
 * @create 2017-06-28 下午4:25
 **/
public class MetaResponse extends ResultDO<Object> implements Serializable {

    private static final long serialVersionUID = -4986672710117496096L;

    /**
     * 执行日志
     */
    private List<String> executeLog;

    /**
     * 获取执行日志
     * @return
     */
    public List<String> getExecuteLog() {
        return executeLog;
    }

    public void setExecuteLog(List<String> executeLog) {
        this.executeLog = executeLog;
    }
}
