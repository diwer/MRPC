package cn.whatisee.netty.example;

import cn.whatisee.common.utils.StringUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.PortUnreachableException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by ming on 2017/3/9.
 */
public class TimeClient {
    public void connect(int port, String host) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group);
            b.channel(NioSocketChannel.class);
            b.handler(new TimeClientChannelInitializer());


            ChannelFuture future = b.connect(host, port).sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        int port = 8080;

        Runnable runnable = new TimeRunable();
        for (int i = 0; i < 1000; i++) {
            Thread t = new Thread(runnable);
            t.start();
        }

    }



    private class TimeClientChannelInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new TimeClientHandler());
        }
    }
}
 class TimeRunable implements Runnable {

    @Override
    public void run() {
        new TimeClient().connect(8080, "127.0.0.1");
    }
}
class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private final ByteBuf message;

    public TimeClientHandler() {
        byte[] req = "QueryTime".getBytes();
        message = Unpooled.buffer(req.length);
        message.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("Now is:" + body);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
