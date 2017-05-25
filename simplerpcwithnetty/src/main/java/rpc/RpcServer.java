package rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by ming on 2017/4/26.
 */
public class RpcServer {
    private static final ExecutorService taskPool = Executors.newFixedThreadPool(50);
    private static final ConcurrentHashMap<String, Object> serviceTargets = new ConcurrentHashMap<String, Object>();
    private static AtomicBoolean run = new AtomicBoolean(false);

    public void registService(Object service) {
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces == null) {
            throw new IllegalArgumentException("服务对象必须继承接口");
        }
        Class<?> interfacez = interfaces[0];
        String interfaceName = interfacez.getName();
        serviceTargets.put(interfaceName, service);
    }

    public void startServer(final int port) throws InterruptedException {
        doOpen(port);
    }

    public void doOpen(final int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(50);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                socketChannel.pipeline().addLast(new ObjectEncoder());
                socketChannel.pipeline().addLast(new reciveRequestHandl());
            }
        });
        bootstrap.bind("127.0.0.1", port).sync();
    }

    protected class  reciveRequestHandl extends ChannelInboundHandlerAdapter {

        public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
            RpcProtocol protocol = (RpcProtocol) o;
            Object targetService = serviceTargets.get(protocol.getServiceName());
            if (targetService == null) {
                throw new ClassNotFoundException(protocol.getServiceName() + "服务未找到");
            }
            Method targetMethod = targetService.getClass().getMethod(protocol.getMethodName(), protocol.getParamTypes());
            Object result = targetMethod.invoke(targetService, protocol.getArguments());
            channelHandlerContext.writeAndFlush(result);
        }
    }
}

























