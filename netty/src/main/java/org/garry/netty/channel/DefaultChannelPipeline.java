package org.garry.netty.channel;

import org.garry.netty.logging.Logger;

import java.lang.annotation.AnnotationFormatError;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

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
    private final Map<String,DefaultChannelHandlerContext> name2ctx =
            new HashMap<>(4);


    @Override
    public synchronized void addFirst(String name, ChannelHandler handler) {
        if (name2ctx.isEmpty())
        {
            init(name,handler);
        }
        else
        {
            checkDuplicateName(name);
            DefaultChannelHandlerContext oldHead = head;
            DefaultChannelHandlerContext newHead = new DefaultChannelHandlerContext(null,oldHead,name,handler);
            oldHead.prev = newHead;
            head = newHead;
            name2ctx.put(name,newHead);
        }
    }

    private void init(String name,ChannelHandler handler)
    {
        DefaultChannelHandlerContext ctx = new DefaultChannelHandlerContext(null, null, name, handler);
        head = tail = ctx;
        name2ctx.clear();
        name2ctx.put(name,ctx);
    }

    private void checkDuplicateName(String name)
    {
        if(name2ctx.containsKey(name))
        {
            throw new IllegalArgumentException("Duplicate handler name.");
        }
    }

    private DefaultChannelHandlerContext getContextOrDie(String name)
    {
        DefaultChannelHandlerContext ctx = (DefaultChannelHandlerContext)getContext(name);
        if(ctx == null)
        {
            throw new NoSuchElementException(name);
        }
        else
        {
            return ctx;
        }
    }

    private DefaultChannelHandlerContext getContextOrDie(ChannelHandler handler)
    {
        DefaultChannelHandlerContext ctx = (DefaultChannelHandlerContext)getContext(handler);
        if(ctx == null)
        {
            throw new NoSuchElementException(handler.getClass().getName());
        }
        else
        {
            return ctx;
        }
    }

    private DefaultChannelHandlerContext getContextOrDie(Class<? extends ChannelHandler> handlerType) {
        DefaultChannelHandlerContext ctx = (DefaultChannelHandlerContext) getContext(handlerType);
        if (ctx == null) {
            throw new NoSuchElementException(handlerType.getName());
        } else {
            return ctx;
        }
    }

    @Override
    public synchronized void addLast(String name, ChannelHandler handler) {
        if (name2ctx.isEmpty()) {
            init(name, handler);
        } else {
            checkDuplicateName(name);
            DefaultChannelHandlerContext oldTail = tail;
            DefaultChannelHandlerContext newTail = new DefaultChannelHandlerContext(oldTail, null, name, handler);
            oldTail.next = newTail;
            tail = newTail;
            name2ctx.put(name, newTail);
        }
    }

    @Override
    public synchronized void addBefore(String baseName, String name, ChannelHandler handler) {
        DefaultChannelHandlerContext ctx = getContextOrDie(baseName);
        if (ctx == head) {
            addFirst(name, handler);
        } else {
            checkDuplicateName(name);
            DefaultChannelHandlerContext newCtx = new DefaultChannelHandlerContext(ctx.prev, ctx, name, handler);
            ctx.prev.next = newCtx;
            ctx.prev = newCtx;
            name2ctx.put(name, newCtx);
        }
    }

    @Override
    public synchronized void addAfter(String baseName, String name, ChannelHandler handler) {
        DefaultChannelHandlerContext ctx = getContextOrDie(baseName);
        if (ctx == tail) {
            addLast(name, handler);
        } else {
            checkDuplicateName(name);
            DefaultChannelHandlerContext newCtx = new DefaultChannelHandlerContext(ctx, ctx.next, name, handler);
            ctx.next.prev = newCtx;
            ctx.next = newCtx;
            name2ctx.put(name, newCtx);
        }
    }

    @Override
    public synchronized void remove(ChannelHandler handler) {
        remove(getContextOrDie(handler));
    }

    @Override
    public synchronized ChannelHandler remove(String name) {
        return remove(getContextOrDie(name)).getHandler();
    }

    @Override
    public synchronized <T extends ChannelHandler> T remove(Class<T> handlerType) {
        return (T)remove(getContextOrDie(handlerType)).getHandler();
    }

    private DefaultChannelHandlerContext remove(DefaultChannelHandlerContext ctx)
    {
        if(head == tail)
        {
            head = tail = null;
            name2ctx.clear();
        }else if(ctx == head)
        {
            removeFirst();
        }else if(ctx == tail)
        {
            removeLast();
        }else {
            DefaultChannelHandlerContext prev = ctx.prev;
            DefaultChannelHandlerContext next = ctx.next;
            prev.next = next;
            prev.prev = prev;
            name2ctx.remove(ctx.getName());
        }
        return ctx;
    }

    @Override
    public synchronized ChannelHandler removeFirst() {
        if(name2ctx.isEmpty())
        {
            throw new NoSuchElementException();
        }

        DefaultChannelHandlerContext oldHead = head;
        oldHead.next.prev = null;
        head = oldHead.next;
        name2ctx.remove(oldHead.getName());
        return oldHead.getHandler();
    }

    @Override
    public ChannelHandler removeLast() {
        if (name2ctx.isEmpty())
        {
            throw new NoSuchElementException();
        }

        DefaultChannelHandlerContext oldTail = tail;
        oldTail.prev.next = null;
        tail = oldTail.prev;
        name2ctx.remove(oldTail.getName());
        return oldTail.getHandler();
    }

    @Override
    public synchronized void replace(ChannelHandler oldHandler, String newName, ChannelHandler newHandler) {
        replace(getContextOrDie(oldHandler),newName,newHandler);
    }

    @Override
    public synchronized ChannelHandler replace(String oldName, String newName, ChannelHandler newHandler) {
       return replace(getContextOrDie(oldName),newName,newHandler);
    }

    @Override
    public <T extends ChannelHandler> T replace(Class<T> oldHandlerType, String newName, ChannelHandler newHandler) {
        return (T)replace(getContextOrDie(oldHandlerType),newName,newHandler);
    }

    private ChannelHandler replace(DefaultChannelHandlerContext ctx, String newName, ChannelHandler newHandler)
    {
        if(ctx == head)
        {
            removeFirst();
            addFirst(newName,newHandler);
        }
        else if(ctx == tail)
        {
            removeLast();
            addLast(newName,newHandler);
        }else {
            boolean sameName = ctx.getHandler().equals(newName);
            if (!sameName)
            {
                checkDuplicateName(newName);
            }
            DefaultChannelHandlerContext prev = ctx.prev;
            DefaultChannelHandlerContext next = ctx.next;
            DefaultChannelHandlerContext newCtx = new DefaultChannelHandlerContext(prev, next, newName, newHandler);
            prev.next = newCtx;
            newCtx.prev = newCtx;
            if(!sameName)
            {
                name2ctx.remove(ctx.getName());
                name2ctx.put(newName,newCtx);
            }
        }
        return ctx.getHandler();
    }

    @Override
    public ChannelHandler getFirst() {
        return head.getHandler();
    }

    @Override
    public ChannelHandler getLast() {
        return tail.getHandler();
    }

    @Override
    public synchronized ChannelHandler get(String name) {
        DefaultChannelHandlerContext ctx = name2ctx.get(name);
        if(ctx == null)
        {
            return null;
        }
        else {
            return ctx.getHandler();
        }
    }

    @Override
    public synchronized  <T extends ChannelHandler> T get(Class<T> handlerType) {
        ChannelHandlerContext ctx = getContext(handlerType);
        if (ctx == null)
        {
            return null;
        }
        else
        {
            return (T) ctx.getHandler();
        }
    }

    @Override
    public synchronized ChannelHandlerContext getContext(ChannelHandler handler) {
        if(handler == null)
        {
            throw new NullPointerException("handler");
        }
        if (name2ctx.isEmpty())
        {
            return null;
        }
        DefaultChannelHandlerContext ctx = head;
        for(;;)
        {
            if(ctx.getHandler() == handler)
            {
                return ctx;
            }
            ctx = ctx.next;
            if(ctx == null)
            {
                break;
            }
        }
        return null;
    }

    @Override
    public ChannelHandlerContext getContext(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        return name2ctx.get(name);
    }

    @Override
    public ChannelHandlerContext getContext(Class<? extends ChannelHandler> handlerType) {
        if (name2ctx.isEmpty()) {
            return null;
        }
        DefaultChannelHandlerContext ctx = head;
        for (;;) {
            if (handlerType.isAssignableFrom(ctx.getHandler().getClass())) {
                return ctx;
            }

            ctx = ctx.next;
            if (ctx == null) {
                break;
            }
        }
        return null;
    }

    @Override
    public void sendUpstream(ChannelEvent e) {
        DefaultChannelHandlerContext head = getActualUpstreamContext(this.head);
        if (head == null)
        {
            logger.warn(
                    "The pipeline contains no upstream handlers; discarding: " + e
            );
        }
        sendUpstream(head,e);
    }

    @Override
    public void sendDownstream(ChannelEvent e) {
        DefaultChannelHandlerContext tail = getActualDownstreamContext(this.tail);
        if (tail == null)
        {
            try {
                getSink().eventSunk(this,e);
                return;
            }catch (Throwable t)
            {
                notifyException(e,t);
            }
        }
        sendDownstream(tail,e);
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public ChannelSink getSink() {
        ChannelSink sink = this.sink;
        if (sink == null)
        {
            return discardingSink;
        }
        return sink;
    }

    @Override
    public void attach(Channel channel, ChannelSink sink) {
        if (channel == null)
        {
            throw new NullPointerException("channel");
        }
        if (sink == null)
        {
            throw new NullPointerException("sink");
        }
        if(this.channel != null || this.sink != null)
        {
            throw new IllegalArgumentException("attached already");
        }

        this.channel = channel;
        this.sink = sink;
    }

    @Override
    public Map<String, ChannelHandler> toMap() {
        Map<String, ChannelHandler> map = new LinkedHashMap<>();
        if(name2ctx.isEmpty())
        {
            return map;
        }

        DefaultChannelHandlerContext ctx = head;
        for(;;)
        {
            map.put(ctx.getName(),ctx.getHandler());
            ctx = ctx.next;
            if(ctx == null)
            {
                break;
            }
        }
        return map;
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
