package cn.whatisee.netty.handler;

import cn.whatisee.netty.MessageType;
import cn.whatisee.netty.mRpcMessage;
import cn.whatisee.netty.mRpcMessageHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by ming on 2017/3/19.
 */
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        mRpcMessage message = (mRpcMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
            System.out.println("Receive client heart beat message:----->" + message);
            mRpcMessage heartBeat = buildHeatBeat();
            System.out.println("Send heart beat response message to client: -----> " + heartBeat);
            ctx.writeAndFlush(heartBeat);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private mRpcMessage buildHeatBeat() {
        mRpcMessage message = new mRpcMessage();
        mRpcMessageHeader header = new mRpcMessageHeader();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }
}
