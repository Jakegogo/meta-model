package com.concur.meta.client.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by taixu.zqq on 2017/2/4.
 */
public class ToString implements Serializable {

    private static final long serialVersionUID = 894815529144843913L;

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
