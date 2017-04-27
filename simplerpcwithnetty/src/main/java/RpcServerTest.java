import rpc.RpcServer;

/**
 * Created by ming on 2017/4/25.
 */
public class RpcServerTest {
    public  static void main(String[]args) throws InterruptedException {
        RpcServer server=new RpcServer();
        server.registService(new HelloWorldImpl());
        server.startServer(8080);
    }
}
