package cn.whatisee.netty.codec;

import cn.whatisee.netty.mRpcMessage;
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
public class mRpcMessageEncoder extends MessageToMessageEncoder<mRpcMessage> {


    cn.whatisee.netty.codec.mRpcGeneralEncoder mRpcGeneralEncoder;

    public mRpcMessageEncoder() throws IOException {
        this.mRpcGeneralEncoder = new mRpcGeneralEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, mRpcMessage mRpcMessage, List<Object> list) throws Exception {
        if (mRpcMessage == null || mRpcMessage.getHeader() == null)
            throw new Exception("this encode message is null");

        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(mRpcMessage.getHeader().getCrcCode());
        sendBuf.writeInt(mRpcMessage.getHeader().getLength());
        sendBuf.writeLong(mRpcMessage.getHeader().getSessionID());
        sendBuf.writeByte(mRpcMessage.getHeader().getType());
        sendBuf.writeByte(mRpcMessage.getHeader().getPriority());
        sendBuf.writeInt(mRpcMessage.getHeader().getAttachment().size());
        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param : mRpcMessage.getHeader().getAttachment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes("utf-8");
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            mRpcGeneralEncoder.encode(value, sendBuf);
        }

        key = null;
        keyArray = null;
        value = null;
        if (mRpcMessage.getBody() != null) {
            mRpcGeneralEncoder.encode(mRpcMessage.getBody(), sendBuf);
        } else {
            sendBuf.writeInt(0);
        }
        sendBuf.setInt(4, sendBuf.readableBytes());// 更新了包的长度字段
        list.add(sendBuf);
    }
}
