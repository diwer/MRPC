package cn.whatisee;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws InterruptedException {
        // IHello hello = (IHello) beanFactory.getBean("hello");
//        hello.sayHello();
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        IPerson person1 = (IPerson) context.getBean(Person.class);
        IPerson person2 = (IPerson) context.getBean(Person.class);
        System.out.print(person1 == person2);
        System.out.print(person1.equals(person2));
        for (int i = 20; i > 0; i--) {
            Thread t = new Thread(new job(context));
            t.start();
        }
        new job(context).run();
    }

    static class job implements Runnable {

        ApplicationContext context;

        public job(ApplicationContext context) {
            this.context = context;
        }

        public void run() {
            while (true) {
                int i = new Random().nextInt(10000);
                try {
                    if(Thread.currentThread().getName()=="main"){
                        Thread.sleep(i);
                        notifyAll();
                    }
                    else {
                        if (i % 2 == 0) {
                            Thread.sleep(i);
                        } else {
                            synchronized (this) {
                                wait();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                IHello hello1 = (IHello) context.getBean("hello");
                IHello hello2 = (IHello) context.getBean("hello");
                System.out.println(Thread.currentThread().getState());
                System.out.println(Thread.currentThread().getName());
                System.out.println(hello2 == hello1);
                //System.out.print(hello1.equals(hello2));
                //hello.sayHello();
            }
        }
    }

    public static int hash(Object k) {
        int h = 31;

        if ((0 != h) && (k instanceof String)) {
            return sun.misc.Hashing.stringHash32((String) k);
        }
        System.out.println(k.hashCode());
        h ^= k.hashCode();
        System.out.println(h);
        // Spread bits to regularize both segment and index locations,
        // using variant of single-word Wang/Jenkins hash.
        h += (h << 15) ^ 0xffffcd7d;
        System.out.println(h);
        h ^= (h >>> 10);
        System.out.println(h);
        h += (h << 3);
        System.out.println(h);
        h ^= (h >>> 6);
        System.out.println(h);
        h += (h << 2) + (h << 14);
        System.out.println(h);
        h = h ^ (h >>> 16);
        System.out.println(h);
        return h;
    }
}
