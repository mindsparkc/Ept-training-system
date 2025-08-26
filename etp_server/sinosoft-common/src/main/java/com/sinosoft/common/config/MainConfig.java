package com.sinosoft.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 *
 */
@Component
@ConfigurationProperties(prefix = "sinosoft")
public class MainConfig
{
    /** 项目名称 */
    private String name;

    /** 版本 */
    private String version;

    /** 版权年份 */
    private String copyrightYear;

    /** 实例演示开关 */
    private boolean demoEnabled;

    /** 上传路径 */
    private static String profile;

    /** 获取地址开关 */
    private static boolean addressEnabled;

    /** 判断是否本地调试模式 */
    private static boolean localDev;
    /** 文件转换服务 */
    private static String parsrFileServer;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getCopyrightYear()
    {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear)
    {
        this.copyrightYear = copyrightYear;
    }

    public boolean isDemoEnabled()
    {
        return demoEnabled;
    }

    public void setDemoEnabled(boolean demoEnabled)
    {
        this.demoEnabled = demoEnabled;
    }

    public static String getProfile()
    {
        return profile;
    }

    public void setProfile(String profile)
    {
        MainConfig.profile = profile;
    }

    public static boolean isAddressEnabled()
    {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
        MainConfig.addressEnabled = addressEnabled;
    }

    public static boolean isLocalDev() {
        return localDev;
    }

    public void setLocalDev(boolean localDev) {
        MainConfig.localDev = localDev;
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }

    /**
     * 获取临时文件保存路径
     */
    public static String getUploadTemporaryPath()
    {
        return getProfile() + "/temp";
    }
    public static String getWordTransPath()
    {
        return getProfile() + "/wordTemplate/";
    }

    public static String getParsrFileServer() {
        return parsrFileServer;
    }

    public void setParsrFileServer(String parsrFileServer) {
        MainConfig.parsrFileServer = parsrFileServer;
    }
}
