package com.aratek.framework.core.aspect;

import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.service.UserLogService;
import com.aratek.framework.core.util.HttpContextUtil;
import com.aratek.framework.core.util.IPUtil;
import com.aratek.framework.domain.core.UserLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author shijinlong
 * @date 2018-05-09
 * @description 用户操作日志记录，切面处理类
 */
@Aspect
@Component
@Order(3)
public class UserLogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLogAspect.class);

    @Autowired
    private UserLogService userLogService;

    /**
     * 加上UserLogAnnotation注解的接口需要记录用户操作日志
     */
    @Pointcut("@annotation(com.aratek.framework.core.annotation.UserLogAnnotation)")
    public void userLogPointCut() {
    }

    @Before("userLogPointCut()")
    public void doBefore(JoinPoint joinPoint) {
//        LOGGER.debug("UserLogAspect.doBefore");
        UserLog userLog = new UserLog();
        //获取request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String uri = request.getRequestURI();
        String url = request.getRequestURL().toString();
//        LOGGER.debug("uri={}", uri);
//        LOGGER.debug("url={}", url);
        userLog.setfUri(uri);
        //get annotation
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        UserLogAnnotation userLogAnnotation = method.getAnnotation(UserLogAnnotation.class);
        if (userLogAnnotation != null) {
            userLog.setfModel(userLogAnnotation.module());
            userLog.setfActType(userLogAnnotation.actType());
        } else {
            userLog.setfModel(getModuleFromUri(uri));
            userLog.setfActType(getActTypeFromUri(uri));
        }
        //get ip
        userLog.setfIP(IPUtil.getIpAddr(request));
        userLog.setfEquipmentNo(request.getHeader("EquipmentNo"));
        userLogService.insertUserLog(userLog);
    }

    /**
     * 从uri获取actType
     *
     * @param uri
     * @return
     */
    private String getActTypeFromUri(String uri) {
        String actType;
        if (uri.contains("insert")) {
            actType = "insert";
        } else if (uri.contains("update")) {
            actType = "update";
        } else if (uri.contains("delete")) {
            actType = "delete";
        } else if (uri.contains("select")) {
            actType = "select";
        } else {
            actType = "other";
        }
        return actType;
    }

    private String getModuleFromUri(String uri) {
        String module = "unknown module";
        try {
            String[] uris = uri.split("/");
            //取uri倒数第二格
            if (uris != null && uris.length > 1) {
                return uris[uris.length - 1];
            }
        } catch (Exception e) {
            LOGGER.error("getModuleFromUri.error:{}", e);
            return "unknown module";
        }
        return module;
    }
}
