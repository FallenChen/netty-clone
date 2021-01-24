package org.garry.netty.channel;

public interface ChannelSink {

    void eventSunk(ChannelPipeline pipeline, ChannelEvent e);

    void exceptionCaught(ChannelPipeline pipeline, ChannelEvent e, ChannelPipelineException cause);
}
