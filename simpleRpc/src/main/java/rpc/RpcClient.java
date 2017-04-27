package rpc;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * Created by ming on 2017/4/25.
 */
public class RpcClient {
    @SuppressWarnings("unchecked")
    public static <T> T findService(final String host, final int port, final Class<T> serviceInterface) {
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class[]{serviceInterface}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                Socket socket = null;
                InputStream inputStream = null;
                OutputStream outputStream = null;
                ObjectInput objectInput = null;
                ObjectOutput objectOutput = null;
                try {
                    socket = new Socket(host, port);
                    outputStream = socket.getOutputStream();
                    objectOutput = new ObjectOutputStream(outputStream);
                    objectOutput.writeUTF(serviceInterface.getName());
                    objectOutput.writeUTF(method.getName());
                    objectOutput.writeObject(method.getParameterTypes());
                    objectOutput.writeObject(args);
                    inputStream = socket.getInputStream();
                    objectInput = new ObjectInputStream(inputStream);
                    return objectInput.readObject();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {

                        socket.close();
                        inputStream.close();
                        outputStream.close();
                        objectInput.close();
                        objectOutput.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });

    }
}
