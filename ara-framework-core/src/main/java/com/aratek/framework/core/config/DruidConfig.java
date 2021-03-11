//package com.aratek.framework.core.config;
//
//import com.alibaba.druid.filter.logging.Slf4jLogFilter;
//import com.alibaba.druid.filter.stat.StatFilter;
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.support.http.StatViewServlet;
//import com.alibaba.druid.support.http.WebStatFilter;
//import com.alibaba.druid.wall.WallConfig;
//import com.alibaba.druid.wall.WallFilter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import javax.sql.DataSource;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author shijinlong
// * @date 2018-04-28
// * @discription 数据源Druid配置类
// */
//@Configuration
//@ConditionalOnClass({DruidDataSource.class, StatViewServlet.class, WebStatFilter.class})
//public class DruidConfig {
//    private static final Logger LOGGER = LoggerFactory.getLogger(DruidConfig.class);
//
//    /**
//     * DB config
//     *****************************************************************************************************/
//    @Value("${spring.datasource.url:jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&allowMultiQueries=true&failOverReadOnly=false}")
//    private String url;
//    @Value("${spring.datasource.username:demo}")
//    private String username;
//    @Value("${spring.datasource.password:demo}")
//    private String password;
//    @Value("${spring.datasource.driverClassName:com.mysql.jdbc.Driver}")
//    private String driverClassName;
//
//    /** druid config **************************************************************************************************/
//    /**
//     * 连接池初始化大小
//     */
//    @Value("${druid.initialSize:1}")
//    private int initialSize;
//
//    /**
//     * 连接池最小
//     */
//    @Value("${druid.minIdle:3}")
//    private int minIdle;
//
//    /**
//     * 连接池最大
//     */
//    @Value("${druid.maxActive:20}")
//    private int maxActive;
//
//    /**
//     * 配置获取连接等待超时的时间
//     */
//    @Value("${druid.maxWait:60000}")
//    private int maxWait;
//
//    /**
//     * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
//     */
//    @Value("${druid.timeBetweenEvictionRunsMillis:60000}")
//    private int timeBetweenEvictionRunsMillis;
//
//    /**
//     * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
//     */
//    @Value("${druid.minEvictableIdleTimeMillis:30000}")
//    private int minEvictableIdleTimeMillis;
//
//    @Value("${druid.validationQuery:select 'x'}")
//    private String validationQuery;
//
//    @Value("${druid.testWhileIdle:true}")
//    private boolean testWhileIdle;
//
//    @Value("${druid.testOnBorrow:false}")
//    private boolean testOnBorrow;
//
//    @Value("${druid.testOnReturn:false}")
//    private boolean testOnReturn;
//
//    /**
//     * 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
//     */
//    @Value("${druid.filters:stat,slf4j}")
//    private String filters;
//
//    /**
//     * 通过connectProperties属性来打开mergeSql功能；慢SQL记录
//     */
//    @Value("${druid.connectionProperties:druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000}")
//    private String connectionProperties;
//
//    /**
//     * 控制台IP白名单
//     */
//    @Value("${druid.console.whiteIPs:127.0.0.1}")
//    private String whiteIPs;
//
//    /**
//     * 控制台管理用户:用户名
//     */
//    @Value("${druid.console.loginUserName:admin}")
//    private String loginUserName;
//
//    /**
//     * 控制台管理用户:用户密码
//     */
//    @Value("${druid.console.loginPassWord:admin}")
//    private String loginPassWord;
//
//    @Bean
//    public ServletRegistrationBean druidServlet() {
//        LOGGER.info("init Druid Servlet Configuration ");
//        LOGGER.debug("====DruidConfig.druidServlet.whiteIPs={}", whiteIPs);
//        LOGGER.debug("====DruidConfig.druidServlet.loginUserName={}", loginUserName);
//        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
//        // IP白名单
//        servletRegistrationBean.addInitParameter("allow", whiteIPs);
//        // IP黑名单(共同存在时，deny优先于allow)
////        servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
//        //控制台管理用户
//        servletRegistrationBean.addInitParameter("loginUsername", loginUserName);
//        servletRegistrationBean.addInitParameter("loginPassword", loginPassWord);
//        //是否能够重置数据 禁用HTML页面上的“Reset All”功能
//        servletRegistrationBean.addInitParameter("resetEnable", "false");
//        return servletRegistrationBean;
//    }
//
//
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
//        //添加过滤规则
//        filterRegistrationBean.addUrlPatterns("/*");
//        //添加不需要忽略的格式信息
//        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
//        return filterRegistrationBean;
//    }
//
//    @Bean
//    public StatFilter statFilter() {
//        StatFilter statFilter = new StatFilter();
//        statFilter.setMergeSql(true);
//        statFilter.setSlowSqlMillis(5000);
//        return statFilter;
//    }
//
//    @Bean
//    public Slf4jLogFilter slf4jLogFilter() {
//        Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
//        return slf4jLogFilter;
//    }
//
//    @Bean
//    public WallFilter wallFilter() {
//        WallFilter wallFilter = new WallFilter();
//        wallFilter.setConfig(wallConfig());
//        return wallFilter;
//    }
//
//    @Bean
//    public WallConfig wallConfig() {
//        WallConfig config = new WallConfig();
//        //允许一次执行多条语句
//        config.setMultiStatementAllow(true);
//        //允许非基本语句的其他语句
//        config.setNoneBaseStatementAllow(true);
//        return config;
//    }
//
//    @Bean //声明其为Bean实例
//    @Primary //在同样的DataSource中，首先使用被标注的DataSource
//    public DataSource dataSource() {
//        DruidDataSource datasource = new DruidDataSource();
//        datasource.setUrl(url);
//        datasource.setUsername(username);
//        datasource.setPassword(password);
//        datasource.setDriverClassName(driverClassName);
//
//        //configuration
//        datasource.setInitialSize(initialSize);
//        datasource.setMinIdle(minIdle);
//        datasource.setMaxActive(maxActive);
//        datasource.setMaxWait(maxWait);
//        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
//        datasource.setValidationQuery(validationQuery);
//        datasource.setTestWhileIdle(testWhileIdle);
//        datasource.setTestOnBorrow(testOnBorrow);
//        datasource.setTestOnReturn(testOnReturn);
//        //set proxy filters
//        List filterList = new ArrayList();
//        filterList.add(statFilter());
//        filterList.add(slf4jLogFilter());
//        filterList.add(wallFilter());
//        datasource.setProxyFilters(filterList);
////        try {
////            //set filters
////            datasource.setFilters(filters);
////        } catch (SQLException e) {
////            LOGGER.error("druid configuration initialization filter: ", e);
////        }
//        datasource.setConnectionProperties(connectionProperties);
//        return datasource;
//    }
//
//
//}
