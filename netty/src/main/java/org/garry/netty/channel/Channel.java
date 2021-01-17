package org.garry.netty.channel;

/**
 * similar {@link java.nio.channels.SelectionKey}
 */
public interface Channel {

    int OP_NONE = 0;
    int OP_READ = 1;
    int OP_WRITE = 4;
    int OP_READ_WRITE = OP_READ | OP_WRITE;


}
