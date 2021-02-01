package org.garry.netty.channel;

import java.util.Map;

public interface ChannelConfig {

    void setOptions(Map<String,Object> options);

    ChannelPipelineFactory getPipelineFactory();

    void setPipelineFactory(ChannelPipelineFactory pipelineFactory);

    int getConnectTimeoutMillis();

    void setConnectTimeoutMills(int connectTimeoutMills);

    int getWriteTimeoutMillis();

    void setWriteTimeoutMillis(int writeTimeoutMillis);
}
