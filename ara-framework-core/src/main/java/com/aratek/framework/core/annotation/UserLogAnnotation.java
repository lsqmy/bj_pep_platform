package com.aratek.framework.core.annotation;

import java.lang.annotation.*;

/**
 * @author shijinlong
 * @date 2018-05-16
 * @description 需AOP记录用户操作日志的自定义注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserLogAnnotation {

    /**
     * 操作模块名称
     *
     * @return
     */
    String module() default "unknown module";

    /**
     * 操作类型
     *
     * @return
     */
    String actType() default "other";
}
