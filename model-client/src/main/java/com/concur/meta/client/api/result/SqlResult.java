package com.concur.meta.client.api.result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.concur.meta.client.api.query.ClientQueryAction;
import com.concur.meta.client.result.ClientResultCode;
import com.concur.meta.client.utils.ConvertUtils;
import com.concur.meta.client.exception.ExecuteException;
import org.apache.commons.collections.CollectionUtils;

/**
 * Sql查询结果
 *
 * @author yongfu.cyf
 * @create 2017-08-01 上午9:51
 **/
public class SqlResult<T> extends BaseResult<T> {

    private static final long serialVersionUID = -487878648235332275L;
    /**
     * list结果
     */
    protected List<T> list;

    public SqlResult(Class<T> clazz, ClientQueryAction queryAction) {
        this.clazz = clazz;
        this.queryAction = queryAction;
    }

    public List<T> getList() {
        return resolveResult().list;
    }

    /**
     * 获取一个实体对象
     * @return
     */
    @Override
    public T getOne() {
        if (resolveResult().one != null) {
            return resolveResult().one;
        }
        if (CollectionUtils.isEmpty(resolveResult().getList())) {
            return null;
        }
        return resolveResult().getList().get(0);
    }

    @Override
    protected String buildCacheKey() {
        return null;
    }

    @Override
    protected SqlResult<T> resolveResult() {
        if (resolved) {
            return this;
        }

        // 执行查询
        this.responsePage = queryAction.doQuery();
        if (this.responsePage == null) {
            return this;
        }

        List<Map> list = responsePage.getList();
        List<T> doList = super.convertToDOList(list);
        this.list = doList;
        resolved = true;
        return this;
    }


    /**
     * 转换为目标结果
     * @param clazz 目标类型
     * @param <R>
     * @return
     */
    public <R> List<R> transferTo(Class<R> clazz) {
        List<Map> list = responsePage.getList();
        List<R> pojoList = new ArrayList<R>();

        for (Map map : list) {
            try {
                R object = ConvertUtils.toPoJo(clazz, map);
                pojoList.add(object);
            } catch (Exception e) {
                throw new ExecuteException(ClientResultCode.DATA_CONVERT_EXCEPTION, e);
            }
        }
        return pojoList;
    }

}
