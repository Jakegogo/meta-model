package com.concur.meta.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.concur.meta.client.constants.ColumnType;

/**
 * 字段配置
 *
 * @author yongfu.cyf
 * @create 2017-07-05 下午3:29
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LColumn {

    /**
     * 指定属性名(驼峰形式)
     * @return
     */
    String name() default "";

    /**
     * 字段存储类型
     * @return
     */
    ColumnType type() default ColumnType.STRING;

}
