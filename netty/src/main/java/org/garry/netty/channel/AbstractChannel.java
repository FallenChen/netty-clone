package org.garry.netty.channel;

import org.garry.netty.util.TimeBasedUuidGenerator;

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



}

