package cn.whatisee;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        System.out.println( "Hello World!" );
        String str="12313412,132424";
        System.out.println(str.indexOf(',')>-1);
        String[] strs=str.split(",");
        List<String> strlist= Arrays.asList(strs);
        for (String s:strlist){
            System.out.println( s);
        }
    }
}
