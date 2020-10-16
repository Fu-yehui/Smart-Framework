package com.roger.smartframework.constant;

/**
 * 提供相关配置项常量
 * @author roger
 */
public enum ConfigConstant {

    /**
     * 配置文件名常量
     */
    CONFIG_FILE("smart.properties"),
    /**
     * 配置文件中配置项driver常量
     */
    JDBC_DRIVER("smart.framework.jdbc.driver"),
    /**
     * 配置文件中配置项url常量
     */
    JDBC_URL("smart.framework.jdbc.url"),
    /**
     * 配置文件中配置项username常量
     */
    JDBC_USERNAME("smart.framework.jdbc.username"),
    /**
     * 配置文件中配置项password常量
     */
    JDBC_PASSWORD("smart.framework.jdbc.password"),
    /**
     * 配置文件中配置项app.base_package常量
     */
    APP_BASE_PACKAGE("smart.framework.app.base_package"),
    /**
     * 配置文件中配置项app.jsp_path常量
     */
    APP_JDP_PATH("smart.framework.app.jsp_path"),
    /**
     * 配置文件中配置项app.asset_path常量
     */
    APP_ASSET_PATH("smart.framework.app.asset_path");

    private final String value;
    ConfigConstant(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
