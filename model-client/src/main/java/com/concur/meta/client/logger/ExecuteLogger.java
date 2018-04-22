package com.concur.meta.client.logger;

import java.util.List;

import com.concur.meta.client.dataobject.MetaResponse;
import com.concur.meta.client.exception.LModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行细节Log输出
 *
 * @author yongfu.cyf
 * @create 2017-09-22 下午4:41
 **/
public class ExecuteLogger {


    protected static final Logger SQL_LOGGER = LoggerFactory.getLogger("LMODEL_SQL_LOG");

    /**
     * 输出日志
     * @param response
     */
    public static void doLogger(MetaResponse response) {
        if (response == null) {
            return;
        }
        List<String> loggers = response.getExecuteLog();
        if (loggers != null && SQL_LOGGER.isErrorEnabled()) {
            for (String logger : loggers) {
                SQL_LOGGER.info(logger);
            }
        }
    }

    /**
     * 输出日志
     * @param e
     */
    public static void doLogger(RuntimeException e) {
        if (e == null) {
            return;
        }
        if (!(e instanceof LModelException)) {
            return;
        }
        List<String> loggers = ((LModelException)e).getExecuteLog();
        if (loggers != null && SQL_LOGGER.isErrorEnabled()) {
            for (String logger : loggers) {
                SQL_LOGGER.info(logger);
            }
        }
    }

}
