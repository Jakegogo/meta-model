package com.concur.meta.core.dbengine.execute;

import java.util.ArrayList;
import java.util.List;

/**
 * 执行上下文
 *
 * @author yongfu.cyf
 * @create 2017-07-03 下午6:09
 **/
public class ExecuteContext {

    /**
     * 线程本地环境
     */
    private static final ThreadLocal<DataSourceContext> dataSources = new ThreadLocal<DataSourceContext>() {
        @Override
        protected DataSourceContext initialValue() {
            return new DataSourceContext();
        }
    };
    /**
     * 设置数据源
     */
    public static void setDataSource(String customerType) {
        dataSources.get().setDataSourceKey(customerType);
    }
    /**
     * 获取数据源
     */
    public static String getDataSource() {
        return dataSources.get().getDataSourceKey();
    }
    /**
     * 清除数据源
     */
    public static void clearDataSource() {
        dataSources.remove();
    }

    /**
     * 追加日志
     * @param log
     */
    public static void appendLog(String log) {
        dataSources.get().getExecuteLog().add(log);
    }

    /**
     * 设置是否输出日志
     * @param showLog
     */
    public static void setShowLog(boolean showLog) {
        dataSources.get().setShowLog(showLog);
    }

    /**
     * 是否展示日志
     * @return
     */
    public static boolean isShowLog() {
        return dataSources.get().isShowLog();
    }

    /**
     * 获取日志
     */
    public static List<String> getLog() {
        return dataSources.get().getExecuteLog();
    }

    /**
     * 数据源上下文
     */
    private static class DataSourceContext {
        private String dataSourceKey;
        /**
         * 是否需要显示日志
         */
        private boolean showLog;
        private List<String> executeLog = new ArrayList<String>();

        public String getDataSourceKey() {
            return dataSourceKey;
        }

        public void setDataSourceKey(String dataSourceKey) {
            this.dataSourceKey = dataSourceKey;
        }

        public List<String> getExecuteLog() {
            return executeLog;
        }

        public void setExecuteLog(List<String> executeLog) {
            this.executeLog = executeLog;
        }

        public boolean isShowLog() {
            return showLog;
        }

        public void setShowLog(boolean showLog) {
            this.showLog = showLog;
        }
    }
}
