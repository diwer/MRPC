package cn.whatisee;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ming on 2017/7/21.
 */
public class Test {
    private     int i;
    private  AtomicInteger ii;
    public Test(){
        i=0;
        ii=new AtomicInteger(0);
    }
    public void write() {
        i++;
    }

    public int read() {
        return i;
    }

    public synchronized void synchronizedwrite() {
        i+=1;
    }

    public synchronized int synchronizedread() {
        return i;
    }
    public    int synchronizedwriteandread() {
        return  i++;
    }
}

    
    
    
    
    
    
    
    
    
    