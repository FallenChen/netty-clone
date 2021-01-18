package org.garry.netty.channel;

public interface ChannelPipeline {

    void addFirst(String name, ChannelHandler handler);
    void addLast(String name, ChannelHandler handler);
    void addBefore(String baseName, String name, ChannelHandler handler);
    void addAfter(String baseName, String name, ChannelHandler handler);
}
