package com.roger.smartframework.helper;

import com.roger.smartframework.annotation.Controller;
import com.roger.smartframework.annotation.Service;
import com.roger.smartframework.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 类操作助手类
 * @author roger
 */
public final class ClassHelper {

    /**
     * 定义类集合（用于存放所加载的类）
     */
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage=ConfigHelper.getAppBasePackage();
        CLASS_SET= ClassUtil.getClassSet(basePackage);
    }

    /**
     * 获取应用包名下的所有类
     * @return
     */
    public static Set<Class<?>> getClassSet(){
        return CLASS_SET;
    }

    /**
     * 获取应用包名下所有的Service类
     * @return
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classSet=new HashSet<>();
        for(Class<?> cls:CLASS_SET){
            if(cls.isAnnotationPresent(Service.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包名下所有的Controller类
     * @return
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> classSet=new HashSet<>();
        for(Class<?> cls:CLASS_SET){
            if(cls.isAnnotationPresent(Controller.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }
    /**
     * 获取应用包名下所有的Bean类(包括：Service，Controller等)
     * @return
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> classSet=new HashSet<>();
        for(Class<?> cls:CLASS_SET){
            if(cls.isAnnotationPresent(Service.class) || cls.isAnnotationPresent(Controller.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }
}
