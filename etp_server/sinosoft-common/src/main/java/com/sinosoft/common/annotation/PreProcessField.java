package com.sinosoft.common.annotation;

import java.lang.annotation.*;

/**
 * 注解 - 处理 URL 前缀需要替换的场景
 *
 * @author WANGLIJIE
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(PreProcessFields.class)
public @interface PreProcessField {
    String field() default "";
}
