package cn.whatisee.codec;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by ming on 2017/5/2.
 */
public class SimpleCodec implements Codec {

    private mRpcGeneralDecoder decoder;
    private mRpcGeneralEncoder encoder;

    public Object decode(ByteBuf inBuf) throws IOException {
            return decoder.decode(inBuf);
    }

    public void encode(Object mRpcMessage, ByteBuf outBuf) throws IOException {
        encoder.encode(mRpcMessage, outBuf);
    }
}
