package com.aratek.framework.core.config;

import com.aratek.framework.core.filter.JWTFilter;
import com.aratek.framework.core.shiro.AraCredentialsMatcher;
import com.aratek.framework.core.shiro.StatelessDefaultSubjectFactory;
import com.aratek.framework.core.shiro.UserRealm;
import com.aratek.framework.core.util.StringUtil;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-02
 * @description shiro配置类
 */
@Configuration
public class ShiroConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroConfig.class);

    /**
     * 负责shiroBean的生命周期
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /*@Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public EhCacheManager cacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile(AraCoreConstants.EHCACHE_CONFIG_FILE);
        return cacheManager;
    }*/

    /*@Bean(name = "credentialsMatcher")
    public CredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //加密的策略
        credentialsMatcher.setHashAlgorithmName("SHA1");
        //加密的次数
        credentialsMatcher.setHashIterations(1);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }*/

    /**
     * 自定义shiro密码验证类
     *
     * @return
     */
    @Bean(name = "araCredentialsMatcher")
    public CredentialsMatcher araCredentialsMatcher() {
//        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
//        matcher.setHashAlgorithmName("MD5");
//        matcher.setHashIterations(1024);
//        matcher.setStoredCredentialsHexEncoded(true);
        return new AraCredentialsMatcher();
    }

    /**
     * 自定义的认证类，继承AuthorizingRealm，负责用户的认证和权限处理
     */
    @Bean(name = "userRealm")
//    @DependsOn("araCredentialsMatcher")
    public Realm userRealm() {
//        UserRealm userRealm = new UserRealm(hashedCredentialsMatcher());
        UserRealm userRealm = new UserRealm();
        userRealm.setCachingEnabled(true);
        userRealm.setCredentialsMatcher(araCredentialsMatcher());
//        userRealm.setCacheManager(cacheManager());
        return userRealm;
    }

    @Bean(name = "subjectFactory")
    public SubjectFactory subjectFactory() {
        return new StatelessDefaultSubjectFactory();
    }

    /*@Bean(name = "sessionIdCookie")
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("aratek.session.id");
        return simpleCookie;
    }*/

    /*@Bean(name = "sessionManager")
    public SessionManager sessionManager() {
//        AraSessionManager sessionManager = new AraSessionManager();
//        DefaultSessionManager sessionManager = new DefaultSessionManager();
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        sessionManager.setGlobalSessionTimeout();
//        sessionManager.setSessionValidationSchedulerEnabled(false);
        sessionManager.setCacheManager(cacheManager());
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        return sessionManager;
    }*/

    @Bean(name = "rememberMeCookie")
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
//        simpleCookie.setMaxAge(2592000);
        simpleCookie.setMaxAge(60 * 60 * 24);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager getRememberManager() {
        CookieRememberMeManager meManager = new CookieRememberMeManager();
        meManager.setCipherKey(Base64.decode("6ZmI6I2j5Y+R5aSn5ZOlAA=="));
        meManager.setCookie(rememberMeCookie());
        return meManager;
    }

    /**
     * 安全管理器
     * 将realm加入securityManager
     *
     * @return
     */
    @Bean(name = "securityManager")
    @DependsOn("userRealm")
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //set cache manager
//        securityManager.setCacheManager(cacheManager());
        //set realm
        securityManager.setRealm(userRealm());
        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        //set subjectFactory
        securityManager.setSubjectFactory(subjectFactory());
        //set session manager
//        securityManager.setSessionManager(sessionManager());
        //set rememberMe manager
        securityManager.setRememberMeManager(null);
        //设置了SecurityManager采用使用SecurityUtils的静态方法 获取用户等
//        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    /*@Bean(name = "methodInvokingFactoryBean")
    @DependsOn("securityManager")
    public FactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(securityManager());
        return factoryBean;
    }*/


    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * shiro filter 工厂类
     * 1.定义ShiroFilterFactoryBean
     * 2.设置SecurityManager
     * 3.配置拦截器
     * 4.返回定义ShiroFilterFactoryBean
     */
    @Bean(name = "shiroFilter")
//    @DependsOn("methodInvokingFactoryBean")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager,
                                              @Value("${shiro.ignorePaths:}") String ignorePaths) {
        //1
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //2 设置securityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        Map<String, Filter> filters = new HashMap<String, Filter>();
        //配置拦截器,实现无权限返回401,而不是跳转到登录页
        filters.put("jwt", new JWTFilter());
        // 登录的url
//        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后跳转的url
//        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 未授权url
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilters(filters);

        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        // 定义filterChain，静态资源不拦截
//        filterChainDefinitionMap.put("/css/**", "anon");
//        filterChainDefinitionMap.put("/js/**", "anon");
//        filterChainDefinitionMap.put("/fonts/**", "anon");
//        filterChainDefinitionMap.put("/img/**", "anon");
        //登录注销自己实现
        filterChainDefinitionMap.put("/login", "anon");
//        filterChainDefinitionMap.put("/refreshToken", "anon");
        filterChainDefinitionMap.put("/logout", "anon");
//        filterChainDefinitionMap.put("/manage", "anon");
        // 配置退出过滤器，其中具体的退出代码Shiro已经替我们实现了
//        filterChainDefinitionMap.put("/logout", "logout");
        // druid数据源监控页面不拦截
        filterChainDefinitionMap.put("/druid/**", "anon");
        //swagger页面不拦截
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/swagger/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        //根路径
        filterChainDefinitionMap.put("/", "anon");
        //代码生成器路径
        filterChainDefinitionMap.put("/gen/code/**", "anon");
        //增加自定义忽略权限验证的path
        LOGGER.debug("shiroIgnorePaths={}", ignorePaths);
        if (StringUtil.isNotBlank(ignorePaths)) {
            LOGGER.debug("shiroIgnorePaths is not blank");
            String[] pathArray = ignorePaths.split(",");
            for (String path : pathArray) {
                filterChainDefinitionMap.put(path, "anon");
            }
        }
        // 除上以外所有url都必须认证通过才可以访问，未通过认证自动访问LoginUrl
        // 使用自定义jwt filter
        filterChainDefinitionMap.put("/**", "jwt");
//        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

}
