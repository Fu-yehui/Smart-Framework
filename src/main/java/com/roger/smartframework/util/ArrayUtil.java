package com.roger.smartframework.util;

import org.apache.commons.lang3.ArrayUtils;

public final class ArrayUtil {

    /**
     * 判断数组是否非空ing
     */
    public static boolean isNotEmpty(Object[] array){
        return !ArrayUtil.isEmpty(array);
    }

    /**
     * 判断数组是否为空
     * @param array
     * @return
     */
    private static boolean isEmpty(Object[] array) {
        return ArrayUtils.isEmpty(array);
    }
}
