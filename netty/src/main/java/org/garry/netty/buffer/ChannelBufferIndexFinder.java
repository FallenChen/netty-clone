package org.garry.netty.buffer;

public interface ChannelBufferIndexFinder {

    boolean find(ChannelBuffer buffer, int guessedIndex);
}
