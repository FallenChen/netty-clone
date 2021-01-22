package org.garry.netty.buffer;

import java.nio.ByteOrder;

/**
 * Creates a new {@link ChannelBuffer} by allocating new space or by wrapping
 * or copying existing byte arrays
 *
 * <h3>Use static import</h3>
 * This classes is intended to be used with Java 5 static import statement
 *
 * <pre>
 *     ChannelBuffer heapBuffer = buffer(128);
 *     ChannelBuffer directBuffer = directBuffer(256);
 *     ChannelBuffer dynamicBuffer = dynamicBuffer(512);
 *     ChannelBuffer wrappedBuffer = wrapperBuffer(new byte[128], new byte[256]);
 *     ChannelBuffer copiedBuffer = copiedBuffer(ByteBuffer.allocate(128));
 * </pre>
 *
 * <h3>Allocating a new buffer</h3>
 *
 * Three buffer types are provided out of the box
 *
 * buffer(int) allocates a new fixed-capacity heap buffer.
 * directBuffer(int) allocates a new fixed-capacity direct buffer.
 * dynamicBuffer(int) allocates a new dynamic-capacity heap buffer,whose capacity increases
 * automatically as needed by a write operation.
 *
 * <h3>Creating a wrapped buffer</h3>
 *
 * Wrapped buffer is a buffer which is a view of one or more existing
 * byte arrays or byte buffer.Any changes in the content of the original
 * array or buffer will be reflected in the wrapped buffer. Various wrapper
 * methods are provided and their name is all {@code wrappedBuffer()}.
 * You might want to take a look at the method closely if you want to create
 * a buffer which is composed of more than one array to reduce the number of
 * memory copy
 *
 * <h3>Creating a copied buffer</h3>
 *
 * Copied buffer is a deep copy of one or more existing byte arrays or byte
 * buffer.Unlike a wrapped buffer,there's no shared data between the
 * original arrays and the copied buffer. Various copy methods are provided
 * and their name is all {@code copiedBuffer()}.It's also convenient to
 * use this operation to merge multiple buffers into one buffer.
 *
 * <h3>Miscellaneous utility methods</h3>
 *
 * This class also provides various utility methods to help implementation
 * of a new buffer type,generation of hex dump and swapping an integer's
 * byte order.
 */
public class ChannelBuffers {

    public static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
    public static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;

    private static final char[][] HEXDUMP_TABLE = new char[65536][];


    public static ChannelBuffer buffer(int length)
    {
        return buffer(BIG_ENDIAN,length);
    }

    public static ChannelBuffer buffer(ByteOrder endianness, int length)
    {
        if (length == 0)
        {
            return ChannelBuffer.
        }
    }
}
