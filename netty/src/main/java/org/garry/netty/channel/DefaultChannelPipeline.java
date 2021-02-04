package org.garry.netty.channel;

import org.garry.netty.logging.Logger;

import java.lang.annotation.AnnotationFormatError;
import java.util.Map;

import static org.garry.netty.channel.ChannelPipelineCoverage.ALL;
import static org.garry.netty.channel.ChannelPipelineCoverage.ONE;

public class DefaultChannelPipeline implements ChannelPipeline{

    static final Logger logger = Logger.getLogger(DefaultChannelPipeline.class);

    private final ChannelSink discardingSink = new ChannelSink() {
        @Override
        public void eventSunk(ChannelPipeline pipeline, ChannelEvent e) {
            logger.warn("Not attached yet; discarding: " + e);
        }

        @Override
        public void exceptionCaught(ChannelPipeline pipeline, ChannelEvent e, ChannelPipelineException cause) {
            throw cause;
        }
    };

    private volatile Channel channel;
    private volatile ChannelSink sink;
    private volatile DefaultChannelHandlerContext head;
    private volatile DefaultChannelHandlerContext tail;

    @Override
    public void addFirst(String name, ChannelHandler handler) {

    }

    @Override
    public void addLast(String name, ChannelHandler handler) {

    }

    @Override
    public void addBefore(String baseName, String name, ChannelHandler handler) {

    }

    @Override
    public void addAfter(String baseName, String name, ChannelHandler handler) {

    }

    @Override
    public void remove(ChannelHandler handler) {

    }

    @Override
    public ChannelHandler remove(String name) {
        return null;
    }

    @Override
    public <T extends ChannelHandler> T remove(Class<T> handlerType) {
        return null;
    }

    @Override
    public ChannelHandler removeFirst() {
        return null;
    }

    @Override
    public ChannelHandler removeLast() {
        return null;
    }

    @Override
    public void replace(ChannelHandler oldHandler, String newName, ChannelHandler newHandler) {

    }

    @Override
    public ChannelHandler replace(String oldName, String newName, ChannelHandler newHandler) {
        return null;
    }

    @Override
    public <T extends ChannelHandler> T replace(Class<T> oldHandlerType, String newName, ChannelHandler newHandler) {
        return null;
    }

    @Override
    public ChannelHandler getFirst() {
        return null;
    }

    @Override
    public ChannelHandler getLast() {
        return null;
    }

    @Override
    public ChannelHandler get(String name) {
        return null;
    }

    @Override
    public <T extends ChannelHandler> T get(Class<T> handlerType) {
        return null;
    }

    @Override
    public ChannelHandlerContext getContext(ChannelHandler handler) {
        return null;
    }

    @Override
    public ChannelHandlerContext getContext(String name) {
        return null;
    }

    @Override
    public ChannelHandlerContext getContext(Class<? extends ChannelHandler> handlerType) {
        return null;
    }

    @Override
    public void sendUpstream(ChannelEvent e) {

    }

    @Override
    public void sendDownstream(ChannelEvent e) {

    }

    @Override
    public Channel getChannel() {
        return null;
    }

    @Override
    public ChannelSink getSink() {
        return null;
    }

    @Override
    public void attach(Channel channel, ChannelSink sink) {

    }

    @Override
    public Map<String, ChannelHandler> toMap() {
        return null;
    }

    void sendUpstream(DefaultChannelHandlerContext ctx, ChannelEvent e)
    {
        try {
            ((ChannelUpstreamHandler)ctx.getHandler()).handleUpstream(ctx,e);
        }catch (Throwable t)
        {
            notifyException(e,t);
        }
    }

    void sendDownstream(DefaultChannelHandlerContext ctx, ChannelEvent e) {
        try {
            ((ChannelDownstreamHandler) ctx.getHandler()).handleDownstream(ctx, e);
        } catch (Throwable t) {
            notifyException(e, t);
        }
    }

    DefaultChannelHandlerContext getActualDownstreamContext(DefaultChannelHandlerContext ctx)
    {
        if(ctx == null)
        {
            return null;
        }

        DefaultChannelHandlerContext realCtx = null;
        while (!realCtx.canHandleDownstream())
        {
            realCtx = realCtx.prev;
            if(realCtx == null)
            {
                return null;
            }
        }
        return realCtx;
    }

