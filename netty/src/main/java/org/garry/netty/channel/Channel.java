package org.garry.netty.channel;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.UUID;

/**
 * similar {@link java.nio.channels.SelectionKey}
 */
public interface Channel {

    int OP_NONE = 0;
    int OP_READ = 1;
    int OP_WRITE = 4;
    int OP_READ_WRITE = OP_READ | OP_WRITE;

    UUID getId();
    ChannelFactory getFactory();
    Channel getParent();
    ChannelConfig getConfig();
    ChannelPipeline getPipeline();

    boolean isOpen();
    boolean isBound();
    boolean isConnected();

    SocketAddress getLocalAddress();
    SocketAddress getRemoteAddress();

    ChannelFuture write(Object message);
    ChannelFuture write(Object message, SocketAddress remoteAddress);

    ChannelFuture bind(SocketAddress localAddress);
    ChannelFuture connect(SocketAddress remoteAddress);
    ChannelFuture disconnect();
    ChannelFuture close();

    int getInterestOps();
    boolean isReadable();
    boolean isWritable();
    ChannelFuture setInterestOps(int interestOps);
    ChannelFuture setReadable(boolean readable);
}
