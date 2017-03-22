package cn.whatisee.common.utils;

/**
 * Created by ming on 2016/12/21.
 */
public class StringUtils {
    public  static  boolean isEmpty(String targetStr){
        if(targetStr==null||"".equals(targetStr))
            return  true;
        return  false;
    }
    public static  boolean isNotEmpty(String targetStr){
        return  !isEmpty(targetStr);
    }
}
