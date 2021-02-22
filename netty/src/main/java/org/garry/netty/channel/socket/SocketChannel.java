package org.garry.netty.channel.socket;

import org.garry.netty.channel.Channel;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public interface SocketChannel extends Channel {

    SocketChannelConfig getConfig();

    InetSocketAddress getLocalAddress();

    InetSocketAddress getRemoteAddress();

}
