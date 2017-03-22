package cn.whatisee;

import cn.whatisee.common.URL;
import cn.whatisee.common.utils.ConfigUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException {

        Logger logger = LogManager.getLogger(App.class);
        logger.info("\\&");

        URL url=URL.valueOf("https://yq.aliyun.com/articles?spm=5176.8067842.headermenu.6.drYhK0&do=login");
        logger.debug(url.getParameters());

        logger.info("&");
        System.out.println("Hello World!");


    }

    public void testThreadLog(Logger logger){
        try {
            while (true) {
                logger.debug(ConfigUtils.getProperty("mRpcName"));
                Thread.sleep(1000);
                logger.error(ConfigUtils.getProperty("mRpcName"));
                logger.info(Thread.currentThread().getId());
                Thread thread;
                for (int i = 0; i < 10; i++) {
                    thread = new Thread(new myThread());
                    thread.start();
                }
            }
        } catch (InterruptedException e) {


        }
    }

    static class myThread implements Runnable {
        Logger logger = LogManager.getLogger(myThread.class);
        @Override
        public void run() {
            try {
                Thread.sleep(new Random().nextInt(10)*1000);
                logger.info(Thread.currentThread().getId()+" "+ Thread.currentThread().getName());
                throw  new NullPointerException();
            } catch (Exception e) {
                logger.error("出错了",e);
            }
        }
    }
}

