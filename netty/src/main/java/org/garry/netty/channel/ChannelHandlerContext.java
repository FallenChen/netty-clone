package org.garry.netty.channel;

public interface ChannelHandlerContext {

    ChannelPipeline getPipeline();

    String getName();
    ChannelHandler getHandler();

}
