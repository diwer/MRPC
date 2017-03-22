package cn.whatisee.netty.handler;

import cn.whatisee.netty.MessageType;
import cn.whatisee.netty.mRpcMessage;
import cn.whatisee.netty.mRpcMessageHeader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ming on 2017/3/19.
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();
    private String[] whiteList = {"127.0.0.1"};


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        mRpcMessage message = (mRpcMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
            String nodeIndex = ctx.channel().remoteAddress().toString();
            mRpcMessage loginResp = null;
            if (nodeCheck.containsKey(nodeIndex)) {
                loginResp = buildResponse((byte) -1);
            } else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOk = false;
                for (String wip : whiteList) {
                    if (wip.equals(ip)) {
                        isOk = true;
                        break;

                    }
                }
                loginResp = isOk ? buildResponse((byte) 0) : buildResponse((byte) -1);
                if (isOk)
                    nodeCheck.put(nodeIndex, true);
                System.out.println("the login response is :" + loginResp + " body {" + loginResp.getBody() + "}");
                ctx.writeAndFlush(loginResp);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private mRpcMessage buildResponse(byte result) {
        mRpcMessage message = new mRpcMessage();
        mRpcMessageHeader header = new mRpcMessageHeader();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }
}
