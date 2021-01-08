package org.garry.netty;


/**
 * A random and sequential accessible sequence of zero or more bytes (octets)
 * This interface provides an abstract view for one or more primitive byte
 * arrays ({@code byte[]}) and {@linkplain java.nio.ByteBuffer NIO buffers}
 *
 * <h3> Creation of a buffer</h3>
 *
 * It is common for a user to create a new buffer using {@link ChannelBuffer}
 * utility class rather than calling an individual implementation's constructor
 *
 * <h3>Random Access Indexing</h3>
 *
 * Just line an ordinary primitive byte array, {@link ChannelBuffer} uses
 * zero-based indexing.It means the index of the first byte is always 0 and the
 * index of the last byte is always capacity -1 .For example, to iterate
 * all bytes of a buffer,you can do the following,regardless of its internal implementation:
 *
 * ChannelBuffer buffer = ...;
 * for (int i=0; i < buffer.capacity(); i++)
 * {
 *     byte b = array.getByte(i);
 *     System.out.println((char)b);
 * }
 *
 * <h3>Sequential Access Indexing</h3>
 *
 * ChannelBuffer provides two pointer variables to support sequential
 * read and write operation - {@link #readerIndex() readerIndex} for a read
 * operation and {@link #writerIndex} for a write operation respectively.
 * The following diagram shows how a buffer is segmented into three areas by the two pointers:
 *
 * <pre>
 *     +-------------------+------------------+------------------+
 *     | discardable bytes |  readable bytes  |   writable space |
 *     |                   |     (CONTENT)    |                  |
 *     +-------------------+------------------+------------------+
 *     |                   |                  |                  |
 *     0         <=    readerIndex  <=     writerIndex   <=    capacity
 * </pre>
 */
public interface ChannelBuffer extends Comparable<ChannelBuffer> {

    /**
     * Returns the numbers of bytes (octets) this buffer can contain
     * @return
     */
    int capacity();

    /**
     * Returns the {@code readerIndex} of this buffer
     * @return
     */
    int readerIndex();

    /**
     * Returns the {@code writerIndex} of this buffer
     * @return
     */
    int writerIndex();

}
