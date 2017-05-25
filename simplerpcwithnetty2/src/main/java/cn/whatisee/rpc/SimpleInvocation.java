package cn.whatisee.rpc;

import cn.whatisee.common.URL;

import java.util.Map;

/**
 * Created by ming on 2017/5/17.
 */
public class SimpleInvocation implements Invocation {

    private URL url;

    public SimpleInvocation(URL url) {
        this.url = url;
    }


    public String getMethodName() {
        this.url.

        return null;
    }

    public Class<?>[] getParameterTypes() {
        return new Class[0];
    }

    public Object[] getArguments() {
        return new Object[0];
    }

    public Map<String, String> getAttachments() {
        return null;
    }

    public String getAttachment(String key) {
        return null;
    }

    public String getAttachment(String key, String defaultValue) {
        return null;
    }

    public Invoker<?> getInvoker() {
        return null;
    }
}
