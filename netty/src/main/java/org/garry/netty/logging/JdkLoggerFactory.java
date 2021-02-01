package org.garry.netty.logging;

import java.util.logging.Level;

public class JdkLoggerFactory extends LoggerFactory{

    @Override
    public Logger getLogger(String name) {
        final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(name);

        return new Logger() {
            @Override
            public void debug(String msg) {
                logger.log(Level.FINE, msg);
            }

            @Override
            public void debug(String msg, Throwable cause) {
                logger.log(Level.FINE, msg, cause);
            }

            @Override
            public void error(String msg) {
                logger.log(Level.SEVERE, msg);
            }

            @Override
            public void error(String msg, Throwable cause) {
                logger.log(Level.SEVERE, msg, cause);
            }

            @Override
            public void info(String msg) {
                logger.log(Level.INFO, msg);
            }

            @Override
            public void info(String msg, Throwable cause) {
                logger.log(Level.INFO, msg, cause);
            }

            @Override
            public boolean isDebugEnabled() {
                return logger.isLoggable(Level.FINE);
            }

            @Override
            public boolean isErrorEnabled() {
                return logger.isLoggable(Level.SEVERE);
            }

            @Override
            public boolean isInfoEnabled() {
                return logger.isLoggable(Level.INFO);
            }

            @Override
            public boolean isWarnEnabled() {
                return logger.isLoggable(Level.WARNING);
            }

            @Override
            public void warn(String msg) {
                logger.log(Level.WARNING, msg);
            }

            @Override
            public void warn(String msg, Throwable cause) {
                logger.log(Level.WARNING, msg, cause);
            }

            @Override
            public String toString() {
                return String.valueOf(logger.getName());
            }
        };
    }
}
