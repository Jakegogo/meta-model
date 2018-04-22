package com.concur.meta.metadata.service.impl;

import java.util.Collections;
import java.util.List;

import com.concur.meta.client.api.persist.Persist;
import com.concur.meta.client.api.query.Query;
import com.concur.meta.metadata.domain.MetaModelColDO;
import com.concur.meta.metadata.domain.MetaModelDO;
import com.concur.meta.metadata.service.LModelService;

/**
 * LModel元数据模型服务接口实现
 *
 * @author yongfu.cyf
 * @create 2017-07-03 下午7:43
 **/
public class LModelServiceImpl implements LModelService {

    private static final String CLASS_NAME = "className";

    private static final String MODEL_ID = "modelId";

    private static final String MODEL_PRIMARY_KEY = "id";

    @Override
    public List<MetaModelDO> list() {
        return Query.create(MetaModelDO.class)
                .execute().getList();
    }

    @Override
    public MetaModelDO getByClassName(String className) {
        return Query.create(MetaModelDO.class)
            .condition(CLASS_NAME, className)
            .execute().getOne();
    }

    @Override
    public List<MetaModelColDO> listColByClass(String className) {
        MetaModelDO lmodelDO =
            Query.create(MetaModelDO.class)
                .condition(CLASS_NAME, className)
                .execute().getOne();

        if (lmodelDO == null) {
            return Collections.emptyList();
        }

        return Query.create(MetaModelColDO.class)
            .condition(MODEL_ID, lmodelDO.getId())
            .execute().getList();
    }

    @Override
    public MetaModelDO add(MetaModelDO lModelDO) {
        return Persist.create(MetaModelDO.class).insert(lModelDO).getResult();
    }

    @Override
    public MetaModelDO get(Long id) {
        return Query.create(MetaModelDO.class).get(id).execute().getOne();
    }

    @Override
    public void update(MetaModelDO lModelDO) {
        Persist.create(MetaModelDO.class).update(lModelDO);
    }

    @Override
    public void delete(MetaModelDO lModelDO) {
        Persist.create(MetaModelDO.class).delete(lModelDO);
    }

}
