package cn.whatisee.dynamic.proxy.example;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by ming on 2016/12/19.
 */
public class HelloWorldProxy implements InvocationHandler {

    private Object object;

    public static Object newInstance(Object obj) {
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new HelloWorldProxy(obj));
    }

    public HelloWorldProxy(Object object) {
        this.object = object;
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        try {
            SayHelloBefor();
            result = method.invoke(object, args);
            SayHelloAfter();
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " +
                    e.getMessage());
        }
        return result;
    }

    public void SayHelloBefor() {
        System.out.println("Say Hello before");
    }

    public void SayHelloAfter() {
        System.out.println("Say Hello after");
    }

}
