package com.concur.meta.client.dataobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.concur.meta.client.api.query.AbstractQuery;
import com.concur.meta.client.constants.ParamKeys;
import com.concur.meta.client.result.ClientResultCode;
import com.concur.meta.client.utils.ConvertUtils;
import com.concur.meta.client.annotation.parser.LModelAnnoParser;
import com.concur.meta.client.api.persist.actions.operation.AbstractOperation;
import com.concur.meta.client.exception.ExecuteException;

/**
 * 请求创建器
 *
 * @author yongfu.cyf
 * @create 2017-08-01 下午9:37
 **/
public class MetaRequestCreator {

    /**
     * 构建实例
     * @param clazz 存取的类
     * @param id 主键
     * @param <T>
     * @return
     */
    public static <T> MetaRequest create(Class<T> clazz, Serializable id) {
        MetaRequest metaRequest = new MetaRequest();
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ParamKeys.DO_PRIMARY_KEY, id);

        // 注解信息
        Map<String, Serializable> annoConfig = LModelAnnoParser.parse(clazz);
        params.putAll(annoConfig);

        metaRequest.setObject(params);
        metaRequest.setClassName(clazz.getName());
        return metaRequest;
    }

    /**
     * 构建实例
     * @param clazz 存取的类
     * @param ids 主键集合
     * @param <T>
     * @return
     */
    public static <T> MetaRequest create(Class<T> clazz, Collection<?> ids) {
        MetaRequest metaRequest = new MetaRequest();
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ParamKeys.BATCH_INSTANCE_LIST, new ArrayList<Object>(ids));

        // 注解信息
        Map<String, Serializable> annoConfig = LModelAnnoParser.parse(clazz);
        params.putAll(annoConfig);

        metaRequest.setObject(params);
        metaRequest.setClassName(clazz.getName());
        metaRequest.setBatch(true);
        return metaRequest;
    }

    /**
     * 构建实例
     * @param instance 需要存取的对象
     * @return
     */
    public static MetaRequest create(Serializable instance) {
        MetaRequest metaRequest = new MetaRequest();
        Map<String, Serializable> params;
        // DO转换成map
        try {
            params = ConvertUtils.toMapBean(instance);
        } catch (Exception e) {
            throw new ExecuteException(ClientResultCode.DATA_CONVERT_EXCEPTION, e);
        }
        // 注解信息
        Map<String, Serializable> annoConfig = LModelAnnoParser.parse(instance.getClass());
        params.putAll(annoConfig);

        metaRequest.setObject(params);
        metaRequest.setClassName(instance.getClass().getName());
        return metaRequest;
    }

    /**
     * 构建实例
     * @param instance 需要存取的对象
     * @return
     */
    public static MetaRequest create(Serializable instance, Serializable lastVersion) {
        MetaRequest request = MetaRequestCreator.create(instance);
        if (lastVersion != null) {
            request.addAttach(ParamKeys.MODEL_UPDATE_CAS_VERSION, lastVersion);
        }
        return request;
    }

    /**
     * 构建实例
     * @param instances 需要存取的对象集合
     * @return
     */
    public static MetaRequest create(Collection<?> instances) {
        MetaRequest metaRequest = new MetaRequest();
        Map<String, Serializable> params = new HashMap<String, Serializable>();

        ArrayList<Map<String, Serializable>> instanceList = new ArrayList<Map<String, Serializable>>();
        for (Object instance : instances) {
            Map<String, Serializable> instanceValue;
            // DO转换成map
            try {
                instanceValue = ConvertUtils.toMapBean((Serializable)instance);
            } catch (Exception e) {
                throw new ExecuteException(ClientResultCode.DATA_CONVERT_EXCEPTION, e);
            }
            // 注解信息
            instanceList.add(instanceValue);

            Map<String, Serializable> annoConfig = LModelAnnoParser.parse(instance.getClass());
            params.putAll(annoConfig);
            metaRequest.setClassName(instance.getClass().getName());
        }
        params.put(ParamKeys.BATCH_INSTANCE_LIST, instanceList);

        metaRequest.setObject(params);
        metaRequest.setBatch(true);
        return metaRequest;
    }

    /**
     * 构建合并请求
     * @param operations 操作集合
     * @param <T>
     * @return
     */
    public static <T> MetaRequest valueOfOperations(List<AbstractOperation> operations) {
        MetaRequest metaRequest = new MetaRequest();
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ParamKeys.CONBINE_ACTIONS, (Serializable) operations);
        metaRequest.setObject(params);
        return metaRequest;
    }

    /**
     * 构建实例
     * @param query 查询参数
     * @param <T>
     * @return
     */
    public static <T extends Serializable> MetaRequest create(AbstractQuery<T> query) {
        MetaRequest metaRequest = new MetaRequest();
        metaRequest.setDataSourceId(query.getDataSourceId());

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(ParamKeys.QUERY_TYPE, query.getQueryType());
        params.put(ParamKeys.QUERY_PARAM, query.getQueryParam());
        if (query.getClazz() != null) {
            // 注解信息
            Map<String, Serializable> annoConfig = LModelAnnoParser.parse(query.getClazz());
            params.putAll(annoConfig);
            metaRequest.setClassName(query.getClazz().getName());
        } else {
            metaRequest.setClassName(query.getCode());
        }
        metaRequest.setObject(params);
        return metaRequest;
    }
}
