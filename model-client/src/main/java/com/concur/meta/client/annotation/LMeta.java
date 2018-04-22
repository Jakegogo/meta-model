package com.concur.meta.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * LModel元数据
 * <li> 支持存储任意类型的元数据</li>
 * <li> 系统自动存储和索引</li>
 * @author yongfu.cyf
 * @create 2017-11-01 下午7:46
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LMeta {


}
