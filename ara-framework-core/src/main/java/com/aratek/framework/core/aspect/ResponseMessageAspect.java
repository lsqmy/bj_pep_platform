package com.aratek.framework.core.aspect;

import com.aratek.framework.core.exception.AraAuthException;
import com.aratek.framework.core.util.HttpContextUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.constant.AraDomainConstants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-08
 * @description 返回json的message数据处理，根据code获取国际化值，切面处理类
 */
@Aspect
@Component
@Order(5)
public class ResponseMessageAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseMessageAspect.class);

    @Autowired
    private MessageSource messageSource;

    /**
     * 默认处理controller下的方法
     */
    @Pointcut("execution(* com.aratek..*.controller..*.*(..)))")
    public void controllerPointCut() {
    }

    /**
     * 后置返回通知
     * 这里需要注意的是:
     * 如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     * 如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     * returning 限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
     */
    @AfterReturning(value = "controllerPointCut()", returning = "resultMap")
    public void doAfterReturning(JoinPoint point, Object resultMap) throws AraAuthException {
        if (resultMap instanceof Map) {
            String code = (String) ((Map) resultMap).get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME);
            if (StringUtil.isBlank(code)) {
                return;
            }
            String message;
            //如果有message并且返回信息不为默认信息则取返回体中的message否则去查国际化文件中的返回信息
            String oldMessage = (String) ((Map) resultMap).get(AraDomainConstants.DEFAULT_RESULT_MESSAGE_NAME);
            if (StringUtil.isNotBlank(oldMessage) && !oldMessage.equals(AraDomainConstants.DEFAULT_RESULT_MESSAGE_VALUE_ERROR) && !oldMessage.equals(AraDomainConstants.DEFAULT_RESULT_MESSAGE_VALUE_OK)) {
                return;
            }
            //1.方式一
//            Locale locale = LocaleContextHolder.getLocale();
            //2.方式二
            HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
            Locale locale = RequestContextUtils.getLocale(request);
//            LOGGER.debug("locale.getLanguage={},locale.getCountry={}", locale.getLanguage(), locale.getCountry());
            try {
                message = messageSource.getMessage(code, null, locale);
            } catch (NoSuchMessageException e) {
                return;
            }

            if (StringUtil.isBlank(message)) {
                return;
            }
//            //如果已有信息就不替换
//            String oldMessage = (String) ((Map) resultMap).get(AraDomainConstants.DEFAULT_RESULT_MESSAGE_NAME);
//            if (StringUtil.isNotBlank(oldMessage)){
//                return;
//            }
            ((Map) resultMap).put(AraDomainConstants.DEFAULT_RESULT_MESSAGE_NAME, message);

        }
    }
}
