package cn.whatisee.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by ming on 2016/12/22.
 */
public class ClassHelper {

    private static Logger logger = LogManager.getLogger(ClassHelper.class);

    public static ClassLoader getClassLoader() {
        return getClassLoader(ClassHelper.class);
    }

    public static ClassLoader getClassLoader(Class<?> cls) {
        ClassLoader c1 = null;
        try {
            c1 = Thread.currentThread().getContextClassLoader();
        } catch (Throwable e) {
            logger.catching(e);
        }
        if (c1 == null) {
            c1 = cls.getClassLoader();
        }
        return c1;
    }
}
