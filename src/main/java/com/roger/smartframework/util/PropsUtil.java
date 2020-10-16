package com.roger.smartframework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 用来读取properties文件的工具类
 * @author roger
 */
public final class PropsUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     */
    public static Properties loadProps(String fileName){
        Properties props=null;

        try(InputStream  in=Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName))
        {
            if(in==null){
                throw new FileNotFoundException(fileName+ " file is not found");
            }
            props=new Properties();
            props.load(in);
        } catch (IOException e) {
            LOGGER.error("load properties file failure",e);
        }
        return props;
    }

    /**
     * 获取字符型属性(默认值为空字段）
     * @param props
     * @param key
     * @return
     */
    public static String getString(Properties props,String key){
        return getString(props,key,"");
    }

    /**
     * 获取字符型属性（可以指定默认值)
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Properties props,String key,String defaultValue){
        String value=defaultValue;
        if(props.containsKey(key)){
            value=props.getProperty(key);
        }
        return value;
    }

    /**
     * 获取数值性属性（默认值为0）
     * @param props
     * @param key
     * @return
     */
   public static int getInt(Properties props,String key){
        return getInt(props,key,0);
   }

    /**
     * 获取数值性属性（可以指定默认值）
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
   public static int getInt(Properties props,String key,int defaultValue){
       int value=defaultValue;
       if(props.containsKey(key)){
           value=CastUtil.castInt(props.getProperty(key));
       }
       return value;
   }

    /**
     * 获取布尔性属性（默认值为0）
     * @param props
     * @param key
     * @return
     */
    public static boolean getBoolean(Properties props,String key){
        return getBoolean(props,key,false);
    }

    /**
     * 获取布尔性属性（可以指定默认值）
     * @param props
     * @param key
     * @param defaultValue
     * @return
     */
    public static Boolean getBoolean(Properties props,String key,Boolean defaultValue){
        Boolean value=defaultValue;
        if(props.containsKey(key)){
            value=CastUtil.castBoolean(props.getProperty(key));
        }
        return value;
    }
}
