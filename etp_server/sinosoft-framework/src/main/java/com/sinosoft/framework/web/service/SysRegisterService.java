package com.sinosoft.framework.web.service;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.constant.Constants;
import com.sinosoft.common.constant.UserConstants;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.common.core.domain.model.RegisterBody;
import com.sinosoft.common.core.redis.RedisCache;
import com.sinosoft.common.exception.user.CaptchaException;
import com.sinosoft.common.exception.user.CaptchaExpireException;
import com.sinosoft.common.utils.MessageUtils;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.framework.manager.AsyncManager;
import com.sinosoft.framework.manager.factory.AsyncFactory;
import com.sinosoft.system.domain.SysTenant;
import com.sinosoft.system.service.ISysConfigService;
import com.sinosoft.system.service.ISysDeptService;
import com.sinosoft.system.service.ISysTenantService;
import com.sinosoft.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注册校验方法
 *
 *
 */
@Component
public class SysRegisterService {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysTenantService tenantService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 注册 用户 todo:还需要完善，注册后属于哪个部门？？
     */
    public String register(RegisterBody registerBody) {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();

        boolean captchaOnOff = configService.selectCaptchaOnOff();
        // 验证码开关
        if (captchaOnOff) {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(username))) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else {
            SysUser sysUser = new SysUser();
            sysUser.setUserName(username);
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(registerBody.getPassword()));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER,
                        MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    /**
     * 注册租户
     */
    public String registerTenant(SysTenant tenant) {
        String msg = "";
        try {
            if(cn.jkingtools.utils.StringUtils.isEmpty(tenant.getVipGrade())){
                String vipGrade = configService.selectConfigByKey("sys.tenant.defaultVipGrade");
                tenant.setVipGrade(StringUtils.isEmpty(vipGrade)?"0":vipGrade);
            }
            this.tenantService.addCustomer(tenant);
        }catch (Exception ex){
            msg = ex.getMessage();
        }
        return msg;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }

}
