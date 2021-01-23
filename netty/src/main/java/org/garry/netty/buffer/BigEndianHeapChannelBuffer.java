package org.garry.netty.buffer;

public class BigEndianHeapChannelBuffer extends HeapChannelBuffer{

    public BigEndianHeapChannelBuffer(int length) {
        super(length);
    }

    public BigEndianHeapChannelBuffer(byte[] array) {
        super(array);
    }

    public BigEndianHeapChannelBuffer(byte[] array, int readerIndex, int writerIndex) {
        super(array, readerIndex, writerIndex);
    }
}
