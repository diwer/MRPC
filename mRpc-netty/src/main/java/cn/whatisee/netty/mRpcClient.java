package cn.whatisee.netty;

import cn.whatisee.netty.codec.mRpcMessageDecoder;
import cn.whatisee.netty.codec.mRpcMessageEncoder;
import cn.whatisee.netty.handler.HeartBeatReqHandler;
import cn.whatisee.netty.handler.LoginAuthReqHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ming on 2017/3/19.
 */
public class mRpcClient {
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port, String host) {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class);
            b.option(ChannelOption.TCP_NODELAY, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new mRpcMessageDecoder(1024 * 1024, 4, 4));
                    socketChannel.pipeline().addLast("MessageEncoder", new mRpcMessageEncoder());
                    socketChannel.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(5000));
                    socketChannel.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
                    socketChannel.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
                }
            });

            ChannelFuture future = b.connect(new InetSocketAddress(host, port), new InetSocketAddress(mRpcConstant.LOCAL_IP, mRpcConstant.LOCAL_PORT)).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            group.shutdownGracefully();
        } finally {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        try {
                            connect(mRpcConstant.PORT, mRpcConstant.LOCAL_IP);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        new mRpcClient().connect(mRpcConstant.PORT, mRpcConstant.REMOTE_IP);
    }
}
