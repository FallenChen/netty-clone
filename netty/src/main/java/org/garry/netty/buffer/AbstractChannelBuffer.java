package org.garry.netty.buffer;

public abstract class AbstractChannelBuffer implements ChannelBuffer{

    private int readerIndex;
    private int writerIndex;
    private int markedReaderIndex;
    private int markedWriterIndex;

    public int readerIndex()
    {
        return readerIndex;
    }

    public void readerIndex(int readerIndex)
    {
        if (readerIndex < 0 || readerIndex > writerIndex)
        {
            throw new IndexOutOfBoundsException();
        }
        this.readerIndex = readerIndex;
    }

    public int writerIndex()
    {
        return writerIndex;
    }

    public void writerIndex(int writerIndex)
    {
        if(writerIndex < readerIndex || writerIndex > capacity())
        {
            throw new IndexOutOfBoundsException();
        }
        this.writerIndex = writerIndex;
    }

    @Override
    public void setIndex(int readerIndex, int writeIndex) {
        if (readerIndex < 0 || readerIndex > writeIndex || writeIndex > capacity())
        {
            throw new IndexOutOfBoundsException();
        }
        this.writerIndex = writeIndex;
        this.readerIndex = readerIndex;
    }

    @Override
    public void clear() {
        readerIndex = writerIndex = 0;
    }

    @Override
    public boolean readable() {
       return readableBytes() > 0;
    }

    @Override
    public boolean writable() {
       return writableBytes() > 0;
    }

    @Override
    public int readableBytes() {
       return writerIndex - readerIndex;
    }

    @Override
    public int writableBytes() {
       return capacity() - writerIndex;
    }

    @Override
    public void markReaderIndex() {
        markedReaderIndex = readerIndex;
    }

    @Override
    public void resetReaderIndex() {
        readerIndex(markedReaderIndex);
    }

    @Override
    public void markWriterIndex() {
        markedWriterIndex = writerIndex;
    }

    @Override
    public void resetWriterIndex() {
        writerIndex = markedWriterIndex;
    }
}
