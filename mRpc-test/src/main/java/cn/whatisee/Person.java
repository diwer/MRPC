package cn.whatisee;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by ming on 2017/7/7.
 */
@Component
public class Person implements IPerson {

    @Value("li")
    private String name;

    public void introductionSelf() {
        System.out.println("I'm " + name);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

    
    
    
    
    
    
    
    
    
    