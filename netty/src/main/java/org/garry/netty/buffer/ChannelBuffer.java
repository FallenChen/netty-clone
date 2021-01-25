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
 *
 * <h3>Mark and reset</h3>
 *
 * There are two marker indexes in every buffer. One is for storing
 * {@link #readerIndex()} and the other is for storing
 * {@link #writerIndex()} . You can always reposition one of the
 * two indexes by calling a reset method. It works in a similar fashion to
 * the mark and reset methods in {@link java.io.InputStream} except that there's no
 * {@code readlimit}
 *
 * <h3>Derived buffers</h3>
 *
 * You can create a view of an existing buffer by calling either
 * {@link #duplicate()},{@link #slice()} or {@link @slice(int,int)}
 * A derived buffer will have an independent {@link #readerIndex()},
 * {@link #writerIndex()} and marked indexes, while it shares
 * other internal data representation, just like a NIO {@link ByteBuffer} does.
 *
 * In case a completely fresh copy of an existing buffer is required,please
 * call {@link #copy()} method instead.
 *
 * <h3>Conversion to existing JDK types</h3>
 *
 * Various {@link #toByteBuffer()} and {@link #toByteBuffers} methods convert
 * a {@link ChannelBuffer} into one or more NIO buffers.These methods avoid
 * buffer allocation and memory copy whenever possible,but there's no
 * guarantee that memory copy will not be involved or that an explicit memory
 * copy will be involved.
 *
 * In case you need to convert a {@link ChannelBuffer} into
 * an {@link java.io.InputStream} or an {@link java.io.OutputStream},please refer to
 * {@link ChannelBufferInputStream} and {@link ChannelBufferOutputStream}
 */
public interface ChannelBuffer extends Comparable<ChannelBuffer> {

    ChannelBuffer EMPTY_BUFFER = new BigEndianHeapChannelBuffer(0);

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

    void readerIndex(int readerIndex);

    /**
     * Returns the {@code writerIndex} of this buffer
     * @return
     */
    int writerIndex();

    /**
     * Sets the {@code writerIndex} of this buffer
     * @param writerIndex
     * @return
     */
    void writerIndex(int writerIndex);


    void writeInt(int value);


    /**
     * Discards the bytes between the 0th index and {@code readerIndex}
     * It moves the bytes between {@code readerIndex} and {@code writerIndex}
     * to the 0th index, and sets {@code readerIndex} and {@code writerIndex}
     * to {@code 0} and {@code oldWriterIndex - oldReaderIndex} respectively.
     */
    void discardReadBytes();

    int getInt(int index);

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

    /**
     * Returns the number of readable bytes which equals to
     * {@code (writerIndex - readerIndex)}
     * @return
     */
    int readableBytes();

    /**
     * Returns the number of writable bytes which equals to
     * {@code capacity - writerIndex}
     * @return
     */
    int writableBytes();

    /**
     * {@link #readableBytes()} greater than {@code 0}
     * @return
     */
    boolean readable();

    /**
     * {@link #writableBytes()} greater than {@code 0}
     * @return
     */
    boolean writable();

    /**
     * Marks the current {@code readerIndex} in this buffer. You can restore
     * the marked {@code readerIndex} by calling {@link #resetReaderIndex()}
     * The initial value of the marked {@code readerIndex} is always {@code 0}
     */
    void markReaderIndex();

    /**
     * Repositions the current {@code readerIndex} to the marked {@code readerIndex}
     * in this buffer
     *
     * @throws  IndexOutOfBoundsException
     *          if the current {@code writerIndex} is less than the marked
     *          {@code readerIndex}
     */
    void resetReaderIndex();

    /**
     * Marks the current {@code writerIndex} in this buffer.  You can restore
     * the marked {@code writerIndex} by calling {@link #resetWriterIndex()}.
     * The initial value of the marked {@code writerIndex} is always {@code 0}.
     */
    void markWriterIndex();

    /**
     * Repositions the current {@code writerIndex} to the marked {@code writerIndex}
     * in this buffer.
     *
     * @throws IndexOutOfBoundsException
     *         if the current {@code readerIndex} is greater than the marked
     *         {@code writerIndex}
     */
    void resetWriterIndex();

    ChannelBuffer copy();

    ChannelBuffer copy(int index,int length);

    ChannelBuffer slice();

    ChannelBuffer slice(int index,int length);

    ChannelBuffer duplicate();

    ByteBuffer toByteBuffer();

    ByteBuffer toByteBuffer(int index, int length);

    ByteBuffer[] toByteBuffers();

    ByteBuffer[] toByteBuffers(int index,int length);

    /**
     * Returns a hash code which was calculated from the content of this
     * buffer.
     * @return
     */
    int hashCode();

    boolean equals();

    int compareTo(ChannelBuffer buffer);

    String toString();

}
