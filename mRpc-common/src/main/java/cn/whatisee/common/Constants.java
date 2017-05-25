package cn.whatisee.common;

import java.util.regex.Pattern;

/**
 * Created by ming on 2016/12/21.
 */
public class Constants {
    public static final String MRPC_PROPERTIES_KEY = "mRpc.properties.file";

    public static final String DEFAULT_MPRC_PROPERTIES = "mRpc.properties";
    public static final String BACKUP_KEY = "backup";
    public static final Pattern COMMA_SPLIT_PATTERN = Pattern
            .compile("\\s*[,]+\\s*");
    public static final String DEFAULT_KEY_PREFIX = "default.";
    public static final String LOCALHOST_KEY = "localhost";
    public static final String ANYHOST_KEY = "anyhost";
    public static final String ANYHOST_VALUE = "0.0.0.0";
    public static final String REMOVE_VALUE_PREFIX = "-";
    public static final String DEFAULT_KEY = "default";

    public static Object PROXY_KEY;
}
