package com.aratek.framework.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description web MVC配置类
 */
@Configuration
public class WebMVCConfig extends WebMvcConfigurerAdapter {

    /**
     * cors
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders(CorsConfiguration.ALL)
                .allowedOrigins(CorsConfiguration.ALL)
//                .allowedOrigins(HttpContextUtil.getHttpServletRequest().getHeader("origin"))
                .allowedMethods(CorsConfiguration.ALL)
                .exposedHeaders(
                        "code",
                        "message",
                        "access-control-allow-headers",
                        "access-control-allow-methods",
                        "access-control-allow-origin",
                        "access-control-max-age",
                        "X-Frame-Options"
                )
                .allowCredentials(false)
                .maxAge(3600);
    }

//    @Bean
//    public LocaleResolver localeResolver() {
//        SessionLocaleResolver slr = new SessionLocaleResolver();
//        // 默认语言
//        slr.setDefaultLocale(Locale.US);
//        return slr;
//    }

//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
//        // 参数名
//        lci.setParamName("lang");
//        return lci;
//    }

//    @Bean
//    public MyInterceptor myInterceptor() {
//        MyInterceptor myInterceptor = new MyInterceptor();
//        return myInterceptor;
//    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(localeChangeInterceptor());
//        registry.addInterceptor(myInterceptor());
//        super.addInterceptors(registry);
//    }

    /**
     * 配置静态访问资源
     * @param registry
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
////        registry.addResourceHandler("**/**").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
////        registry.addResourceHandler("/api/swagger/**").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/swagger-resources/**").addResourceLocations("classpath:/META-INF/resources/");
////        registry.addResourceHandler("/api/v2/**").addResourceLocations("classpath:/META-INF/resources/");
////        super.addResourceHandlers(registry);
//    }
}
