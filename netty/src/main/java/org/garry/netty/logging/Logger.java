package org.garry.netty.logging;

public abstract class Logger {

    public static Logger getLogger(Class<?> clazz)
    {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name)
    {
        return LoggerFactory.getDefault().getLogger(name);
    }

    public abstract boolean isDebugEnabled();
    public abstract boolean isInfoEnabled();
    public abstract boolean isWarnEnabled();
    public abstract boolean isErrorEnabled();

    public abstract void debug(String msg);
    public abstract void debug(String msg, Throwable cause);
    public abstract void info(String msg);
    public abstract void info(String msg,Throwable cause);
    public abstract void warn(String msg);
    public abstract void warn(String msg, Throwable cause);
    public abstract void error(String msg);
    public abstract void error(String msg, Throwable cause);
}
