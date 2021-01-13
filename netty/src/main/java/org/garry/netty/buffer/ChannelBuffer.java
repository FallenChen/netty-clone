package org.garry.netty.buffer;


import java.nio.ByteBuffer;

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
 *
 * <h4>Readable bytes (the actual 'content' of the buffer)</h4>
 *
 * This segment, so called 'the <strong>content</strong> of a buffer',is where
 * the actual data is stored. Any operation whose name starts with
 * {@code read}or {@code skip}will get or skip the data at the current
 * {@link #readerIndex() readerIndex} and increase it by the number of read
 * bytes.If the arguments of the read operation is also a {@link ChannelBuffer}
 * and no start index is specified, the specified buffer's {@link #readerIndex()}
 * is increased together
 *
 * It there's not enough content left, {@link IndexOutOfBoundsException} is
 * raised. The default value of newly allocated, wrapped or copied buffer's
 * {@link #readerIndex() readerIndex} is {@code 0}
 *
 * <pre>
 *     // Iterates the readable bytes of a buffer
 *     ChannelBuffer buffer = ...;
 *     while(buffer.readable()) {
 *         System.out.println(buffer.readByte());
 *     }
 * </pre>
 *
 * <h4>Writable space</h4>
 *
 * This segment is a undefined space which needs to be filled. Any operation
 * whose name ends with {@code write} will write the data at the current
 * {@link #writerIndex() writerIndex} and increase it by the number of written
 * bytes. If the argument of the write operation is also a {@link ChannelBuffer},
 * and no start index is specified, the specified buffer's
 * {@link #readerIndex() readerIndex} is increased together.
 *
 * If there's not enough writable space left, {@link IndexOutOfBoundsException}
 * is raised. The default value of newly allocated buffer's
 * {@link #writerIndex() writerIndex} is {@code 0}.The default value of
 * wrapped or copied buffer's {@link #writerIndex() writerIndex} is the
 * {@link #capacity() capacity} of buffer
 *
 * <pre>
 *     // Fills the writable space of a buffer with random integers
 *     ChannelBuffer buffer = ...;
 *     while(buffer.writableBytes() >= 4){
 *         buffer.writeInt(random.nextInt()).
 *     }
 * </pre>
 *
 * <h4>Disacrdable bytes</h4>
 *
 * This segment contains the bytes which were read already by a read operation.
 * Initially, the size of this segment is {@code 0}, but its size increases up
 * to the {@link #writerIndex()} as read operations are executed.
 * The read bytes can be discarded by calling {@link #discardReadBytes()} to
 * reclaim unused area as depicted by the following diagram:
 *
 * <pre>
 *     BEFORE discardReadBytes()
 *      +-------------------+------------------+------------------+
 *      | discardable bytes |  readable bytes  |  writable space  |
 *      |                   |     (CONTENT)    |                  |
 *      +-------------------+------------------+------------------+
 *      |                   |                  |                  |
 *      0      <=      readerIndex   <=   writerIndex    <=    capacity
 *  AFTER discardReadBytes()
 *
 *      +------------------+--------------------------------------+
 *      |  readable bytes  |    writable space (got more space)   |
 *      |     (CONTENT)    |                                      |
 *      +------------------+--------------------------------------+
 *      |                  |                                      |
 * readerIndex (0) <= writerIndex (decreased)        <=        capacity
 * </pre>
 *
 * <h4>Clearing the buffer indexes</h4>
 *
 * You can set both {@link #readerIndex()} and {@link #writerIndex()}
 * to {@code 0} by calling {@link #clear()}.It doesn't clear the buffer content
 * (e.g. filling with {@code 0}) but just clears the two pointers.this operation
 * is different from {@link ByteBuffer#clear()}
 *
 * <pre>
 *  BEFORE clear()
 *
 *      +-------------------+------------------+------------------+
 *      | discardable bytes |  readable bytes  |  writable space  |
 *      |                   |     (CONTENT)    |                  |
 *      +-------------------+------------------+------------------+
 *      |                   |                  |                  |
 *      0      <=      readerIndex   <=   writerIndex    <=    capacity
 *
 *
 *  AFTER clear()
 *
 *      +---------------------------------------------------------+
 *      |             writable space (got more space)             |
 *      +---------------------------------------------------------+
 *      |                                                         |
 *      0 = readerIndex = writerIndex            <=            capacity
 * </pre>
 *
 * <h3>Search operation</h3>
 *
 * Various {@code indexOf()} methods help you locate an index of a value which
 * meets a certain criteria. Complicated dynamic sequential search can be done
 * with {@link ChannelBufferIndexFinder} as well as simple static single byte search
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


    void writeInt(int value);


    /**
     * Discards the bytes between the 0th index and {@code readerIndex}
     * It moves the bytes between {@code readerIndex} and {@code writerIndex}
     * to the 0th index, and sets {@code readerIndex} and {@code writerIndex}
     * to {@code 0} and {@code oldWriterIndex - oldReaderIndex} respectively.
     */
    void discardReadBytes();

    /**
     * Sets the {@code readerIndex} and {@code writerIndex} of this buffer to
     * {@code 0}
     * This method is identical to {@link #setIndex(int,int) setIndex(0,0)}
     */
    void clear();

    /**
     * Sets the {@code readerIndex} and {@code writerIndex} of this buffer
     * in one shot. This method is useful because you don't need to worry
     * about the invocation order of {@link #readerIndex(int)} and {@link #writerIndex(int)}
     * methods. For example, the following code will fail:
     *
     * <pre>
     *     // Create a buffer whose readerIndex, writerIndex and capacity are
     *     // 0,0 and 8 respectively
     *     ChannelBuffer buf = ChannelBuffers.buffer(8);
     *
     *     // IndexOutOfBoundsException is thrown because the specified
     *     // readerIndex (2) cannot be greater than the current writerIndex(0)
     *     buf.readerIndex(2);
     *     buf.writerIndex(4);
     * </pre>
     *
     * The following code will also fail:
     * <pre>
     *     // Create a buffer whose readerIndex, writerIndex and capacity are
     *     // 0,0 and 8 respectively
     *     ChannelBuffer buf = ChannelBuffers.wrappedBuffer(new byte[8]);
     *
     *     // readerIndex becomes 8
     *     buf.readLong();
     *
     *     // IndexOutOfBoundsException is thrown because the specified
     *     // writerIndex (4) cannot be less than the current readerIndex(8)
     *     buf.writerIndex(4);
     *     buf.readerIndex(2);
     *
     * </pre>
     *
     * By contrast, {@link #setIndex(int, int)} guarantees that is never
     * throws an {@link IndexOutOfBoundsException} as long as the specified indexes meets
     * all constraints, regardless what the current index values of the buffer are:
     *
     * <pre>
     *     // No matter what the current state of the buffer is, the following
     *     // call always succeeds as long as the capacity of the buffer is not
     *     // less than 4
     *     buf.setIndex(2,4)
     * </pre>
     *
     * @throws IndexOutOfBoundsException
     *         if the specified {@code readerIndex} is less than 0,
     *         if the specified {@code writerIndex} is less than the specified
     *         {@code readerIndex} or if the specified {@code writerIndex} is
     *         greater than {@code capacity}
     */
    void setIndex(int readerIndex, int writeIndex);

}
