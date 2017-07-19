package cn.whatisee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by ming on 2017/7/5.
 */
@Scope("prototype")
@Component
public class Hello implements IHello {
    @Autowired()
    private IWorld world;

    private String toname;

    @Autowired(required = true)
    private Person self;

    public void init() {
        toname = "ming";
    }

    public String sayHello() {
        String hello = "hello";
        System.out.println(hello + " " + toname);
        self.introductionSelf();
        if (world != null)
            world.sayWorld();
        return hello;
    }
}