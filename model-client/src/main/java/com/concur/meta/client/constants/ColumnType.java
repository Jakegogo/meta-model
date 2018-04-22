package com.concur.meta.client.constants;

import java.io.Serializable;

/**
 * 字段存储类型
 *
 * @author yongfu.cyf
 * @create 2017-07-31 下午3:44
 **/
public enum ColumnType implements Serializable {
    /**
     * 字符串类型
     * 无需指定, 默认将非8种(以及Date)基本类型的属性转换成字符串进行存储
     */
    STRING,
    /**
     * Attribues类型
     * 如果属性类型是Map, 无需指定, 将默认自动使用此选项
     */
    ATTRIBUTES,
    /**
     * Json String类型
     * 自定义类型的属性可以使用JSON存储
     */
    JSON,
    ;
}
