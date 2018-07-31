package com.concur.meta.client.api.persist.actions.operation;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.concur.meta.client.api.persist.ActionStatus;
import com.concur.meta.client.constants.DataSourceType;
import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.domain.dto.DataSourceDTO;
import com.concur.meta.client.result.ClientResultCode;
import com.concur.meta.client.service.server.MetaDataWriteServerService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.concur.meta.client.exception.ExecuteException;
import com.concur.meta.client.service.DataSourceService;
import com.concur.meta.client.utils.CacheUtils;

/**
 * 抽象写入原子操作(传输到服务端执行)
 * <li>具体的写入动作实现(参数包装, 调用数据源写入层指令, 状态维护, 回滚)</li>
 * <li>可传输到后端执行, 可回滚</li>
 *
 * @author yongfu.cyf
 * @create 2017-09-22 上午12:13
 **/
public abstract class AbstractOperation implements Serializable {

    /**
     * 元数据写服务
     */
    private static MetaDataWriteServerService metaDataWriteServerService;
    /**
     * 数据源服务
     */
    protected static DataSourceService dataSourceService;
    /**
     * 数据源缓存
     */
    protected final LoadingCache<Long, DataSourceDTO> DATASOUR_CECACHE = CacheBuilder.newBuilder()
        .concurrencyLevel(4)
        .maximumSize(1000)
        .refreshAfterWrite(5, TimeUnit.MINUTES)
        .build(
            new CacheUtils.AsynchronousCacheLoader<Long, DataSourceDTO>() {
                @Override
                public DataSourceDTO load(Long key) {
                    DataSourceDTO dto = dataSourceService.getDataSource(key);
                    if (dto != null) {
                        return dto;
                    }
                    return DataSourceDTO.NULL_DATASOURCE;
                }
            });
    /**
     * 当前操作的数据源ID
     */
    private Long dataSourceId;

    /**
     * 执行状态
     */
    protected ActionStatus actionStatus = ActionStatus.INIT;

    /**
     * 回滚操作
     */
    protected AbstractOperation undoOperation;
    /**
     * action自增ID
     */
    private long actionId;

    public AbstractOperation(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    /**
     * 获取数据源类型
     * @param dataSourceId 数据源ID
     * @return
     */
    private DataSourceType getDataSourceType(Long dataSourceId) {
        if (dataSourceId == null) {
            return DataSourceType.NULL;
        }
        DataSourceDTO dataSourceDTO;
        try {
            dataSourceDTO = DATASOUR_CECACHE.get(dataSourceId);
        } catch (ExecutionException e) {
            throw new ExecuteException(ClientResultCode.DATA_SOUCE_GET_ERROR);
        }
        if (dataSourceDTO == null || DataSourceDTO.NULL_DATASOURCE == dataSourceDTO) {
            return DataSourceType.NULL;
        }
        return DataSourceType.getByName(dataSourceDTO.getType());
    }

    protected MetaDataWriteServerService getWriteServerService() {
        return metaDataWriteServerService;
    }

    /**
     * 获取数据源类型
     * @return
     */
    DataSourceType getDataSourceType() {
        return this.getDataSourceType(this.dataSourceId);
    }

    /**
     * Action执行内容抽象方法
     * @return
     */
    public abstract AbstractOperation operate();

    /**
     * 获取执行结果
     * @return
     */
    public abstract MetaResponse getResponse();

    /**
     * 判断是否执行成功
     * @return
     */
    public abstract boolean isSuccess();

    /**
     * 包含状态更改的执行
     */
    public void operateWithStatus() {
        // 未执行成功的调用执行
        if (this.actionStatus != ActionStatus.OK) {
            this.operate();
            this.actionStatus = ActionStatus.OK;
        }
    }

    /**
     * 包含状态更改的撤销
     */
    public void undoWithStatus() {
        // 执行过了才需要撤销
        if (this.actionStatus != ActionStatus.UNDO) {
            this.undo();
        }
        // 未执行动作或已经回滚的直接置为回滚状态
        this.actionStatus = ActionStatus.UNDO;
    }

    /**
     * 撤销:撤销钩子动作,用于事务失败后的逆向补偿, insert的模式undoAction默认使用DeleteAction实例
     */
    public void undo() {
        if (this.undoOperation != null) {
            this.undoOperation.operateWithStatus();
        }
    }

    public ActionStatus getActionStatus() {
        return actionStatus;
    }

    public AbstractOperation setActionStatus(ActionStatus actionStatus) {
        this.actionStatus = actionStatus;
        return this;
    }

    public Long getDataSourceId() {
        return dataSourceId;
    }

    public AbstractOperation getUndoOperation() {
        return undoOperation;
    }

    // 使用前需要设置的依赖服务

    /**
     * 设置数据写服务
     * @param metaDataWriteServerService
     */
    public static void setMetaDataWriteServerService(MetaDataWriteServerService metaDataWriteServerService) {
        AbstractOperation.metaDataWriteServerService = metaDataWriteServerService;
    }

    /**
     * 设置数据源服务
     * @param dataSourceService
     */
    public static void setDataSourceService(DataSourceService dataSourceService) {
        AbstractOperation.dataSourceService = dataSourceService;
    }

    /**
     * 获取信息信息, 用于日志输出
     * @return
     */
    public abstract String toDetailMessage();

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    /**
     * 是否需要检测一致性
     * @return
     */
    public boolean needCheckConsistency() {
        return true;
    }
}
