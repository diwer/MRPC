/**
 * Created by ming on 2017/4/25.
 */
public class HelloWorldImpl implements IHelloWorld {
    @Override
    public String sayHello(String name) {

        return "Hello :" + name;
    }
}
