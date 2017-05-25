package cn.whatisee;

/**
 * Created by ming on 2017/5/2.
 */
public class SimpleMessage extends Message {
    private SimpleMessageHeader header;

    public SimpleMessageHeader getHeader() {
        return header;
    }

    public void setHeader(SimpleMessageHeader header) {
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
