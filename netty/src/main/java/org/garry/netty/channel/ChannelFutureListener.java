package org.garry.netty.channel;

import java.util.EventListener;

public interface ChannelFutureListener extends EventListener {

    /**
     * Invoked when the operation associated with the {@link ChannelFuture}
     * has been completed
     * @param future
     * @throws Exception
     */
    void operationComplete(ChannelFuture future) throws Exception;
}
