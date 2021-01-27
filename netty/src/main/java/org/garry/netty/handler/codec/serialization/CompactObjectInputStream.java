package org.garry.netty.handler.codec.serialization;

import java.io.*;

class CompactObjectInputStream extends ObjectInputStream {

    private final ClassLoader classLoader;

    CompactObjectInputStream(InputStream in) throws IOException {
        this(in,Thread.currentThread().getContextClassLoader());
    }

    CompactObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
        super(in);
        this.classLoader = classLoader;
    }

    @Override
    protected void readStreamHeader() throws IOException {

        // todo ???
        int version = readByte() & 0xFF;
        if(version != STREAM_VERSION)
        {
            throw new StreamCorruptedException("Unsupported version: " + version);
        }
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        int type = read();
        if(type < 0)
        {
            throw new EOFException();
        }

        switch (type)
        {

        }

        return null;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String name = desc.getName();
        try {
            return Class.forName(name, false, classLoader);
        }catch (ClassNotFoundException ex)
        {
            return super.resolveClass(desc);
        }
    }
}
