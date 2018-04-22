package com.concur.meta.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * LModel数据DO
 * <li> 优先走配置,然后走注解</li>
 * <li> 需要指定数据源和建表</li>
 * @author yongfu.cyf
 * @create 2017-06-29 下午8:13
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LModel {

    /**
     * 指定表名
     * @return
     */
    String table();

    /**
     * 指定默认数据源类型
     * @return
     */
    long defaultDateSource() default -1L;

    /**
     * 模型描述
     * @return
     */
    String description() default "";

}
