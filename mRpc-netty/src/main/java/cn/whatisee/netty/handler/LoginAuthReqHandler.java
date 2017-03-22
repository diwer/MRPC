package cn.whatisee.netty.handler;

import cn.whatisee.netty.MessageType;
import cn.whatisee.netty.mRpcMessage;
import cn.whatisee.netty.mRpcMessageHeader;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ming on 2017/3/19.
 */
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();
    private String[] whiteList = {"127.0.0.1"};

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        mRpcMessage message = (mRpcMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            byte loginResult = (byte) message.getBody();
            if (loginResult != (byte) 0) {
                ctx.close();
            } else {
                System.out.println("Login is  ok: " + message);
                ctx.fireChannelRead(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private mRpcMessage buildLoginReq() {
        mRpcMessage message = new mRpcMessage();
        mRpcMessageHeader header = new mRpcMessageHeader();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        return message;
    }


}
