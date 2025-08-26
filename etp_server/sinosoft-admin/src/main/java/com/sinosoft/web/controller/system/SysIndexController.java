package com.sinosoft.web.controller.system;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.config.MainConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 *
 *
 */
@RestController
public class SysIndexController
{
    /** 系统基础配置 */
    @Autowired
    private MainConfig mainConfig;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index()
    {
        return StringUtils.format("欢迎使用{}系统，当前 ETP API 服务版本：v{}，请通过前端地址访问。", mainConfig.getName(), mainConfig.getVersion());
    }
}
