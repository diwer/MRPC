package cn.whatisee.codec;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by ming on 2017/5/2.
 */
public interface NettyCodecHander {

    Object decode(ByteBuf inBuf) throws IOException;

    void encode(Object Message, ByteBuf outBuf) throws IOException;
}
