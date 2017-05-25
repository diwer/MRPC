package cn.whatisee.util;

import java.util.MissingResourceException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ming on 2017/5/2.
 */
public class NamedThreadFactory implements ThreadFactory {
    private static AtomicInteger POOL_SEQ = new AtomicInteger(1);
    private final AtomicInteger mThreadNum = new AtomicInteger(1);
    private final String mPrefix;
    private final boolean mDaemo;
    private final ThreadGroup mGroup;

    public NamedThreadFactory() {
        this("pool-" + POOL_SEQ.getAndIncrement(), false);
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemo) {
        mPrefix = prefix + "-thread-";
        mDaemo = daemo;
        SecurityManager s = System.getSecurityManager();
        mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    public Thread newThread(Runnable r) {
        String name = mPrefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(mGroup, r, name, 0);
        ret.setDaemon(mDaemo);
        return ret;
    }

    public ThreadGroup getThreaddGroup() {
        return mGroup;
    }

}
