package org.garry.netty.buffer;

public abstract class HeapChannelBuffer extends AbstractChannelBuffer{

    protected final byte[] array;

    public HeapChannelBuffer(int length)
    {
        this(new byte[length],0,0);
    }

    public HeapChannelBuffer(byte[] array)
    {
        this(array,0,array.length);
    }

    protected HeapChannelBuffer(byte[] array,int readerIndex,int writerIndex)
    {
        if(array == null)
        {
            throw new NullPointerException("array");
        }
        this.array = array;
        setIndex(readerIndex,writerIndex);
    }


}
