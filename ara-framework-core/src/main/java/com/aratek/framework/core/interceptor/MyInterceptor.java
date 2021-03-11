//package com.aratek.framework.core.interceptor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @author shijinlong
// * @date 2018-07-02
// * @description interceptor
// */
//public class MyInterceptor implements HandlerInterceptor {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(MyInterceptor.class);
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        LOGGER.debug("====MyInterceptor.preHandle");
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        LOGGER.debug("====MyInterceptor.postHandle");
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        LOGGER.debug("====MyInterceptor.afterCompletion");
//    }
//}
