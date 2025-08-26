package com.sinosoft.web.controller.system;

import com.sinosoft.common.core.controller.BaseController;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.domain.model.RegisterBody;
import com.sinosoft.framework.web.service.SysRegisterService;
import com.sinosoft.system.domain.SysTenant;
import com.sinosoft.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证
 *
 *
 */
@RestController
public class SysRegisterController extends BaseController
{
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser"))))
        {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }

    //租户注册(包含部门和用户信息)
    @PostMapping("/register/tenant")
    public AjaxResult registerTenant(@RequestBody SysTenant tenant)
    {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerTenant"))))
        {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.registerTenant(tenant);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
