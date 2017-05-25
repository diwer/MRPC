package cn.whatisee.netty;

import cn.whatisee.codec.SimpleMessageDecoder;
import cn.whatisee.codec.SimpleMessageEncoder;
import cn.whatisee.netapi.Server;
import cn.whatisee.util.NamedThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by ming on 2017/5/2.
 */
public class NettyServer implements Server {

    private Bootstrap bootstrap;

    public void connect(String host, int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(5, new NamedThreadFactory("NettyServerBoss"));
        EventLoopGroup workerGroup = new NioEventLoopGroup(20, new NamedThreadFactory("NetterServerWorker"));
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(
                new ChannelInitializer<ServerSocketChannel>() {
                    protected void initChannel(ServerSocketChannel serverSocketChannel) throws Exception {
                        serverSocketChannel.pipeline().addLast(new SimpleMessageDecoder());
                        serverSocketChannel.pipeline().addLast(new SimpleMessageEncoder());
                        serverSocketChannel.pipeline().addLast(new NettyHandler());
                    }
                }
        );
        try {
            bootstrap.bind(host, port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean isConnect() {
        return false;
    }

    public void shutDownNow() {

    }

    public void shotDown() {


    }
}
