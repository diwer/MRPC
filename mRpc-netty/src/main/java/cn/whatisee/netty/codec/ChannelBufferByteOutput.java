package cn.whatisee.netty.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

/**
 * Created by ming on 2017/3/18.
 */
public class ChannelBufferByteOutput implements ByteOutput {
    private final ByteBuf buffer;

    public static ChannelBufferByteOutput CreateInstance(ByteBuf buffer) {
        return new ChannelBufferByteOutput(buffer);
    }

    ChannelBufferByteOutput(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public void close() throws IOException {
    }

    public void flush() throws IOException {
    }

    public void write(int b) throws IOException {
        this.buffer.writeByte(b);
    }

    public void write(byte[] bytes) throws IOException {
        this.buffer.writeBytes(bytes);
    }

    public void write(byte[] bytes, int srcIndex, int length) throws IOException {
        this.buffer.writeBytes(bytes, srcIndex, length);
    }

    ByteBuf getBuffer() {
        return this.buffer;
    }
}
