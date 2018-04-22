package com.concur.meta.core.dbengine.logger;

import java.sql.Connection;
import java.sql.SQLException;

import com.concur.meta.core.dbengine.execute.ExecuteContext;
import com.concur.meta.core.dbengine.execute.routing.DynamicDBDataSource;
import org.apache.ibatis.logging.Log;

/**
 * 包含日志的动态数据源
 *
 * @author yongfu.cyf
 * @create 2017-09-16 下午9:06
 **/
public class LogableDynamicDBDataSource extends DynamicDBDataSource {

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = super.getConnection();
        if (ExecuteContext.isShowLog()) {
            return ConnectionLogger.newInstance(connection, new ProxyLogger(), 0);
        } else {
            return connection;
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = super.getConnection(username, password);
        if (ExecuteContext.isShowLog()) {
            return ConnectionLogger.newInstance(connection, new ProxyLogger(), 0);
        } else {
            return connection;
        }
    }


    private static class ProxyLogger implements Log {

        @Override
        public boolean isDebugEnabled() {
            return true;
        }

        @Override
        public boolean isTraceEnabled() {
            return true;
        }

        @Override
        public void error(String s, Throwable e) {
            ExecuteContext.appendLog("ERROR:" + s);

        }

        @Override
        public void error(String s) {
            ExecuteContext.appendLog("ERROR:" + s);
        }

        @Override
        public void debug(String s) {
            ExecuteContext.appendLog("DEBUG:" + s);
        }

        @Override
        public void trace(String s) {
            ExecuteContext.appendLog("TRACE:" + s);
        }

        @Override
        public void warn(String s) {
            ExecuteContext.appendLog("WARN:" + s);
        }
    }

}
