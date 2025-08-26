package com.sinosoft.common.utils;
/**
 * DeviceUtil
 *
 */
public class DeviceUtil {
    //定义移动端请求的所有可能类型
    private final static String[] agent = { "Android", "iPhone", "iPod","iPad", "Windows Phone", "MQQBrowser" };

    /**
     * 判断User-Agent 是不是来自于手机
     * @param userAgent
     * @return
     */
    public static boolean checkAgentIsMobile(String userAgent) {
        boolean flag = false;
        if (!userAgent.contains("Windows NT") || (userAgent.contains("Windows NT") && userAgent.contains("compatible; MSIE 9.0;"))) {
            // 排除 苹果桌面系统
            if (!userAgent.contains("Windows NT") && !userAgent.contains("Macintosh")) {
                for (String item : agent) {
                    if (userAgent.contains(item)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

}
