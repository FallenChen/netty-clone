package org.garry.netty.channel;

import org.garry.netty.util.TimeBasedUuidGenerator;

import java.net.SocketAddress;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractChannel implements Channel,Comparable<Channel>{

    private final UUID id = TimeBasedUuidGenerator.generate();
    private final Channel parent;
    private final ChannelFactory factory;
    private final ChannelPipeline pipeline;
    private final ChannelFuture succeededFuture = new SucceededChannelFuture(this);

    private final AtomicBoolean closed = new AtomicBoolean();
    private volatile int interestOps = OP_READ;

    // Cache for the string representation of this channel todo
    private String strVal;

    protected AbstractChannel(
            Channel parent, ChannelFactory factory,
            ChannelPipeline pipeline, ChannelSink sink)
    {
        this.parent = parent;
        this.factory = factory;
        this.pipeline = pipeline;
        pipeline.attach(this,sink);
    }

    @Override
    public final UUID getId() {
        return id;
    }

    @Override
    public Channel getParent() {
        return parent;
    }

    @Override
    public ChannelFactory getFactory() {
        return factory;
    }

    @Override
    public ChannelPipeline getPipeline() {
        return pipeline;
    }

    protected ChannelFuture getSucceededFuture()
    {
        return succeededFuture;
    }

    protected ChannelFuture getUnsupportedOperationFuture()
    {
        return new FailedChannelFuture(this,new UnsupportedOperationException());
    }

    public final int hashCode()
    {
        return System.identityHashCode(this);
    }

    public final boolean equal(Object o)
    {
        return this == o;
    }

    @Override
    public int compareTo(Channel o) {
       return System.identityHashCode(this) - System.identityHashCode(o);
    }

    public boolean isOpen()
    {
        return !closed.get();
    }

    protected boolean setClosed()
    {
        return closed.compareAndSet(false,true);
    }

    @Override
    public ChannelFuture bind(SocketAddress localAddress) {
       return
    }
}

