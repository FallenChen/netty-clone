package org.garry.netty.channel.socket;

import org.garry.netty.channel.ChannelConfig;

public interface SocketChannelConfig extends ChannelConfig {

    boolean itTcpNoDelay();

    void setTcpNoDelay(boolean tcpNoDelay);
    // todo ???
    int getSoLinger();

    void setSoLinger(int soLinger);

    int getSendBufferSize();

    void setSendBufferSize(int sendBufferSize);

    int getReceiveBufferSize();

    void setReceiveBufferSize(int receiveBufferSize);

    boolean isKeepAlive();

    void setKeepAlive(boolean keepAlive);

    int getTrafficClass();

    void setTrafficClass(int trafficClass);

    boolean isReuseAddress();

    void setReuseAddress(boolean reuseAddress);

    void setPerformancePreferences(int connectionTime, int latency,int bandwidth);
}
