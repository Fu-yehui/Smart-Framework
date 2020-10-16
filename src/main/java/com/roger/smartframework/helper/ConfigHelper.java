package com.roger.smartframework.helper;

import com.roger.smartframework.constant.ConfigConstant;
import com.roger.smartframework.util.PropsUtil;

import java.util.Properties;

/**
 * 属性文件助手类
 *
 * 用于获取smart.properties配置文件的配置项
 * @author roger
 */
public final class ConfigHelper {

    /**
     * 加载配置文件
     */
    private static final Properties CONFIG_PROPS= PropsUtil.loadProps(ConfigConstant.CONFIG_FILE.getValue());

    /**
     * 获取JDBC驱动
     * @return
     */
    public static String getJdbcDriver(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_DRIVER.getValue());
    }

    /**
     * 获取 JDBC URL
     * @return
     */
    public static String getJdbcUrl(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_URL.getValue());
    }

    /**
     * 获取JDBC 用户名
     * @return
     */
    public static String getJdbcUsername(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_USERNAME.getValue());
    }
    /**
     * 获取JDBC 密码
     * @return
     */
    public static String getJdbcPassword(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_PASSWORD.getValue());
    }

    /**
     * 获取应用基础包名
     * @return
     */
    public static String getAppBasePackage(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_BASE_PACKAGE.getValue());
    }
    /**
     * 获取应用JSP路径,默认值：/WEB-INF/view
     * @return
     */
    public static String getAppJspPath(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_JDP_PATH.getValue(),"/WEB-INF/view");
    }
    /**
     * 获取应用应用静态资源路径
     * 默认值：/asset/
     * @return
     */
    public static String getAppAssetPath(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_ASSET_PATH.getValue(),"/asset/");
    }
}
