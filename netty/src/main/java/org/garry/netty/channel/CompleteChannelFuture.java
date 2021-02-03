package org.garry.netty.channel;

import org.garry.netty.logging.Logger;

import java.util.concurrent.TimeUnit;

public abstract class CompleteChannelFuture implements ChannelFuture{

    private static final Logger logger =
            Logger.getLogger(CompleteChannelFuture.class);

    private final Channel channel;

    protected CompleteChannelFuture(Channel channel) {
        if(channel == null)
        {
            throw new NullPointerException("channel");
        }
        this.channel = channel;
    }

    @Override
    public void addListener(ChannelFutureListener listener) {
        try {
            listener.operationComplete(this);
        } catch (Throwable t) {
            logger.warn(
                    "An exception was thrown by " +
                            ChannelFutureListener.class.getSimpleName() + ".", t);
        }
    }

    @Override
    public void removeListener(ChannelFutureListener listener) {
        // NOOP
    }

    @Override
    public ChannelFuture await() {
        return this;
    }

    @Override
    public boolean await(long timeoutMills) {
        return true;
    }

    @Override
    public boolean await(long timeout, TimeUnit unit) {
        return true;
    }

    @Override
    public ChannelFuture awaitUninterruptibly() {
        return this;
    }

    @Override
    public boolean awaitUninterruptibly(long timeoutMillis) {
        return true;
    }

    @Override
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        return true;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public void setFailure(Throwable cause) {
        // Unused
    }

    @Override
    public void setSuccess() {
        // Unused
    }

    @Override
    public boolean cancel() {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
