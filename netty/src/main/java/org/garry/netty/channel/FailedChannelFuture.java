package org.garry.netty.channel;

public class FailedChannelFuture extends CompleteChannelFuture{

    private final Throwable cause;

    public FailedChannelFuture(Channel channel, Throwable cause)
    {
        super(channel);
        if (cause == null)
        {
            throw new NullPointerException("cause");
        }
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public boolean isSuccess()
    {
        return false;
    }
}
