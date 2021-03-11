package com.aratek.framework.core.exception;

import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.constant.AraDomainConstants;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 全局异常处理器
 */
@RestControllerAdvice
public class GlobalRestExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalRestExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = {AraBaseException.class, AraAuthException.class})
    public Map handleAraException(Exception e, HttpServletResponse response) {
        LOGGER.error(e.getMessage(), e);
        if (e instanceof AraAuthException) {
            String code = ((AraAuthException) e).getCode();
            if (StringUtil.isNotBlank(code) && StringUtil.isNumeric(code)) {
                //如果有设置code，并且为数字，响应码就使用code值
                response.setStatus(Integer.parseInt(code));
            } else {
                //没设置就使用403作为http响应码
                response.setStatus(HttpStatus.FORBIDDEN.value());
            }
        } else if (e instanceof AraBaseException) {
            String code = ((AraBaseException) e).getCode();
            if (StringUtil.isNotBlank(code) && StringUtil.isNumeric(code)) {
                //如果有设置code，并且为数字，响应码就使用code值
                response.setStatus(Integer.parseInt(code));
            } else {
                //没设置就使用200作为http响应码
                response.setStatus(HttpStatus.OK.value());
            }
        } else {
            //默认使用200作为http响应码
            response.setStatus(HttpStatus.OK.value());
        }
        return localeResult(Result.error(((AraBaseException) e).getCode(), e.getMessage()));
    }

    /**
     * 处理shiro异常
     */
    @ExceptionHandler(value = {AuthenticationException.class, AuthorizationException.class, ShiroException.class})
    public Map handleShiroException(Exception e, HttpServletResponse response) {
        LOGGER.error(e.getMessage(), e);
        if (e instanceof AuthenticationException) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return localeResult(Result.error(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, e.getMessage()));
        } else if (e instanceof AuthorizationException) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return localeResult(Result.error(AraCoreConstants.HTTP_STATUS_FORBIDDEN, e.getMessage()));
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return localeResult(Result.error(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, e.getMessage()));
        }
//        return new BaseResponse(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return localeResult(Result.error());
    }

    private Map localeResult(Result result) {
        //1.get request
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        //2.get locale
        Locale locale = RequestContextUtils.getLocale(request);
        String code = (String) result.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME);
        if (StringUtil.isBlank(code)) {
            code = AraDomainConstants.DEFAULT_RESULT_CODE_VALUE_ERROR;
        }
        String message = messageSource.getMessage(code, null, locale);
        if (StringUtil.isNotBlank(message)) {
            result.put(AraDomainConstants.DEFAULT_RESULT_MESSAGE_NAME, message);
        }
        return result;
    }
}
