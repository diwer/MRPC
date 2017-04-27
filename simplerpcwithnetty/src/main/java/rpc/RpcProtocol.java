package rpc;

import java.io.Serializable;

/**
 * Created by ming on 2017/4/26.
 */
public class RpcProtocol implements Serializable {
    private static final long serialVersionUID = 9211171244132725859L;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    private String serviceName;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] arguments;


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
