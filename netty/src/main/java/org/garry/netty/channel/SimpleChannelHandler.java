package org.garry.netty.channel;

public class SimpleChannelHandler implements ChannelUpstreamHandler {

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) {

        if(e instanceof ChannelStateEvent)
        {
            ChannelStateEvent evt = (ChannelStateEvent) e;
            switch (evt.getState())
            {
                case OPEN:
                    if(Boolean.TRUE.equals(evt.getValue()))
                    {
                        channelOpen(ctx,evt);
                    }else
                    {
                        channelClosed(ctx,evt);
                    }
                    break;
                case BOUND:
                    if(evt.getValue() != null)
                    {
                        channelBound(ctx,evt);
                    }else
                    {
                        channelUnbound(ctx,evt);
                    }
                    break;
                case CONNECTED:
                    if(evt.getValue() != null)
                    {
                        channelConnected(ctx,evt);
                    }else
                    {
                        channelDisconnected(ctx,evt);
                    }
                   break;
                case INTEREST_OPS:
                    channelInterestChanged(ctx,evt);
                    break;
            }
        }
    }

    public void channelBound(ChannelHandlerContext ctx, ChannelStateEvent e)
    {
        ctx.sendUpstream(e);
    }

    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
    {
        ctx.sendUpstream(e);
    }

    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
    {
        ctx.sendUpstream(e);
    }

    public void channelUnbound(ChannelHandlerContext ctx, ChannelStateEvent e)
    {
        ctx.sendUpstream(e);
    }

    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
    {
        ctx.sendUpstream(e);
    }

    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
    {
        ctx.sendUpstream(e);
    }

    public void channelInterestChanged(ChannelHandlerContext ctx, ChannelStateEvent e)
    {
        ctx.sendUpstream(e);
    }
}
