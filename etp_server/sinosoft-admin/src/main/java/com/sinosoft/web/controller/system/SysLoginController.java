package com.sinosoft.web.controller.system;

import cn.jkingtools.utils.DateUtils;
import com.alibaba.fastjson.util.IOUtils;
import com.sinosoft.common.constant.Constants;
import com.sinosoft.common.constant.UserConstants;
import com.sinosoft.common.core.domain.AjaxResult;
import com.sinosoft.common.core.domain.entity.SysMenu;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.common.core.domain.model.LoginBody;
import com.sinosoft.common.core.domain.model.LoginUser;
import com.sinosoft.common.utils.ServletUtils;
import com.sinosoft.common.utils.sign.RsaUtils;
import com.sinosoft.etp.common.domain.SysInfo;
import com.sinosoft.etp.common.utils.TableFiledEnum;
import com.sinosoft.etp.cp.tag.service.ITTagWithCpService;
import com.sinosoft.framework.web.service.SysLoginService;
import com.sinosoft.framework.web.service.SysPermissionService;
import com.sinosoft.framework.web.service.TokenService;
import com.sinosoft.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 *
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private ITTagWithCpService iTTagService;

    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDK6SCtWEwG1SJv" +
            "9bTyGRHdys7Ub7nDFGo+Dur78UR9hH/62U9GqoTqLJS+qaDpXsTFHiGhF5MD8m4ZHfsoZU2" +
            "Q+QBGRp2ByuSXHt0M1jUS3CC73EekZ71kbea89lQiopoQnCSCiQAJ6FlBz9NyXXhsl0EZb/" +
            "0iYotevYJ5b2E6uQIDAQAB";
    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        try {
            if(false) {
                String license = getAuthCode();
                SysInfo info = new SysInfo();
                //配置文件中的
                String[] authArr = RsaUtils.decryptByPublicKey(publicKey, license).split(",");
                String now = DateUtils.getDate();

                //获取当前系统/物理地址/CPU序号
                String name = info.getOs();
                String ip = info.getIp();
                String cpu = info.getCpu();
                String sysInfo = name + "," + ip + "," + cpu;

                // 授权合法性验证(sys u ip)
                if (!sysInfo.equals(authArr[0] + "," + authArr[1] + "," + authArr[2])) {
                    return AjaxResult.error("身份认证不符,不能登录！");
                }
                //license有效期验证
                if (authArr[3].compareTo(now) < 0) {
                    return AjaxResult.error("证书有效期已过,不能登录！");
                }
            }
            // 生成令牌 token
            String token = loginService.login(loginBody.getUsername()
                    , loginBody.getPassword()
                    , loginBody.getCode()
                    , loginBody.getUuid());
            ajax.put(Constants.TOKEN, token);
            return ajax;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取用户信息
     *
     * GetInfo 改成允许匿名访问，将返回匿名用户允许看到的菜单
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user;
        // 角色集合
        Set<String> roles;
        // 权限集合
        Set<String> permissions;
        if (loginUser == null) {
            // 匿名用户访问，设置 userId 为 3
            user = new SysUser(UserConstants.ANONYMOUS_USER_ID);
        } else {
            user = loginUser.getUser();
        }

        roles = permissionService.getRolePermission(user);
        permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        //加载企业关联题库标签缓存数据
        if (user.getUserId() != 0L && user.getUserId() != UserConstants.ANONYMOUS_USER_ID) {
            for(TableFiledEnum item: TableFiledEnum.values()){
                iTTagService.loadingTagsCache(item.name());
            }
        }

        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters(HttpServletRequest request)
    {
        String plateform = request.getHeader("x-platform");
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        List<SysMenu> menus;
        if (loginUser == null ||
                (loginUser.getUser().getRoles() == null || loginUser.getUser().getRoles().size() == 0)) {
            // 添加匿名用户获取路由表，或者未给用户分配角色，默认获取匿名用户的菜单信息
            SysUser sysUser = new SysUser(UserConstants.ANONYMOUS_USER_ID);
            menus = menuService.selectMenuTreeByUserId(sysUser.getUserId(),plateform);
        }else {
            menus = menuService.selectMenuTreeByUserId(loginUser.getUser().getUserId(),plateform);
        }

        return AjaxResult.success(menuService.buildMenus(menus));
    }

    //获取授权码
    public String getAuthCode(){

        SysInfo info = new SysInfo();
        String os = info.getOs();
        String path = null;
        File file = null;
        path  = System.getProperty("user.home");
        file = new File(path,"/.auth/authCode.txt");

//        InputStream inputStream = this.getClass().getResourceAsStream("/config/authCode.txt");
        try{
            if (file.isFile() && file.exists()) {
                InputStream inputStream = new FileInputStream(file);
                InputStreamReader dataReader = new InputStreamReader(inputStream);
                String result = IOUtils.readAll(dataReader);
                dataReader.close();
                inputStream.close();
                return result;
            }else{
                System.out.println("文件不存在!");
            }

        }catch(Exception e){
            System.out.println("文件读取错误!");
            e.printStackTrace();
        }
        return "";
    }

}