    DefaultChannelHandlerContext getActualUpstreamContext(DefaultChannelHandlerContext ctx)
    {
        if(ctx == null)
        {
            return null;
        }

        DefaultChannelHandlerContext realCtx = ctx;
        while (!realCtx.canHandleUpstream())
        {
            realCtx = realCtx.next;
            if (realCtx == null)
            {
                return null;
            }
        }
        return realCtx;
    }

    void notifyException(ChannelEvent e,Throwable t)
    {
        ChannelPipelineException pe;
        if(t instanceof ChannelPipelineException)
        {
            pe = (ChannelPipelineException) t;
        }else {
            pe = new ChannelPipelineException(t);
        }

        try {
            sink.exceptionCaught(this,e,pe);
        }catch (Exception e1)
        {
            logger.warn("An exception was thrown by an exception handler." ,e1);
        }
    }

    private class DefaultChannelHandlerContext implements ChannelHandlerContext
    {
        volatile DefaultChannelHandlerContext next;
        volatile DefaultChannelHandlerContext prev;
        private final String name;
        private final ChannelHandler handler;
        private final boolean canHandleUpstream;
        private final boolean canHandleDownstram;

        DefaultChannelHandlerContext(
                DefaultChannelHandlerContext prev,DefaultChannelHandlerContext next,
                String name, ChannelHandler handler)
        {
            if(name == null)
            {
                throw new NullPointerException("name");
            }
            if(handler == null)
            {
                throw new NullPointerException("handler");
            }
            canHandleUpstream = handler instanceof ChannelUpstreamHandler;
            canHandleDownstram = handler instanceof ChannelDownstreamHandler;

            if(!canHandleUpstream && !canHandleDownstram)
            {
                throw new IllegalArgumentException(
                        "handler must be either " +
                                ChannelUpstreamHandler.class.getName() + " or " +
                                ChannelDownstreamHandler.class.getName() + '.');
            }

            ChannelPipelineCoverage coverage = handler.getClass().getAnnotation(ChannelPipelineCoverage.class);

            if(coverage == null)
            {
                logger.warn(
                        "Handler '" + handler.getClass().getName() +
                                "' doesn't have a '" +
                                ChannelPipelineCoverage.class.getSimpleName() +
                                "' annotation with its class declaration. " +
                                "It is recommended to add the annotation to tell if " +
                                "one handler instance can handle more than one pipeline " +
                                "(\"" + ALL + "\") or not (\"" + ONE + "\")");
            }else
            {
                String coverageValue = coverage.value();
                if (coverageValue == null) {
                    throw new AnnotationFormatError(
                            ChannelPipelineCoverage.class.getSimpleName() +
                                    " annotation value is undefined for type: " +
                                    handler.getClass().getName());
                }


                if (!coverageValue.equals(ALL) && !coverageValue.equals(ONE)) {
                    throw new AnnotationFormatError(
                            ChannelPipelineCoverage.class.getSimpleName() +
                                    " annotation value: " + coverageValue +
                                    " (must be either \"" + ALL + "\" or \"" + ONE + ")");
                }
            }
            this.prev = prev;
            this.next = next;
            this.name = name;
            this.handler = handler;
        }

        @Override
        public ChannelPipeline getPipeline() {
            return DefaultChannelPipeline.this;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public ChannelHandler getHandler() {
            return handler;
        }

        @Override
        public boolean canHandleUpstream() {
            return canHandleUpstream;
        }

        @Override
        public boolean canHandleDownstream() {
            return canHandleDownstram;
        }

        @Override
        public void sendUpstream(ChannelEvent e) {
            DefaultChannelHandlerContext prev = getActualDownstreamContext(this.prev);
            if (prev == null)
            {
                try {
                    getSink().eventSunk(DefaultChannelPipeline.this,e);
                }catch (Throwable t)
                {
                    notifyException(e,t);
                }
            }else
            {
                DefaultChannelPipeline.this.sendDownstream(prev,e);
            }
        }

        @Override
        public void sendDownstream(ChannelEvent e) {
            DefaultChannelHandlerContext next = getActualUpstreamContext(this.next);
            if(next == null)
            {
                DefaultChannelPipeline.this.sendUpstream(next,e);
            }
        }
    }
}
