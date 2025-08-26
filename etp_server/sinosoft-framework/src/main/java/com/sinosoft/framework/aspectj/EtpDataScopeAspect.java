package com.sinosoft.framework.aspectj;

import cn.jkingtools.utils.StringUtils;
import com.sinosoft.common.annotation.EtpDataScope;
import com.sinosoft.common.core.domain.BaseEntity;
import com.sinosoft.common.core.domain.entity.SysUser;
import com.sinosoft.common.core.domain.model.LoginUser;
import com.sinosoft.common.utils.ServletUtils;
import com.sinosoft.common.utils.spring.SpringUtils;
import com.sinosoft.framework.web.service.TokenService;
import com.sinosoft.system.service.ISysConfigService;
import com.sinosoft.system.utils.EtpDataScopeUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/***
 * @Description: 数据过滤处理- 培训系统，依据在注解中配置dataScopeMode来实现
 * @Date  2025/5/16
 * @Author soft.l
*/
@Aspect
@Component
public class EtpDataScopeAspect
{
    @Resource
    private ISysConfigService configService;

    // 配置织入点
    @Pointcut("@annotation(com.sinosoft.common.annotation.EtpDataScope)")
    public void dataScopePointCut()
    {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable
    {
        EtpDataScope controllerDataScope = getAnnotationLog(point);
        if (controllerDataScope == null) {
            return;
        }
        clearDataScope(point,controllerDataScope);
        handleDataScope(point,controllerDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint,EtpDataScope controllerDataScope)
    {
        // 获取当前的用户
        LoginUser loginUser = SpringUtils.getBean(TokenService.class).getLoginUser(ServletUtils.getRequest());
        if (StringUtils.isNotNull(loginUser))
        {
            SysUser currentUser = loginUser.getUser();
            // 如果是超级管理员，则不过滤数据
            if (StringUtils.isNotNull(currentUser) && !currentUser.isAdmin())
            {
                String dataScope = configService.selectConfigByKey("etp.datascope.mode");
                if(StringUtils.isNull(dataScope))dataScope="6";
                //dataScopeFilter(joinPoint, currentUser, controllerDataScope,dataScope); // 后续可以考虑通过系统参数来控制，而不是通过注解中指定
                dataScopeFilter(joinPoint, controllerDataScope);
            }
        }
    }
    public static void dataScopeFilter(JoinPoint joinPoint,EtpDataScope controllerDataScope) {
        String sql = EtpDataScopeUtils.getDataScopeSql(controllerDataScope.alias());
        if (StringUtils.isNotBlank(sql)) {
            //Object params = joinPoint.getArgs()[0]; // 取第一个参数，只能是实体类，约定如此，否则不能用
            Object params = joinPoint.getArgs()[controllerDataScope.index()]; // 取第一个参数，只能是实体类，约定如此，否则不能用
            if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) params;
                //baseEntity.getParams().put(EtpDataScopeUtils.ETP_DATA_SCOPE, " AND (" + sql + ")");
                baseEntity.getParams().put(EtpDataScopeUtils.ETP_DATA_SCOPE, sql); // and 已在工具类中实现
            }
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private EtpDataScope getAnnotationLog(JoinPoint joinPoint)
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(EtpDataScope.class);
        }
        return null;
    }

    /**
     * 拼接权限sql前先清空params.dataScope参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint,EtpDataScope controllerDataScope)
    {
        //Object params = joinPoint.getArgs()[0];
        Object params = joinPoint.getArgs()[controllerDataScope.index()];
        if (StringUtils.isNotNull(params) && params instanceof BaseEntity)
        {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put(EtpDataScopeUtils.ETP_DATA_SCOPE, "");
        }
    }
}
