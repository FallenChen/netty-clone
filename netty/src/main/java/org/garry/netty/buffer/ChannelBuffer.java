package org.garry.netty.buffer;


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

}
