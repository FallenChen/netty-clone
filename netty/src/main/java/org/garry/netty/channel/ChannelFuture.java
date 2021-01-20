package org.garry.netty.channel;

public interface ChannelFuture {

    Channel getChannel();

    boolean isDone();
    boolean isCancelled();
    boolean isSuccess();
    Throwable getCause();

    boolean cancel();

    void setSuccess();
    void setFailure(Throwable cause);


}
