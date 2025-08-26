package com.sinosoft.common.annotation;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreProcessFields {
    PreProcessField[] value();
}
