package org.garry.netty.channel.socket.nio;

public class DefaultReceiveBufferSizePredictor implements ReceiveBufferSizePredictor{

    private static final int DEFAULT_MINIMUM = 256;
    private static final int DEFAULT_INITIAL = 1024;
    private static final int DEFAULT_MAXIMUM = 1048576;

    private final int minimum;
    private final int maximum;
    private int nextReceiveBufferSize = 1024;
    private boolean shouldHalveNow;

    public DefaultReceiveBufferSizePredictor()
    {
        this(DEFAULT_MINIMUM,DEFAULT_INITIAL,DEFAULT_MAXIMUM);
    }

    public DefaultReceiveBufferSizePredictor(int minimum,int initial, int maximum) {
        if (minimum <= 0) {
            throw new IllegalArgumentException("minimum: " + minimum);
        }
        if (initial < minimum) {
            throw new IllegalArgumentException("initial: " + initial);
        }
        if (maximum < initial) {
            throw new IllegalArgumentException("maximum: " + maximum);
        }
        this.minimum = minimum;
        nextReceiveBufferSize = initial;
        this.maximum = maximum;
    }

    @Override
    public int nextReceiveBufferSize() {
        return nextReceiveBufferSize;
    }

    @Override
    public void previousReceiveBufferSize(int previousReceiveBufferSize) {
        // todo 无符号右移1位
        if(previousReceiveBufferSize < nextReceiveBufferSize >>> 1)
        {
            if(shouldHalveNow)
            {
                nextReceiveBufferSize = Math.max(minimum, nextReceiveBufferSize >>> 1);
                shouldHalveNow = false;
            }else {
                shouldHalveNow = true;
            }
        }else if(previousReceiveBufferSize == nextReceiveBufferSize)
        {
            nextReceiveBufferSize = Math.min(maximum,nextReceiveBufferSize << 1);
            shouldHalveNow = false;
        }
    }
}
