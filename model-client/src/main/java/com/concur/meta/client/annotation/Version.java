package com.concur.meta.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 乐观锁版本号字段
 *
 * @author yongfu.cyf
 * @create 2017-09-21 下午9:33
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Version {
}
