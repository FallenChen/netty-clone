package org.garry.netty.channel.socket.nio;

public interface ReceiveBufferSizePredictor {

    int nextReceiveBufferSize();

    void previousReceiveBufferSize(int previousReceiveBufferSize);
}
