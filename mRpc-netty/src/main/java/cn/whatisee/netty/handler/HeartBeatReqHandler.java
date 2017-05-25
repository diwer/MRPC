package cn.whatisee.netty.handler;

import cn.whatisee.netty.MessageType;
import cn.whatisee.netty.mRpcMessage;
import cn.whatisee.netty.mRpcMessageHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * Created by ming on 2017/3/19.
 */
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    private volatile ScheduledFuture<?> heartBeat;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        mRpcMessage message = (mRpcMessage) msg;

        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx), 0, 5000, TimeUnit.MICROSECONDS);
        } else if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {

            System.out.println("Client receive server heart  beat message :---> " + message);

        } else {
            ctx.fireChannelRead(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }



        public void run() {
            mRpcMessage heatBeat = buildHeatBeat();
            System.out.println("Client send heart beat message to server :---->" + heatBeat);
            ctx.writeAndFlush(heatBeat);
        }

        private mRpcMessage buildHeatBeat() {
            mRpcMessage message = new mRpcMessage();
            mRpcMessageHeader header = new mRpcMessageHeader();
            header.setType(MessageType.HEARTBEAT_REQ.value());
            message.setHeader(header);
            return message;
        }

    }
}
