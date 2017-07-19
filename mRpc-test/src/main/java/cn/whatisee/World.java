package cn.whatisee;

import org.springframework.stereotype.Component;

/**
 * Created by ming on 2017/7/5.
 */
@Component
public class World implements IWorld {
    public String sayWorld() {
        String world = "world";
        System.out.print(world);
        return world;
    }
}

    
    
    
    
    
    
    
    
    
    