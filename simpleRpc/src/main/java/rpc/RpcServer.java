package rpc;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by ming on 2017/4/23.
 */
public class RpcServer {
    private static final ExecutorService taskPool = Executors.newFixedThreadPool(50);

    private static final ConcurrentHashMap<String, Object> serviceTargets = new ConcurrentHashMap<String, Object>();

    private static AtomicBoolean run = new AtomicBoolean(false);


    public void registService(Object service) {
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces == null) {
            throw new IllegalArgumentException("服务对象必须实现接口");
        }
        Class<?> interfacez = interfaces[0];
        String interfaceName = interfacez.getName();
        serviceTargets.put(interfaceName, service);
    }

    public void startServer(final int port) {
        Runnable lifeThread = new Runnable() {
            @Override
            public void run() {
                ServerSocket lifeSocket = null;
                Socket client = null;
                ServiceTask serviceTask = null;
                try {
                    lifeSocket = new ServerSocket(port);
                    run.set(true);
                    while (run.get()) {
                        client = lifeSocket.accept();
                        serviceTask = new ServiceTask(client);
                        serviceTask.accept();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        taskPool.execute(lifeThread);
        System.out.println("服务启动成功");
    }

    public void stopServer() {
        run.set(false);
        taskPool.shutdown();
    }

    public static final class ServiceTask implements Runnable {
        private Socket client;

        public ServiceTask(Socket client) {
            this.client = client;
        }

        public void accept() {
            taskPool.execute(this);
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            ObjectInput objectInput = null;
            OutputStream outputStream = null;
            ObjectOutput objectOutput = null;
            try {
                inputStream = client.getInputStream();
                outputStream = client.getOutputStream();
                objectInput = new ObjectInputStream(inputStream);
                String serviceName = objectInput.readUTF();
                String methodName = objectInput.readUTF();
                Class<?>[] paramTypes = (Class<?>[]) objectInput.readObject();
                Object[] arguments = (Object[]) objectInput.readObject();
                System.out.println("serviceName" + serviceName + "methodName:" + methodName);
                Object targetService = serviceTargets.get(serviceName);
                if (targetService == null) {
                    throw new ClassNotFoundException(serviceName + "服务未找到");
                }

                Method targetMethod = targetService.getClass().getMethod(methodName, paramTypes);
                Object result = targetMethod.invoke(targetService, arguments);
                objectOutput = new ObjectOutputStream(outputStream);
                objectOutput.writeObject(result);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                    outputStream.close();
                    objectInput.close();
                    objectOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}







































