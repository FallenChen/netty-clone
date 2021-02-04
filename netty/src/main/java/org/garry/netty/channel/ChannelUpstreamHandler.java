package org.garry.netty.channel;

public interface ChannelUpstreamHandler extends ChannelHandler{

    void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e);
}
