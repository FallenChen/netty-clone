package org.garry.netty.handler.codec.serialization;

import org.garry.netty.buffer.ChannelBuffer;
import org.garry.netty.channel.Channel;
import org.garry.netty.channel.ChannelHandlerContext;
import org.garry.netty.handler.codec.frame.FrameDecoder;

public class ObjectDecoder extends FrameDecoder {

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) {
        if(buffer.readableBytes() < 4)
        {
            return null;
        }
        // todo why getInt() ???
        int dataLen = buffer.getInt(buffer.readerIndex());

        return null;
    }
}
