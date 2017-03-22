package cn.whatisee.dynamic.proxy.example;

/**
 * Created by ming on 2016/12/19.
 */
public class HelloWorldImpl implements  IHelloWorld{

    public void SayHello() {
        System.out.println("Hello World");
    }

    public  static  void main(String[]args) throws Throwable {

        IHelloWorld hello=new HelloWorldImpl();
        IHelloWorld foo = (IHelloWorld) HelloWorldProxy.newInstance(new HelloWorldImpl());
        foo.SayHello();


    }
}
