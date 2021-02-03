package org.garry.netty.channel;

public class SucceededChannelFuture extends CompleteChannelFuture{

    public SucceededChannelFuture(Channel channel) {
        super(channel);
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public Throwable getCause() {
        return null;
    }
}
