package cn.whatisee.netty.codec;

import com.sun.jmx.remote.internal.Unmarshal;
import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * Created by ming on 2017/3/18.
 */
public class mRpcGeneralDecoder {
    private final Unmarshaller unmarshaller;

    public mRpcGeneralDecoder() throws IOException {
        unmarshaller = MarshallingCodeCFactory.buildUnMarshalling();
    }

    protected Object decode(ByteBuf in) throws IOException {
        Object obj = null;
        int objectSize = in.readInt();

        ByteBuf buf = in.slice(in.readerIndex(), objectSize);

        ByteInput input = new ChannelBufferByteInput(buf);
        try {
            unmarshaller.start(input);
            try {
                obj = unmarshaller.readObject();
            } catch (ClassNotFoundException e) {

                obj = null;
            }
            unmarshaller.finish();
            in.readerIndex(in.readerIndex() + objectSize);
            return obj;
        } finally {
            unmarshaller.close();
        }
    }
}
