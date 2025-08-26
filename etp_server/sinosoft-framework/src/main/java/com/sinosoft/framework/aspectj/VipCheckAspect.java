package com.sinosoft.framework.aspectj;

import cn.jkingtools.utils.BeanUtils;
import cn.jkingtools.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.annotation.VipCheck;
import com.sinosoft.common.core.domain.BaseEntity;
import com.sinosoft.common.core.domain.entity.SysDept;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.common.core.domain.model.LoginUser;
import com.sinosoft.common.enums.VipResource;
import com.sinosoft.common.utils.SecurityUtils;
import com.sinosoft.common.utils.ServletUtils;
import com.sinosoft.common.utils.spring.SpringUtils;
import com.sinosoft.framework.web.service.TokenService;
import com.sinosoft.system.domain.SysTenant;
import com.sinosoft.system.domain.SysTenantVip;
import com.sinosoft.system.service.ISysConfigService;
import com.sinosoft.system.service.ISysDeptService;
import com.sinosoft.system.service.ISysTenantService;
import com.sinosoft.system.service.ISysTenantVipService;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections.MapUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/***
 * @Description: vip等级控制来实现
 * @Date  2025/5/29
 * @Author soft.l
*/
@Aspect
@Component
public class VipCheckAspect {
    @Resource
    private ISysConfigService configService;
    @Resource
    private ISysTenantService tenantService;
    @Resource
    private ISysTenantVipService tenantVipService;
    @Resource
    private ISysDeptService deptService;
    // 配置织入点
    @Pointcut("@annotation(com.sinosoft.common.annotation.VipCheck)")
    public void vipCheckPointCut() {
    }

    @Before("vipCheckPointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        VipCheck check = getAnnotationLog(point);
        if (check == null) return;
        VipResource resource =VipResource.getVipResourceByCode(check.resource());
        if (resource == null) return;
        SysDept top = deptService.queryTopDept();
        if (top == null) return;
        if (SecurityUtils.getLoginUser() == null) return;
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String tenantId = user.getTenantId() == null ? "0" : user.getTenantId(); // 即二级部门的id
        if ("0".equals(tenantId)) return;

        SysTenant tenant = new SysTenant();
        tenant.setDeptId(Long.valueOf(tenantId));
        List<SysTenant> list = tenantService.selectTenantList(tenant);
        if (list == null || list.isEmpty()) return;
        if (list.get(0).getVipGrade() == null) return;
        SysTenantVip vip = tenantVipService.selectSysTenantVipByCode(list.get(0).getVipGrade());
        if (vip == null) return;

        Boolean checked = resource.check(tenantId, top.getDeptId(), new ObjectMapper().convertValue(vip, Map.class), (String sql) -> tenantVipService.getCount(sql));
        if (checked) return;
        throw new RuntimeException("您的资源使用量超过了会员等级限制"); // 抛出异常，阻止方法执行
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private VipCheck getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(VipCheck.class);
        }
        return null;
    }
}
