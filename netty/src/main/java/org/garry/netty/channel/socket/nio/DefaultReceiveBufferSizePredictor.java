package org.garry.netty.channel.socket.nio;

public class DefaultReceiveBufferSizePredictor implements ReceiveBufferSizePredictor{

    @Override
    public int nextReceiveBufferSize() {
        return 0;
    }

    @Override
    public void previousReceiveBufferSize(int previousReceiveBufferSize) {

    }
}
