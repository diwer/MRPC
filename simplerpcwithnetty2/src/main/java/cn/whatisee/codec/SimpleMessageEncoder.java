package cn.whatisee.codec;

import cn.whatisee.SimpleMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by ming on 2017/3/18.
 */
public class SimpleMessageEncoder extends MessageToMessageEncoder<SimpleMessage> {

    private NettyCodecHander codecHander;

    public SimpleMessageEncoder() throws IOException {
        this.codecHander = new NettyCodecAdapter();
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, SimpleMessage message, List<Object> list) throws Exception {
        if (message == null || message.getHeader() == null)
            throw new Exception("this encode message is null");

        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(message.getHeader().getCrcCode());
        sendBuf.writeInt(message.getHeader().getLength());
        sendBuf.writeLong(message.getHeader().getSessionID());
        sendBuf.writeByte(message.getHeader().getType());
        sendBuf.writeByte(message.getHeader().getPriority());
        sendBuf.writeInt(message.getHeader().getAttachment().size());
        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param : message.getHeader().getAttachment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes("utf-8");
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            codecHander.encode(value, sendBuf);
        }

        key = null;
        keyArray = null;
        value = null;
        if (message.getBody() != null) {
            codecHander.encode(message.getBody(), sendBuf);
        } else {
            sendBuf.writeInt(0);
        }
        sendBuf.setInt(4, sendBuf.readableBytes());// 更新了包的长度字段
        list.add(sendBuf);
    }
}
