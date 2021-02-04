package org.garry.netty.channel;

public interface ChannelDownstreamHandler extends ChannelHandler{

    void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception;
}
