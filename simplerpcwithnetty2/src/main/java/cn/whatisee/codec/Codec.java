package cn.whatisee.codec;

import cn.whatisee.SimpleMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by ming on 2017/5/2.
 */
public interface Codec {
    Object decode(ByteBuf inBuf) throws IOException;

    void encode(Object mRpcMessage, ByteBuf outBuf) throws IOException;
}
