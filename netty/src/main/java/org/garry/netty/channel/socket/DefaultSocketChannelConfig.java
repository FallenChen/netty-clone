package org.garry.netty.channel.socket;

import org.garry.netty.channel.ChannelException;
import org.garry.netty.channel.ChannelPipelineFactory;
import org.garry.netty.util.ConvertUtil;

import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

public class DefaultSocketChannelConfig implements SocketChannelConfig{

    private final Socket socket;
    public volatile int connectTimeoutMillis = 10000;// 10 seconds

    public DefaultSocketChannelConfig(Socket socket) {
        if(socket == null)
        {
            throw new NullPointerException("socket");
        }
        this.socket = socket;
    }

    @Override
    public void setOptions(Map<String, Object> options) {
        for(Map.Entry<String,Object> e :options.entrySet())
        {
            setOption(e.getKey(),e.getValue());
        }
    }

    protected boolean setOption(String key,Object value) {
        if (key.equals("receiveBufferSize")) {
            setReceiveBufferSize(ConvertUtil.toInt(value));
        } else if (key.equals("sendBufferSize")) {
            setSendBufferSize(ConvertUtil.toInt(value));
        } else if (key.equals("tcpNoDelay")) {
            setTcpNoDelay(ConvertUtil.toBoolean(value));
        } else if (key.equals("keepAlive")) {
            setKeepAlive(ConvertUtil.toBoolean(value));
        } else if (key.equals("soLinger")) {
            setSoLinger(ConvertUtil.toInt(value));
        } else if (key.equals("reuseAddress")) {
            setReuseAddress(ConvertUtil.toBoolean(value));
        } else if (key.equals("trafficClass")) {
            setTrafficClass(ConvertUtil.toInt(value));
        } else if (key.equals("writeTimeoutMillis")) {
            setWriteTimeoutMillis(ConvertUtil.toInt(value));
        }else if(key.equals("connectTimeoutMillis"))
        {
            setConnectTimeoutMillis(ConvertUtil.toInt(value));
        }else if(key.equals("pipelineFactory"))
        {
            setPipelineFactory((ChannelPipelineFactory) value);
        }else {
            return false;
        }
        return true;
    }

    @Override
    public int getReceiveBufferSize() {

        try {
            return socket.getReceiveBufferSize();
        } catch (SocketException e) {
            throw new ChannelException(e);
        }
    }

    @Override
    public int getSendBufferSize() {
        try {
            return socket.getSendBufferSize();
        } catch (SocketException e) {
            throw new ChannelException(e);
        }
    }

    @Override
    public int getSoLinger() {
        try {
            return socket.getSoLinger();
        } catch (SocketException e) {
           throw new ChannelException(e);
        }
    }

    @Override
    public int getTrafficClass() {
        try {
            return socket.getTrafficClass();
        } catch (SocketException e) {
           throw new ChannelException(e);
        }
    }

    @Override
    public boolean isKeepAlive() {
        try {
            return socket.getKeepAlive();
        } catch (SocketException e) {
           throw new ChannelException(e);
        }
    }

    @Override
    public boolean isReuseAddress() {
        try {
            return socket.getReuseAddress();
        } catch (SocketException e) {
           throw new ChannelException(e);
        }
    }

    @Override
    public boolean isTcpNoDelay() {

        try {
            return socket.getTcpNoDelay();
        } catch (SocketException e) {
           throw new ChannelException(e);
        }
    }

    @Override
    public void setKeepAlive(boolean keepAlive) {
        try {
            socket.setKeepAlive(keepAlive);
        } catch (SocketException e) {
           throw new ChannelException(e);
        }
    }

    @Override
    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        socket.setPerformancePreferences(connectionTime,latency,bandwidth);
    }

    @Override
    public void setReceiveBufferSize(int receiveBufferSize) {
        try {
            socket.setReceiveBufferSize(receiveBufferSize);
        } catch (SocketException e) {
           throw new ChannelException(e);
        }
    }

    @Override
    public void setReuseAddress(boolean reuseAddress) {
        try {
            socket.setReuseAddress(reuseAddress);
        } catch (SocketException e) {
           throw new ChannelException(e);
        }
    }

    @Override
    public void setSendBufferSize(int sendBufferSize) {
        try {
            socket.setSendBufferSize(sendBufferSize);
        } catch (SocketException e) {
            throw new ChannelException(e);
        }
    }

    @Override
    public void setSoLinger(int soLinger) {
        try {
            if(soLinger < 0)
            {
                socket.setSoLinger(false,0);
            }
            else
            {
                socket.setSoLinger(true,soLinger);
            }
        }catch (SocketException e)
        {
            throw new ChannelException(e);
        }
    }

    @Override
    public void setTcpNoDelay(boolean tcpNoDelay) {
        try {
            socket.setTcpNoDelay(tcpNoDelay);
        } catch (SocketException e) {
           throw new ChannelException(e);
        }
    }

    @Override
    public void setTrafficClass(int trafficClass) {
        try {
            socket.setTrafficClass(trafficClass);
        } catch (SocketException e) {
           throw new ChannelException(e);
        }
    }

    @Override
    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    @Override
    public ChannelPipelineFactory getPipelineFactory() {
        return null;
    }

    @Override
    public int getWriteTimeoutMillis() {
        return 0;
    }

    @Override
    public void setConnectTimeoutMillis(int connectTimeoutMills) {
        if(connectTimeoutMills < 0)
        {
            throw new IllegalArgumentException("connectTimeoutMillis: " + connectTimeoutMills);
        }
        this.connectTimeoutMillis = connectTimeoutMills;
    }

    @Override
    public void setPipelineFactory(ChannelPipelineFactory pipelineFactory) {
        // Unused
    }

    @Override
    public void setWriteTimeoutMillis(int writeTimeoutMillis) {
        // Unused
    }
}
