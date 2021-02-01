package org.garry.netty.channel;

public interface ChannelFactory {

    Channel newChannel(ChannelPipeline pipeline);
}
