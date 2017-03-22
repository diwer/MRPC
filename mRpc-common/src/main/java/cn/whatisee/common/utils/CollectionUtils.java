package cn.whatisee.common.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ming on 2016/12/27.
 */
public class CollectionUtils {
    public static Map<String,String> toStringMap(String[] pairs) {
        Map<String,String> parameters=new HashMap<>();
        if(pairs.length>0){
            if(pairs.length%2!=0){
                throw  new IllegalArgumentException(" must be pairs");
            }
            for (int i=0;i<pairs.length;i+=2){
                parameters.put(pairs[i],pairs[1]);
            }
        }
        return parameters;
    }

    public static boolean isEmpty(Collection col) {
        if(col==null||col.size()==0)
            return true;
        return false;
    }
    public static boolean isNotEmpty(Collection collection){
        return !isEmpty(collection);
    }

    public static boolean requireNotEmpty(String[] arrays) {
        if (arrays==null||arrays.length==0)
            return false;
        return true;
    }
}
