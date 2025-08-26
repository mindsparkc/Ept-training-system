package com.sinosoft.common.annotation;

import com.sinosoft.common.enums.VipResource;

import java.lang.annotation.*;

/**
 * @Description: Vip资源控制
 * @Date  2025/5/29
 * @Author soft.l
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VipCheck
{
    //  资源
    public String resource() default "user";
}
