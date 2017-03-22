package cn.whatisee.netty;

import java.util.Map;

/**
 * Created by ming on 2017/2/23.
 */
public class mRpcMessage {

    private mRpcMessageHeader header;

    public mRpcMessageHeader getHeader() {
        return header;
    }

    public void setHeader(mRpcMessageHeader header) {
        this.header = header;
    }

    private Object body;


    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
