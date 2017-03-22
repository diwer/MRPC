package cn.whatisee.netty;

/**
 * Created by ming on 2017/3/19.
 */
public enum MessageType {

    //  枚举必须写在最前面否则编译出错 -。-
    LOGIN_RESP("登录回应", (byte) 0),
    LOGIN_REQ("登录请求", (byte) 1),
    HEARTBEAT_RESP("心跳回应", (byte) 2),
    HEARTBEAT_REQ("心跳请求", (byte) 3);


    private String name;
    private byte value;

    private MessageType(String name, byte value) {
        this.name = name;
        this.value = value;
    }

    public byte value() {
        return this.value;
    }
}
