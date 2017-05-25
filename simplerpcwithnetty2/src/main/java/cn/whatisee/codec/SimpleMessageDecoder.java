package cn.whatisee.codec;

import cn.whatisee.SimpleMessage;
import cn.whatisee.SimpleMessageHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ming on 2017/3/18.
 */
public class SimpleMessageDecoder extends LengthFieldBasedFrameDecoder {

    NettyCodecHander codecHander;


    public SimpleMessageDecoder() throws IOException {
        super(1024*1024*4, 4, 4, -8, 0);
        codecHander = new NettyCodecAdapter();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null)
            return null;

        SimpleMessage message = new SimpleMessage();
        SimpleMessageHeader header = new SimpleMessageHeader();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionID(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        int size = frame.readInt();
        if (size > 0) {
            Map<String, Object> attch = new HashMap<String, Object>(size);
            int keySize = 0;
            byte[] keyArray = null;
            String key = null;
            for (int i = 0; i < size; i++) {
                keySize = frame.readInt();
                keyArray = new byte[keySize];
                frame.readBytes(keyArray);
                key = new String(keyArray, "utf-8");
                attch.put(key, codecHander.decode(in));
            }
            keyArray = null;
            key = null;
            header.setAttachment(attch);
        }
        if (frame.readableBytes() > 4) {
            message.setBody(codecHander.decode(frame));
        }
        message.setHeader(header);
        return message;
    }
}
