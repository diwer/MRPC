package cn.whatisee.netty.codec;

import cn.whatisee.netty.mRpcMessage;
import cn.whatisee.netty.mRpcMessageHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ming on 2017/3/18.
 */
public class mRpcMessageDecoder extends LengthFieldBasedFrameDecoder {

    mRpcGeneralDecoder mRpcGeneralDecoder;


    public mRpcMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength,-8,0);
        mRpcGeneralDecoder = new mRpcGeneralDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null)
            return null;

        mRpcMessage message = new mRpcMessage();
        mRpcMessageHeader header = new mRpcMessageHeader();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionID(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        int size = frame.readInt();
        if (size > 0) {
            Map<String, Object> attch = new HashMap<>(size);
            int keySize = 0;
            byte[] keyArray = null;
            String key = null;
            for (int i = 0; i < size; i++) {
                keySize = frame.readInt();
                keyArray = new byte[keySize];
                frame.readBytes(keyArray);
                key = new String(keyArray, "utf-8");
                attch.put(key, mRpcGeneralDecoder.decode(in));
            }
            keyArray = null;
            key = null;
            header.setAttachment(attch);
        }
        if (frame.readableBytes() > 4) {
            message.setBody(mRpcGeneralDecoder.decode(frame));
        }
        message.setHeader(header);
        return message;
    }
}
