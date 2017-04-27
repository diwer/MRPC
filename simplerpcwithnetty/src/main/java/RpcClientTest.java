import rpc.RpcClient;

/**
 * Created by ming on 2017/4/25.
 */
public class RpcClientTest {
    public static void main(String[] args) {
        IHelloWorld helloWorld =
                RpcClient.findService("127.0.0.1", 8080, IHelloWorld.class);
        String result = helloWorld.sayHello("ming");
        System.out.println(result);
    }
}
