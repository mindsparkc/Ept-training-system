package com.sinosoft.common.annotation;

import java.lang.annotation.*;

/**
 * @Description: 培训系统数据权限 过滤注解 不依赖角色中的数据权限控制，而是通过dataScopeMode来确定
 * user_id tenant_id dept_id
 * @Date  2025/5/16      
 * @Author soft.l  
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EtpDataScope
{
    /**
     * 用来做权限控制的字段所在表(资源所在表)的别名
     * user_id tenant_id dept_id
     */
    public String alias() default "";
    /**
     * 数据权限控制方式 通过参数控制
     */
    //public String dataScope() default "";

    // 实体类参数在方法中的位置，从0开始
    public int index() default 0;
}
