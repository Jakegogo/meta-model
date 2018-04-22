package com.concur.meta.client.api.persist;

import java.io.Serializable;
import java.util.Collection;

import com.concur.meta.client.api.persist.actions.BatchInsertAction;
import com.concur.meta.client.api.persist.actions.DeleteAction;
import com.concur.meta.client.api.persist.actions.InsertAction;
import com.concur.meta.client.api.persist.actions.UpdateAction;
import com.concur.meta.client.api.transaction.Transaction;
import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.service.ServiceFactory;
import com.concur.meta.client.api.persist.actions.operation.AbstractOperation;
import org.apache.commons.collections.CollectionUtils;

/**
 * 持久化对象API
 * @see Transaction
 * @author yongfu.cyf
 * @create 2017-07-03 下午3:00
 **/
public class Persist<T extends Serializable> {

    /**
     * 持久化对象的类
     */
    private Class<T> clazz;
    /**
     * 数据源ID
     */
    private Long dataSourceId;

    static {
        ServiceFactory.getInstance()
            .getMetaDataWriteServerService();
    }

    public static <T extends Serializable> Persist<T> create() {
        Persist<T> persist = new Persist<T>();
        return persist;
    }

    public static <T extends Serializable> Persist<T> create(Class<T> clazz) {
        Persist<T> persist = new Persist<T>();
        persist.clazz = clazz;
        return persist;
    }

    /**
     * 指定数据源ID
     * @param dataSourceId Long
     * @return
     */
    public Persist<T> dataSource(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
        return this;
    }

    /**
     * 更新数据
     * 覆盖更新:如果字段值为NULL,会将NULL值更新到DB
     * @param instance 数据实体
     * @return 使用了事务则立即返回-1
     */
    public PersistAction update(T instance) {
        UpdateAction action = new UpdateAction<T>(instance);
        action.setDataSourceId(dataSourceId);
        Transaction.execute(action);
        return action;
    }

    /**
     * (乐观锁)更新数据;
     * <li/>覆盖更新:如果字段值为NULL,会将NULL值更新到DB;
     * <li/>如果乐观锁更新失败希望回滚, 可以设置Transaction#setDataConsistency(true),
     *      并且显式catch ConsistencyException并调用rollback
     * @param instance 数据实体
     * @param lastVersion 上一个版本号
     * @return 使用了事务则立即返回-1
     */
    public PersistAction updateWithCAS(T instance, Serializable lastVersion) {
        UpdateAction action = new UpdateAction<T>(instance);
        action.setDataSourceId(dataSourceId);
        action.withCAS(lastVersion);
        Transaction.execute(action);
        return action;
    }


    /**
     * 部分字段更新
     * 只更新不为NULL的字段
     * @param instance 数据实体
     * @return 使用了事务则立即返回-1
     */
    public PersistAction updateSelective(T instance) {
        UpdateAction action = new UpdateAction<T>(instance, true);
        action.setDataSourceId(dataSourceId);
        Transaction.execute(action);
        return action;
    }

    /**
     * (乐观锁)部分字段更新
     * <li/>只更新不为NULL的字段
     * <li/>如果乐观锁更新失败希望回滚, 可以设置Transaction#setDataConsistency(true),
     *      并且显式catch ConsistencyException并调用rollback
     * @param instance 数据实体
     * @param lastVersion 上一个版本号
     * @return 使用了事务则立即返回-1
     */
    public PersistAction updateSelectiveWithCAS(T instance, Serializable lastVersion) {
        UpdateAction action = new UpdateAction<T>(instance, true);
        action.setDataSourceId(dataSourceId);
        action.withCAS(lastVersion);
        Transaction.execute(action);
        return action;
    }

    /**
     * 添加数据
     * 将立即调用写入操作并返回自增主键值
     * 若不传主键则默认使用自增主键, 传递则使用给定的主键
     * @param instance 数据实体
     * @return 写入之后的实体,包含主键值; 若添加失败则返回null
     */
    public PersistAction<T> insert(T instance) {
        InsertAction<T> action = new InsertAction<T>(instance);
        action.setDataSourceId(dataSourceId);
        Transaction.execute(action);
        return action;
    }

    /**
     * 添加数据
     * @param instances 数据实体
     * @return 使用了事务则立即返回-1
     */
    public PersistAction insert(Collection<?> instances) {
        if (CollectionUtils.isEmpty(instances)) {
            return null;
        }
        BatchInsertAction action = new BatchInsertAction(instances);
        action.setDataSourceId(dataSourceId);
        Transaction.execute(action);
        return action;
    }

    /**
     * 删除实体
     * <li>在事务中, 删除操作不做一致性校验(目的仅为了删除)</li>
     * <li>并且因需要支持重试幂等的特性,不支持通过删除结果来判断是否为本次的删除的</li>
     * <li>如果需要判断是否为本次删除的场景, 请使用update标记的方式实现逻辑删除</li>
     * @param id 主键值
     * @return 使用了事务则立即返回-1
     */
    public PersistAction deleteById(Serializable id) {
        DeleteAction action = new DeleteAction(this.clazz, id);
        action.setDataSourceId(dataSourceId);
        Transaction.execute(action);
        return action;
    }

    /**
     * 删除实体
     * <li>在事务中, 删除操作不做一致性校验(目的仅为了删除)</li>
     * <li>并且因需要支持重试幂等的特性,不支持通过删除结果来判断是否为本次的删除的</li>
     * <li>如果需要判断是否为本次删除的场景, 请使用update标记的方式实现逻辑删除</li>
     * @param ids 主键值集合
     * @return 使用了事务则立即返回-1
     */
    public PersistAction deleteByIds(Collection<?> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        DeleteAction action = new DeleteAction(this.clazz, ids);
        action.setDataSourceId(dataSourceId);
        Transaction.execute(action);
        return action;
    }

    /**
     * 删除实体
     * <li>在事务中, 删除操作不做一致性校验(目的仅为了删除)</li>
     * <li>并且因需要支持重试幂等的特性,不支持通过删除结果来判断是否为本次的删除的</li>
     * <li>如果需要判断是否为本次删除的场景, 请使用update标记的方式实现逻辑删除</li>
     * @param instance 数据实体
     * @return 使用了事务则立即返回-1
     */
    public PersistAction delete(Serializable instance) {
        DeleteAction action = new DeleteAction(instance);
        action.setDataSourceId(dataSourceId);
        Transaction.execute(action);
        return action;
    }

    /**
     * 本次操作立即执行入库, 不纳入事务管理
     * <pre>
     *     Persist.executeImmediately(new UpdateAction(lmodelDO) {
     *           \\@Override
     *           public void execute() {
     *              lmodel0.put("status", 0);
     *              super.execute();
     *           }
     *
     *           \\@Override
     *           protected void undo() {
     *              lmodel0.put("status", 1);
     *              super.execute();
     *           }
     *       });
     * </pre>
     * @param persistAction
     * @return
     */
    public static PersistAction executeImmediately(final PersistAction persistAction) {
        persistAction.setExecuteImmediately(true);
        persistAction.setUndoAction(new PersistAction() {
            @Override
            public boolean isSuccess() {
                return persistAction.getActionStatus() == ActionStatus.UNDO;
            }

            @Override
            public AbstractOperation initOperation() {
                return null;
            }

            @Override
            public void execute() {
                persistAction.undo();
            }

            @Override
            public void resolveResult(MetaResponse metaResponse) {
                // do nothing
            }
        });
        Transaction.execute(persistAction);
        return persistAction;
    }

    public Long getDataSourceId() {
        return dataSourceId;
    }
}
