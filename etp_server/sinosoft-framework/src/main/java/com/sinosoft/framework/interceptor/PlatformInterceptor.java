package com.sinosoft.framework.interceptor;

import cn.jkingtools.constant.HttpStatus;
import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.core.domain.entity.SysRole;
import com.sinosoft.common.core.domain.model.LoginUser;
import com.sinosoft.common.exception.CustomException;
import com.sinosoft.common.utils.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * x-plateform 拦截器
 * 0. x-plateform 必须有值
 * 1. 只有拥有 admin 或 useradmin 角色的用户可以登录 x-plateform='admin' 的端
 * 2. 无 common 角色的用户不能登录非 x-plateform='admin' 的端
 *
 * @author WANGLIJIE
 */
@Component
public class PlatformInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LoginUser loginUser = null;
        try {
            loginUser = SecurityUtils.getLoginUser();
        } catch (CustomException e) {
            // e.printStackTrace();
        }

//        if (1==1) return true;

        // 若未获取到用户信息，则不处理
        if (StringUtils.isNull(loginUser)) {
            return true;
        }

        String plateform = request.getHeader("x-platform");
        if (StringUtils.isBlank(plateform)) {
            // 若 x-platform 无值，则返回 401
            throw new CustomException("您无权限访问该平台", HttpStatus.UNAUTHORIZED);
        }

        // 若无对应角色
        AtomicBoolean b = new AtomicBoolean(false);
        List<SysRole> roles = loginUser.getUser().getRoles();
        // 如果获取用户角色失败，允许登陆非管理端
        if (roles.size() == 0 && !plateform.equals("ADMIN")) {
            return true;
        }

        roles.forEach(sysRole -> {
            switch (plateform) {
                case "ADMIN": // 仅 admin、useradmin 角色可登陆管理员后台
                   if (sysRole.getRoleKey().equals("admin") || sysRole.getRoleKey().equals("useradmin")) {
                       b.set(true);
                   }
                   break;
                default: // 学员端不做权限控制，所有角色均可登陆
                    // if (sysRole.getRoleKey().equals("common")||sysRole.getRoleKey().equals("student"))
                    b.set(true);
            }
        });

        if (!b.get()) {
            throw new CustomException("您无权限访问该平台，原因：角色不匹配", HttpStatus.UNAUTHORIZED);
        }

        return b.get();
    }
}
