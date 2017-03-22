package cn.whatisee.common.utils;

import cn.whatisee.common.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by ming on 2016/12/21.
 */
public class ConfigUtils {


    private static final Logger logger = LogManager.getLogger(ConfigUtils.class);

    private static volatile Properties PROPERTIES;

    public static Properties getPROPERTIES() {
        if (PROPERTIES == null) {
            synchronized (ConfigUtils.class) {
                if (PROPERTIES == null) {
                    String path = System.getProperty(Constants.MRPC_PROPERTIES_KEY);
                    if (StringUtils.isEmpty(path)) {
                        path = System.getenv(Constants.MRPC_PROPERTIES_KEY);
                        if (StringUtils.isEmpty(path)) {
                            path = Constants.DEFAULT_MPRC_PROPERTIES;
                        }
                    }
                    PROPERTIES = ConfigUtils.loadProperties(path, false, true);
                }
            }
        }
        return PROPERTIES;
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (StringUtils.isNotEmpty(value)) {
            return value;
        }
        Properties properties = getPROPERTIES();
        return replaceProperty(properties.getProperty(key, defaultValue), (Map) properties);
    }

    /**
     * 正则 匹配 java properties
     */
    private static Pattern VARIABLE_PATTERN =
            Pattern.compile("\\$\\s*\\{?\\s*([\\._0-9a-zA-Z]+)\\s*\\}?");

    private static String replaceProperty(String expression, Map<String, String> properties) {
        if (StringUtils.isEmpty(expression) || expression.indexOf('$') < 0) {
            return expression;
        }
        Matcher matcher = VARIABLE_PATTERN.matcher(expression);
        StringBuffer builder = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = System.getProperty(key);
            if (StringUtils.isEmpty(value) && properties != null) {
                value = properties.get(key);
            }
            if (StringUtils.isEmpty(value)) {
                value = "";
            }
            matcher.appendReplacement(builder, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    public static Properties loadProperties(String fileName) {
        return loadProperties(fileName, false);
    }

    public static Properties loadProperties(String fileName, boolean allowMultiFile) {
        return loadProperties(fileName, allowMultiFile, false);
    }

    public static Properties loadProperties(String fileName, boolean allowMultiFile, boolean optional) {
        Properties properties = new Properties();
        if (fileName.startsWith("/")) {
            try {
                FileInputStream fileInputStream = new FileInputStream(fileName);
                try {
                    properties.load(fileInputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    fileInputStream.close();
                }
            } catch (Throwable e) {
                logger.warn("Fail to load "
                        + fileName + " file from "
                        + fileName + "(ignore this file" + e.getMessage());
            }
            return properties;
        }
        List<URL> list = new ArrayList<>();
        try {
            Enumeration<URL> urls = ClassHelper.getClassLoader().getResources(fileName);
            while (urls.hasMoreElements()) {
                list.add((urls.nextElement()));
            }

        } catch (Throwable t) {
            logger.warn("fail to load " + fileName + " found on the classPath");
        }
        if (list.size() == 0) {
            if (!optional) {
                logger.warn("No " + fileName + "found no the class path");
            }
            return properties;
        }
        if (!allowMultiFile) {
            if (list.size() > 1) {
                String errMsg = String.format("Only 1%s file is  expected,but %d mRpc.properties files found on class path:%s",
                        fileName, list.size(), list.toArray());
                logger.warn((errMsg));
            }
            try {
                properties.load(ClassHelper.getClassLoader().getResourceAsStream(fileName));
            } catch (Throwable e) {
                logger.warn("Failed to load " + fileName + " file from " + fileName + "(ignore this file):" + e.getMessage(), e);
            }
            return properties;
        }
        logger.info("load " + fileName + " properties file from " + list);
        for (URL url : list) {
            try {
                Properties p = new Properties();
                InputStream inputStream = url.openStream();
                if (inputStream != null) {
                    try {
                        p.load(inputStream);
                        properties.putAll(p);
                    } finally {
                        try {
                            inputStream.close();
                        } catch (Throwable t) {
                        }
                    }
                }
            } catch (Throwable e) {
                logger.warn("Fail to load " + fileName +" file from "+url +"(ignore this file):" +e.getMessage(),e);
            }
        }
        return properties;
    }
}
