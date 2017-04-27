package rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by ming on 2017/4/26.
 */
public class RpcClient {
    public static <T> T findService(final String host, final int port, final Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class[]{serviceInterface}, new ConsumerProxy(serviceInterface, host, port));
    }
}

class ConsumerProxy implements InvocationHandler {
    private String host = "";
    private int port;
    private EventLoopGroup group = new NioEventLoopGroup();
    private Class<?> type;

    public ConsumerProxy(Class<?> type, final String host, final int port) {
        this.type = type;
        this.host = host;
        this.port = port;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return doRequest(method, args);

    }

    protected Object doRequest(final Method method, final Object[] args) throws InterruptedException {
        final ResponseHandler responseHandler = new ResponseHandler(this.type, method, args);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                socketChannel.pipeline().addLast(new ObjectEncoder());
                socketChannel.pipeline().addLast(responseHandler);
            }
        });

        ChannelFuture future = bootstrap.connect(host, port).sync();
        future.channel().closeFuture().sync();
        return responseHandler.getResult();
    }

    protected class ResponseHandler extends ChannelInboundHandlerAdapter {
        private Method method;
        private Object[] args;
        private Class<?> proxy;
        private Object result;

        ResponseHandler(Class<?> proxy, Method method, Object[] args) {
            this.method = method;
            this.args = args;
            this.proxy = proxy;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            RpcProtocol protocol = new RpcProtocol();
            protocol.setMethodName(this.method.getName());
            protocol.setServiceName(this.proxy.getName());
            protocol.setArguments(args);
            protocol.setParamTypes(this.method.getParameterTypes());
            ctx.writeAndFlush(protocol);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            result = msg;
            ctx.channel().close();
            System.out.print(msg);
        }

        public Object getResult() {
            return result;
        }
    }
}