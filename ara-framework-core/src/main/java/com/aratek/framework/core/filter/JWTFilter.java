package com.aratek.framework.core.filter;

import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.shiro.JWTToken;
import com.aratek.framework.core.util.JWTUtil;
import com.aratek.framework.core.util.JsonUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shijinlong
 * @date 2018-05-08
 * @description JWT token的filter，每次请求提交给shiro做login验证
 */
public class JWTFilter extends AccessControlFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTFilter.class);

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
//            LOGGER.debug("====JWTFilter.preHandle.OPTIONS====");
            httpServletResponse.setHeader("Access-Control-Allow-Origin", CorsConfiguration.ALL);
            httpServletResponse.setHeader("Access-Control-Allow-Methods", CorsConfiguration.ALL);
            httpServletResponse.setHeader("Access-Control-Allow-Headers", CorsConfiguration.ALL);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        LOGGER.debug("====JWTFilter.onAccessDenied====");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(AraCoreConstants.AUTH_KEY);
//        LOGGER.debug("====JWTFilter.onAccessDenied.authorization:{}", authorization);
        //==============test begin================
//        //开发阶段自动生成token
//        User user = new User();
//        user.setfName("admin");
//        user.setfID("9df1dbbe28e642e3b75e0b1ba0d7b345");
//        authorization = JWTUtil.createToken(user, System.currentTimeMillis() + 30 * 60 * 1000);
        //==============test end================
//        UsernamePasswordToken shiroToken = new UsernamePasswordToken(authorization, authorization);
        if (StringUtil.isBlank(authorization)) {
//            LOGGER.debug("====JWTFilter.onAccessDenied.authorization.isBlank");
            unAuthorized(response, null);
            return false;
        }
        //校验token
        if (!JWTUtil.validateToken(authorization, null)) {
            unAuthorized(response, "登录超时！");
            return false;
        }
        JWTToken shiroToken = new JWTToken(authorization, authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        try {
            getSubject(request, response).login(shiroToken);
        } catch (AuthenticationException e) {
            LOGGER.error("onAccessDenied.error:{}", e);
            forbidden(response);
            return false;
        }
        return true;
    }

    /**
     * 401
     *
     * @param response
     * @throws IOException
     */
    private void unAuthorized(ServletResponse response, String message) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setHeader("Content-Type","application/json;charset=UTF-8");
        String result;
        if (StringUtil.isNotBlank(message)) {
            result = JsonUtil.toJson(Result.error(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, message));
        } else {
            result = JsonUtil.toJson(Result.error(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "User authentication failed!"));
        }
        httpServletResponse.getWriter().write(result);
    }

    /**
     * 403
     *
     * @param response
     * @throws IOException
     */
    private void forbidden(ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.getWriter().write(JsonUtil.toJson(Result.error(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "User has no authority!")));
    }

}
