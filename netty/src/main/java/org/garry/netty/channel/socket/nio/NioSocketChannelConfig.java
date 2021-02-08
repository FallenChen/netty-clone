package org.garry.netty.channel.socket.nio;

import org.garry.netty.channel.socket.SocketChannelConfig;

import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public interface NioSocketChannelConfig extends SocketChannelConfig {

    int getWriteSpinCount();

    /**
     * The maximum loop count for a write operation until
     * {@link WritableByteChannel#write(ByteBuffer)} returns a
     * non-zero value.
     * It is similar to what a spin lock is for in concurrency programming
     * It improves memory unilization and write throughput significantly
     * @param writeSpinCount
     */
    void setWriteSpinCount(int writeSpinCount);



    boolean isReadWriteFair();
    void setReadWriteFair(boolean fair);
}
