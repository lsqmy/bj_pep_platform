package com.aratek.framework.core.aspect;

import com.aratek.framework.core.exception.AraAuthException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 权限控制，切面处理类
 */
@Aspect
@Component
@Order(1)
public class AuthAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthAspect.class);

    /**
     * 默认controller下的方法需要权限验证
     */
    @Pointcut("execution(* com.aratek..*.controller..*.*(..)))")
    public void authPointCut() {
    }

    /**
     * 忽略权限验证
     */
    @Pointcut("@annotation(com.aratek.framework.core.annotation.IgnoreAuth)")
    public void ignoreAuthPointCut() {
    }

    @Before("authPointCut() && !ignoreAuthPointCut()")
    public void doBefore(JoinPoint point) throws AraAuthException {
//        LOGGER.debug("====AuthAspect.doBefore.begin...");
        //忽略权限验证的接口
//        IgnoreAuth ignoreAuth = point.getTarget().getClass().getAnnotation(IgnoreAuth.class);
//        if (ignoreAuth != null) {
//            LOGGER.debug("====AuthAspect.doBefore.ignoreAuth != null");
//            return;
//        }
        //验证登录

//        LOGGER.debug("====AuthAspect.doBefore.end...");


//        // 接收到请求，记录请求内容
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        String token = request.getHeader(AraCoreConstants.AUTH_KEY);
//        if (StringUtil.isNotBlank(token)) {
//            //1.check jwt token
//            if (!JWTUtil.validateToken(token,null)) {
//                return;
//            }
//            //2.check permission 略，暂由shiro处理
//        }
//        LOGGER.debug("Token验证失败!Token={}", token);
//        //验证不通过
//        throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "Invalid token!");
    }

}
