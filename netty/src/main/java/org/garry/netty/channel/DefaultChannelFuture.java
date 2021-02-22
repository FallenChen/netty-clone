package org.garry.netty.channel;

import org.garry.netty.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DefaultChannelFuture implements ChannelFuture{

    private static final Logger logger =
            Logger.getLogger(DefaultChannelFuture.class);
    private static final int DEAD_LOCK_CHECK_INTERVAL = 5000;
    private static final Throwable CANCELLED = new Throwable();

    private final Channel channel;
    private final boolean cancellable;

    private ChannelFutureListener firstListener;
    private List<ChannelFutureListener> otherListeners;
    private boolean done;
    private Throwable cause;
    private int waiters;

    public DefaultChannelFuture(Channel channel, boolean cancellable) {
        this.channel = channel;
        this.cancellable = cancellable;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean isCancelled() {
        return cause == CANCELLED;
    }

    @Override
    public boolean isSuccess() {
        return cause == null;
    }

    @Override
    public Throwable getCause() {
        if(cause != CANCELLED)
        {
            return cause;
        }else {
            return null;
        }
    }

    @Override
    public boolean cancel() {
        return false;
    }

    @Override
    public void setSuccess() {

    }

    @Override
    public void setFailure(Throwable cause) {

    }

    @Override
    public void addListener(ChannelFutureListener listener) {
        if(listener == null)
        {
            throw new NullPointerException("listener");
        }

        boolean notifyNow = false;
        synchronized (this)
        {
            if(done)
            {
                notifyNow = true;
            }else {
                if(firstListener == null)
                {
                    firstListener = listener;
                }else
                {
                    if(otherListeners == null)
                    {
                        otherListeners = new ArrayList<>(1);
                    }
                    otherListeners.add(listener);
                }
            }
        }

        if (notifyNow)
        {
            notifyListener(listener);
        }
    }



    @Override
    public void removeListener(ChannelFutureListener listener) {
        if(listener == null)
        {
            throw new NullPointerException("listener");
        }

        synchronized (this)
        {
            if(!done)
            {
                if(listener == firstListener)
                {
                    if(otherListeners != null && !otherListeners.isEmpty())
                    {
                        firstListener = otherListeners.remove(0);
                    }else
                    {
                        firstListener = null;
                    }
                }
            }else if(otherListeners != null)
            {
                otherListeners.remove(listener);
            }
        }
    }

    @Override
    public ChannelFuture await() throws InterruptedException {
        synchronized (this)
        {
            while (!done)
            {
                waiters++;
                try {
                    this.wait(DEAD_LOCK_CHECK_INTERVAL);
                    checkDeadLock();
                }finally {
                    waiters--;
                }
            }
        }
        return this;
    }

    private void checkDeadLock() {

    }


    @Override
    public ChannelFuture awaitUninterruptibly() {
        return null;
    }

    @Override
    public boolean await(long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public boolean await(long timeoutMills) {
        return false;
    }

    @Override
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        return false;
    }

    @Override
    public boolean awaitUninterruptibly(long timeoutMillis) {
        return false;
    }

    private void notifyListener(ChannelFutureListener l) {
        try {
            l.operationComplete(this);
        } catch (Throwable t) {
            logger.warn(
                    "An exception was thrown by " +
                            ChannelFutureListener.class.getSimpleName() + ".",t);
        }
    }
}
