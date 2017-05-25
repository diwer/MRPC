package cn.whatisee.netapi;

/**
 * Created by ming on 2017/5/2.
 */
public interface Server {
    void connect(String host, int port);

    boolean isConnect();

    void shutDownNow();


    void shotDown();
}
