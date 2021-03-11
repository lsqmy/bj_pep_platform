package com.aratek.framework.core.annotation;

import java.lang.annotation.*;

/**
 * @author shijinlong
 * @date 2018-06-28
 * @description 需AOP校验Token的自定义注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApiAnnotation {

    String value() default "";
}
