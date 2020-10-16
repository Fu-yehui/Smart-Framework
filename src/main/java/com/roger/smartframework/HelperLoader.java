package com.roger.smartframework;

import com.roger.smartframework.helper.BeanHelper;
import com.roger.smartframework.helper.ClassHelper;
import com.roger.smartframework.helper.ControllerHelper;
import com.roger.smartframework.helper.IocHelper;
import com.roger.smartframework.util.ClassUtil;

/**
 * 加载相应的 Helper 类
 * @author roger
 */
public final class HelperLoader {

    public static void init(){
        Class<?>[] classList={
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for(Class<?> cls : classList){
            ClassUtil.loadClass(cls.getName());
        }
    }
}
