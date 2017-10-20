package cn.whatisee;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ming on 2017/7/25.
 */
public class Priority {
    private static volatile boolean noStart = true;
    private static volatile boolean notEnd = true;

    public static void main(String[] args) throws Exception {
        List<Job> jobs = new ArrayList<Job>();

        for (int i = 0; i < 10; i++) {
            int priority = i < 5 ? Thread.MIN_PRIORITY : Thread.MAX_PRIORITY;
            Job job = new Job(priority);
            jobs.add(job);
            Thread thread = new Thread(job, "Thread:" + i);
            thread.setPriority(priority);
            thread.start();
        }
        noStart = false;
        TimeUnit.SECONDS.sleep(10);

        while (true) {
            for (Job job : jobs) {
                System.out.println("Job Priority:" + job.priority + ", count:" + job.jobCount);
            }
            TimeUnit.SECONDS.sleep(5);
        }

    }

    static class Job implements Runnable {
        private int priority;
        private long jobCount;

        public Job(int priority) {
            this.priority = priority;
        }

        public void run() {
            while (noStart) {
                Thread.yield();

            }
            while (notEnd) {
                Thread.yield();
                jobCount++;
            }
        }

    }
}

    
    
    
    
    
    
    
    
    
    