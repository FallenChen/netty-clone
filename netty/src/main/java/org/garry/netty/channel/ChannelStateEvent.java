package org.garry.netty.channel;

public interface ChannelStateEvent extends ChannelEvent{

    ChannelState getState();
    Object getValue();
}
