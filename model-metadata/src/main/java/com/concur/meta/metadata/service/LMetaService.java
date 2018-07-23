package com.concur.meta.metadata.service;

import java.util.List;

import com.concur.meta.metadata.domain.MetaModelColDO;
import com.concur.meta.metadata.domain.MetaModelDO;

/**
 * LModel元数据模型服务接口
 *
 * @author yongfu.cyf
 * @create 2017-07-03 下午7:39
 **/
public interface LMetaService {

    /**
     * 获取LModelDO列表
     * @return
     */
    List<MetaModelDO> list();

    /**
     * 根据类名获取元数据模型
     * @param className
     * @return
     */
    MetaModelDO getByClassName(String className);

    /**
     * 根据类名获取元数据字段列表
     * @param className
     * @return
     */
    List<MetaModelColDO> listColByClass(String className);

    /**
     * 添加LModelDO
     * @param lModelDO MetaModelDO
     * @return
     */
    MetaModelDO add(MetaModelDO lModelDO);

    /**
     * 根据ID获取LModelDO
     * @param id
     * @return
     */
    MetaModelDO get(Long id);

    /**
     * 更新LModelDO
     * @param lModelDO MetaModelDO
     * @return
     */
    void update(MetaModelDO lModelDO);

    /**
     * 删除LModelDO
     * @param lModelDO MetaModelDO
     * @return
     */
    void delete(MetaModelDO lModelDO);

}
