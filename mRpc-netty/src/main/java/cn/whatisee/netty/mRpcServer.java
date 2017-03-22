package cn.whatisee.netty;

import cn.whatisee.netty.codec.mRpcMessageDecoder;
import cn.whatisee.netty.codec.mRpcMessageEncoder;
import cn.whatisee.netty.handler.HeartBeatRespHandler;
import cn.whatisee.netty.handler.LoginAuthRespHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import javax.jws.soap.SOAPBinding;

/**
 * Created by ming on 2017/3/20.
 */
public class mRpcServer {
    public void bind() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 100);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new mRpcMessageDecoder(1024 * 1024, 4, 4));
                socketChannel.pipeline().addLast("mRpcMessageEncoder", new mRpcMessageEncoder());
                socketChannel.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                socketChannel.pipeline().addLast("loginAuthRespHandler", new LoginAuthRespHandler());
                socketChannel.pipeline().addLast("hertBeatHandler", new HeartBeatRespHandler());
            }
        });
        bootstrap.bind(mRpcConstant.REMOTE_IP, mRpcConstant.PORT).sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new mRpcServer().bind();
    }
}
