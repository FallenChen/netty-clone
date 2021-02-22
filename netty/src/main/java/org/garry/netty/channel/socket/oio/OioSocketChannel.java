package org.garry.netty.channel.socket.oio;

import org.garry.netty.channel.*;
import org.garry.netty.channel.socket.DefaultSocketChannelConfig;
import org.garry.netty.channel.socket.SocketChannel;
import org.garry.netty.channel.socket.SocketChannelConfig;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

abstract class OioSocketChannel extends AbstractChannel
                                implements SocketChannel {

    final Socket socket;
    private final SocketChannelConfig config;
    volatile Thread workerThread;

    OioSocketChannel(
            Channel parent,
            ChannelFactory factory,
            ChannelPipeline pipeline,
            ChannelSink sink,
            Socket socket)
    {
        super(parent,factory,pipeline,sink);
        this.socket = socket;
        config = new DefaultSocketChannelConfig(socket);
    }

    @Override
    public SocketChannelConfig getConfig() {
        return config;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) socket.getLocalSocketAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
       return (InetSocketAddress) socket.getRemoteSocketAddress();
    }

    @Override
    public boolean isBound() {
       return isOpen() & socket.isBound();
    }

    @Override
    public boolean isConnected() {
        return isOpen()&socket.isConnected();
    }

    @Override
    protected boolean setClosed() {
        return super.setClosed();
    }

    @Override
    public ChannelFuture setInterestOps(int interestOps) {
        return null;
    }
}
