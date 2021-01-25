package org.garry.netty.channel;

public interface ChannelUpStreamHandler extends ChannelHandler{

    void handleUpStream(ChannelHandlerContext ctx, ChannelEvent e);
}
