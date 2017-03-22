package cn.whatisee.common.spi;

import cn.whatisee.common.utils.Holder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ming on 2017/1/3.
 */
public class ExtensionLoader<T> {

    private static final Logger logger = LogManager.getLogger(ExtensionLoader.class);

    private static final String SERVICES_DIRECTORY = "META-INF/service/";
    private static final String MRPC_DIR = "META-INF/mRpc/";
    private static final ConcurrentHashMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<Class<?>, ExtensionLoader<?>>();
    private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<String, Holder<Object>>();
    private static final ConcurrentHashMap<String, Class<?>> cacheClass = new ConcurrentHashMap<String, Class<?>>();
    private Class<?> type;

    private String name;


    private ExtensionLoader(Class<?> type) {
        this.type = type;
        this.name = type.getName();
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("type == null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("type  must be  interface");
        }
        if (!withExtensionAnnotation(type)) {
            throw new IllegalArgumentException("not spi annotations class");
        }
        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (loader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
            loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }

    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }


    public Set<String> getSupportedExtensions() {
        if (cacheClass.size() == 0) {
            loadClasses();
        }
        return Collections.unmodifiableSet(cacheClass.keySet());
    }

    public T getExtension() {
        return getExtension(name);
    }

    public T getExtension(String name) {

        Set<String> supports = getSupportedExtensions();
        if (!supports.contains(name)) {
            throw new IllegalArgumentException("name: " + name + " is not supported!");
        }

        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<Object>());
            holder = cachedInstances.get(name);
        }
        Object obj = holder.get();
        if (obj == null) {
            synchronized (holder) {
                obj = holder.get();
                if (obj == null) {
                    obj = getExtensionInstance(name);
                    holder.set(obj);
                }
            }
        }
        return (T) obj;
    }

    private Object getExtensionInstance(String name) {
        if (cacheClass == null && cacheClass.size() == 0) {
            loadClasses();
        }
        if (cacheClass.containsKey(name)) {
            try {
                Object obj = cacheClass.get(name).newInstance();
                return obj;
            } catch (Throwable e) {
                logger.error("error get new Instance", e);
            }

        }
        throw new IllegalArgumentException("name: " + name + " is not supported!");

    }

    private void loadClasses() {
        //ConcurrentHashMap<String, Class<?>> extensionClass = new ConcurrentHashMap<String, Class<?>>();
        parse(cacheClass, SERVICES_DIRECTORY);
        parse(cacheClass, MRPC_DIR);
        //return extensionClass;
    }

    private void parse(Map<String, Class<?>> extensionClasses, String dir) {
        String filename = dir + type.getName();
        ClassLoader loader = findClassloader();
        Enumeration<URL> urls;
        try {
            urls = loader.getResources(filename);
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();

                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            final int commentFlagIndex = line.lastIndexOf('#');
                            if (commentFlagIndex > 0) {
                                line = line.substring(0, commentFlagIndex);
                            }
                            line = line.trim();
                            if (line.length() > 0) {
                                String name = "";
                                int i = line.indexOf('=');
                                if (i > 0) {
                                    name = line.substring(0, i).trim();
                                    line = line.substring(i + 1).trim();
                                }
                                if (line.length() > 0) {
                                    Class<?> clazz = Class.forName(line, true, loader);
                                    if (clazz.isAssignableFrom(type)) {
                                        throw new IllegalStateException(type.getSimpleName() + ":the type is not subtype for interface:" + clazz.getName());
                                    }
                                    String[] names = name.split(",");
                                    if (names != null && names.length > 0) {
                                        for (String n : names) {
                                            n = n.trim();
                                            if (n.length() > 0 && extensionClasses.get(n) == null) {
                                                extensionClasses.put(n, clazz);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Throwable e) {
                        logger.error("load class  is error", e);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("not load the  file " + filename, e);
        }

    }

    private ClassLoader findClassloader() {
        ClassLoader loader = ExtensionLoader.class.getClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        return loader;
    }

}
