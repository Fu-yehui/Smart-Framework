package com.roger.smartframework.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类
 */
public class CollectionUtil {

    /**
     * 判断Collection是否为空
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection){
        return CollectionUtils.isEmpty(collection);
    }

    /**
     * 判断Collection是否为非空
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }


    public static boolean isEmpty(Map<?,?> map){
        return MapUtils.isEmpty(map);
    }

    public static boolean isNotEmpty(Map<?,?> map){
        return !isEmpty(map);
//        return MapUtils.isNotEmpty(map);
    }

}
