package com.sinosoft.framework.web.service;

import com.sinosoft.common.constant.UserConstants;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.system.service.ISysMenuService;
import com.sinosoft.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户权限处理
 *
 *
 */
@Component
public class SysPermissionService
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user)
    {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user)
    {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            perms.add("*:*:*");
        }
        else
        {   // 如果未给用户分类角色，默认获得匿名用户的权限
            if (user.getRoles()==null || user.getRoles().size() == 0) {
                perms.addAll(menuService.selectMenuPermsByUserId(UserConstants.ANONYMOUS_USER_ID));
            }else {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }

        }
        return perms;
    }
}
