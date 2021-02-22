package org.garry.netty.channel;

import java.util.concurrent.TimeUnit;

public interface ChannelFuture {

    Channel getChannel();

    boolean isDone();
    boolean isCancelled();
    boolean isSuccess();
    Throwable getCause();

    boolean cancel();

    void setSuccess();
    void setFailure(Throwable cause);

    void addListener(ChannelFutureListener listener);
    void removeListener(ChannelFutureListener listener);

    ChannelFuture await() throws InterruptedException;
    ChannelFuture awaitUninterruptibly();
    boolean await(long timeout, TimeUnit unit);
    boolean await(long timeoutMills);
    boolean awaitUninterruptibly(long timeout, TimeUnit unit);
    boolean awaitUninterruptibly(long timeoutMillis);

}
