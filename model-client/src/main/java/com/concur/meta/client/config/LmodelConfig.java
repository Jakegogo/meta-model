package com.concur.meta.client.config;

/**
 * Lmodel配置
 *
 * @author yongfu.cyf
 * @create 2017-09-20 下午7:11
 **/
public class LmodelConfig {

    /**
     * 是否展示Log
     */
    private static boolean showLog = false;
    /**
     * 设置版本号
     */
    private static long version = 0L;

    /**
     * 开启查询SQL日志
     */
    public static void enableQueryLog() {
        showLog = true;
    }

    public static boolean isShowLog() {
        return showLog;
    }

    /**
     * 设置查询SQL日志开关
     * @param showLog
     */
    public static void setShowLog(boolean showLog) {
        LmodelConfig.showLog = showLog;
    }


}
