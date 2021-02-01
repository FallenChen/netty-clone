package org.garry.netty.channel;

import java.util.EventListener;

public interface ChannelFutureListener extends EventListener {

    /**
     * An {@link ChannelFutureListener} that closes the {@link Channel} which is
     * associated with the specified {@link ChannelFuture}
     */
    ChannelFutureListener CLOSE = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            future.getChannel().close();
        }
    };

    /**
     * Invoked when the operation associated with the {@link ChannelFuture}
     * has been completed
     * @param future
     * @throws Exception
     */
    void operationComplete(ChannelFuture future) throws Exception;
}
