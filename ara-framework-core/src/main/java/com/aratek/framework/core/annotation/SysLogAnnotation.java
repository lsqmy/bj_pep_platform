package com.aratek.framework.core.annotation;

import java.lang.annotation.*;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 需AOP记录日志的自定义注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLogAnnotation {

    String value() default "";
}
