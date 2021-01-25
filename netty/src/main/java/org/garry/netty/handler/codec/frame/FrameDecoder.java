package org.garry.netty.handler.codec.frame;

import org.garry.netty.buffer.ChannelBuffer;
import org.garry.netty.channel.Channel;
import org.garry.netty.channel.ChannelHandlerContext;
import org.garry.netty.channel.SimpleChannelHandler;

public abstract class FrameDecoder extends SimpleChannelHandler {

    protected abstract Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer);

}
