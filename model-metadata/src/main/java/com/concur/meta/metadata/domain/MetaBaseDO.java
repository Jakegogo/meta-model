package com.concur.meta.metadata.domain;

import java.util.Date;

import com.concur.meta.client.domain.BaseModel;
import com.concur.meta.client.domain.ToString;

/**
 * 元数据定义基类DO
 *
 * @author yongfu.cyf
 * @create 2017-10-31 上午11:37
 **/
public class MetaBaseDO extends ToString implements BaseModel {

    /**
     * 创建人(TODO)
     */
    private String creator;

    /**
     * 最后一次修改人(TODO)
     */
    private String lastModifier;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 最后修改时间
     */
    private Date gmtModified;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

}
