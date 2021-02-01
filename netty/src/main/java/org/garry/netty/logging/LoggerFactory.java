package org.garry.netty.logging;

public abstract class LoggerFactory {

    private static volatile LoggerFactory defaultInstance = new JdkLoggerFactory();

    public static LoggerFactory getDefault()
    {
        return defaultInstance;
    }

    public static void setDefault(LoggerFactory defaultInstance)
    {
        if(defaultInstance == null)
        {
            throw new NullPointerException("defaultInstance");
        }
        LoggerFactory.defaultInstance = defaultInstance;
    }

    public abstract Logger getLogger(String name);

}
