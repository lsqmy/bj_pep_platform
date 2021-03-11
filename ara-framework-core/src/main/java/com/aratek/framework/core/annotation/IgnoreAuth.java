package com.aratek.framework.core.annotation;

import java.lang.annotation.*;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 忽略AOP权限验证的自定义注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {
}
