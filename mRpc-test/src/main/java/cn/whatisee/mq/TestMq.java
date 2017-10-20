package cn.whatisee.mq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestMq {
    public static void main(final String... args) throws Exception {

        AbstractApplicationContext ctx =
                new ClassPathXmlApplicationContext("spring.xml");
        RabbitTemplate template =(RabbitTemplate) ctx.getBean("amqpTemplate");
        template.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                System.out.println("发送成功");
            }
        });

        for (int i = 0; i < 100000; i++) {
            template.convertAndSend("Hello, world!");
            Thread.sleep(1000);
        }
        System.out.println("send success");
        Thread.sleep(1000);
        ctx.destroy();
    }
}

    
    
    
    
    
    
    
    
    
    