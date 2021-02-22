package org.garry.netty.channel;

import java.net.SocketAddress;
import java.util.Map;

public class Channels {

    // pipeline factory methods
    public static ChannelPipeline pipeline()
    {
        return new DefaultChannelPipeline();
    }

    public static ChannelPipeline pipeline(ChannelPipeline pipeline)
    {
        ChannelPipeline newPipeline = pipeline();
        for(Map.Entry<String, ChannelHandler> e: pipeline.toMap().entrySet())
        {
            newPipeline.addLast(e.getKey(), e.getValue());
        }
        return newPipeline;
    }

    public static ChannelPipelineFactory pipelineFactory(
            final ChannelPipeline pipeline)
    {
        return new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() {
               return pipeline(pipeline);
            }
        };
    }

    // future factory methods
    public static ChannelFuture future(Channel channel)
    {
        return future(channel, false);
    }

    public static ChannelFuture future(Channel channel,boolean cancellable)
    {
        return new
    }


    public static ChannelFuture bind(Channel channel, SocketAddress localAddress)
    {

    }
}
