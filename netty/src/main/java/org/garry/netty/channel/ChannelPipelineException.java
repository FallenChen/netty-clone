package org.garry.netty.channel;

public class ChannelPipelineException extends ChannelException{

    public ChannelPipelineException() {
        super();
    }

    public ChannelPipelineException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelPipelineException(String message) {
        super(message);
    }

    public ChannelPipelineException(Throwable cause) {
        super(cause);
    }
}
