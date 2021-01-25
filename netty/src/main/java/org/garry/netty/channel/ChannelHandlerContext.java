package org.garry.netty.channel;

public interface ChannelHandlerContext {

    ChannelPipeline getPipeline();

    String getName();
    ChannelHandler getHandler();
    boolean canHandleUpstream();
    boolean canHandleDownstream();

    void sendUpstream(ChannelEvent e);
    void sendDownstream(ChannelEvent e);


}
