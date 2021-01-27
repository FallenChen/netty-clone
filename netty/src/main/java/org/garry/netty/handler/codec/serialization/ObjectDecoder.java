package org.garry.netty.handler.codec.serialization;

import org.garry.netty.buffer.ChannelBuffer;
import org.garry.netty.buffer.ChannelBufferInputStream;
import org.garry.netty.channel.Channel;
import org.garry.netty.channel.ChannelHandlerContext;
import org.garry.netty.handler.codec.frame.FrameDecoder;

import java.io.StreamCorruptedException;

public class ObjectDecoder extends FrameDecoder {

    private final int maxObjectSize;
    private final ClassLoader classLoader;

    public ObjectDecoder() {
        this(1048576);
    }

    public ObjectDecoder(int maxObjectSize) {
        this(maxObjectSize,Thread.currentThread().getContextClassLoader());
    }

    public ObjectDecoder(int maxObjectSize, ClassLoader classLoader) {
        if(maxObjectSize <= 0)
        {
            throw new IllegalAccessException("maxObjectSize: " + maxObjectSize);
        }
        if(classLoader == null)
        {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        this.maxObjectSize = maxObjectSize;
        this.classLoader = classLoader;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        if(buffer.readableBytes() < 4)
        {
            return null;
        }
        // todo why getInt() ???
        int dataLen = buffer.getInt(buffer.readerIndex());
        if(dataLen <= 0)
        {
            throw new StreamCorruptedException("invalid data length: " + dataLen);
        }
        if(dataLen > maxObjectSize)
        {
            throw new StreamCorruptedException(
                    "data length too bif: " +dataLen+ " (max: " + maxObjectSize + ')');
        }

        if(buffer.readableBytes() < dataLen + 4)
        {
            return null;
        }

        // todo???
        buffer.skipBytes(4);


        return new CompactObjectInputStream(
                new ChannelBufferInputStream(buffer,dataLen),
                classLoader).readObject() ;
    }
}
