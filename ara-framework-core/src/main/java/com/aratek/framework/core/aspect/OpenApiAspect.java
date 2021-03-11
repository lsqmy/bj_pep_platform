package com.aratek.framework.core.aspect;

import com.aratek.framework.core.annotation.OpenApiAnnotation;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.exception.AraAuthException;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.core.SysLogEntity;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author shijinlong
 * @date 2018-06-28
 * @description Token校验，切面处理类
 */
@Aspect
@Component
@Order(2)
public class OpenApiAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiAspect.class);

    @Value("${ara.log.openApiLogEnabled:false}")
    private String openApiLogEnabled;

    /**
     * JWT token验证
     */
    @Pointcut("@annotation(com.aratek.framework.core.annotation.OpenApiAnnotation)")
    public void tokenPointCut() {
    }

    @Around("tokenPointCut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
//        LOGGER.debug("====OpenApiAspect.doAround.begin...");
        long beginTime = System.currentTimeMillis();
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String token = request.getHeader(AraCoreConstants.AUTH_KEY);
        if (StringUtil.isBlank(token)) {
            throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "Invalid token!");
        }
        if (!JWTUtil.validateTokenForApp(token)) {
            throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "Invalid token!");
        }
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (AraCoreConstants.TRUE.equalsIgnoreCase(openApiLogEnabled)) {
            //记录日志
            saveOpenApiLog(point, time, request);
        }
//        LOGGER.debug("====OpenApiAspect.doAround.end...");
        return result;
    }

    private void saveOpenApiLog(ProceedingJoinPoint joinPoint, long time, HttpServletRequest request) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogEntity sysLogEntity = new SysLogEntity();
        OpenApiAnnotation syslog = method.getAnnotation(OpenApiAnnotation.class);
        if (syslog != null) {
            //注解上的描述
            sysLogEntity.setOperation(syslog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLogEntity.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            String params = JsonUtil.toJson(args[0]);
            sysLogEntity.setParams(params);
        } catch (Exception e) {

        }

        //设置IP地址
        sysLogEntity.setIp(IPUtil.getIpAddr(request));
        //设置设备编号
        sysLogEntity.setEquipmentNo(request.getHeader("EquipmentNo"));
        //设置平台
        sysLogEntity.setPlatForm(request.getHeader("PlatForm"));
        //token
        String token = request.getHeader(AraCoreConstants.AUTH_KEY);
        //设置token
        sysLogEntity.setToken(token);
        //get id、name from token
        try {
            Claims claims = JWTUtil.getClaimsFromToken(token);
            //ID
            String id = (String) claims.get("fID");
            sysLogEntity.setId(id);
            //AppID
            String appID = (String) claims.get("fAppID");
            sysLogEntity.setAppID(appID);
        } catch (Exception e) {
        }
        sysLogEntity.setTime(time);
        sysLogEntity.setCreateDate(new Date());
        //打印request日志
        LOGGER.info("====OpenApi.request.log:{}", JsonUtil.toJson(sysLogEntity));
    }

}
