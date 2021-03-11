package com.aratek.framework.core.aspect;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.constant.AraCoreConstants;
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
 * @date 2018-04-28
 * @description 日志记录，切面处理类
 */
@Aspect
@Component
@Order(-5)
public class SysLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysLogAspect.class);

    @Value("${ara.log.sysLogEnabled:false}")
    private String sysLogEnabled;

    /**
     * 加上@SysLog注解的方法需要记录日志
     */
    @Pointcut("@annotation(com.aratek.framework.core.annotation.SysLogAnnotation)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (AraCoreConstants.TRUE.equalsIgnoreCase(sysLogEnabled)) {
            //记录日志
            saveSysLog(point, time);
        }
        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogEntity sysLogEntity = new SysLogEntity();
        SysLogAnnotation syslog = method.getAnnotation(SysLogAnnotation.class);
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

        //获取request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        //设置IP地址
        sysLogEntity.setIp(IPUtil.getIpAddr(request));
        //设置设备编号
        sysLogEntity.setEquipmentNo(request.getHeader("EquipmentNo"));
        //设置平台
        sysLogEntity.setPlatForm(request.getHeader("PlatForm"));
        //token
        String token = request.getHeader(AraCoreConstants.AUTH_KEY);
        if (StringUtil.isNotBlank(token)) {
            //设置token
            sysLogEntity.setToken(token);
            //get id、name from token
            try {
                Claims claims = JWTUtil.getClaimsFromToken(token);
                //用户ID
                String userID = (String) claims.get("fID");
                sysLogEntity.setUserID(userID);
                //用户名
                String userName = (String) claims.get("fName");
                sysLogEntity.setUsername(userName);
            } catch (Exception e) {
            }
        }
        sysLogEntity.setTime(time);
        sysLogEntity.setCreateDate(new Date());
        //打印request日志
        LOGGER.info("====SysLog.request.log:{}", JsonUtil.toJson(sysLogEntity));
    }

}
