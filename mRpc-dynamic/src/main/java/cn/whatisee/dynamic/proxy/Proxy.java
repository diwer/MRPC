package cn.whatisee.dynamic.proxy;

import java.lang.reflect.InvocationHandler;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ming on 2016/12/19.
 */
public class Proxy {
    private ConcurrentMap<String,Object> proxyMap=null;

    private ConcurrentMap<String,InvocationHandler> handlerMap=null;

}
