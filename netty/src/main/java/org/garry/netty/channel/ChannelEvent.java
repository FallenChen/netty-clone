package org.garry.netty.channel;

public interface ChannelEvent {

    Channel getChannel();
    ChannelFuture getFuture();
}
