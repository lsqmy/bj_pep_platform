//package com.aratek.framework.core.config;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.support.http.StatViewServlet;
//import com.alibaba.druid.support.http.WebStatFilter;
//import com.alibaba.druid.wall.WallConfig;
//import com.alibaba.druid.wall.WallFilter;
//import com.alibaba.fastjson.JSONObject;
//import com.aratek.framework.core.util.AraFileUtils;
//import com.aratek.framework.core.util.JsonUtil;
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.io.FileUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import javax.sql.DataSource;
//import java.io.File;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author shijinlong
// * @date 2018-04-28
// * @description 数据源Druid配置类
// */
//@Configuration
//@ConditionalOnClass({DruidDataSource.class, StatViewServlet.class, WebStatFilter.class})
//public class DynamicDruidConfig {
//    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDruidConfig.class);
//
//    @Value("${datasource.runtimeLoad:false}")
//    private boolean runtimeLoad;
//
//    @Value("${datasource.afterStartUp:load}")
//    private String afterStartUp;
//
//    @Value("${datasource.configFileName:ara-datasource.json}")
//    private String configFileName;
//
//    @Value("${datasource.useOldConfig:true}")
//    private boolean useOldConfig;
//
//    private boolean initFlag = false;
//
//    @Autowired
//    DataSourceConfig dataSourceConfig;
//
//    private VisualDataSource visualDataSource;
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
//    @Bean //声明其为Bean实例
//    @Primary //在同样的DataSource中，首先使用被标注的DataSource
//    public DataSource dataSource() {
//        LOGGER.debug("====DruidConfig.dataSource.runtimeLoad={}", runtimeLoad);
//        LOGGER.debug("====DruidConfig.dataSource.afterStartUp={}", afterStartUp);
//        if (visualDataSource == null) {
//            visualDataSource = new VisualDataSource();
//            LOGGER.info("Load visual data source");
//        }
//        if (!initFlag) {
//            try {
//                init();
//            } catch (IOException e) {
//                LOGGER.error("Init exception", e);
//                throw new RuntimeException(e);
//            } catch (InvocationTargetException e) {
//                LOGGER.error("Init exception", e);
//                throw new RuntimeException(e);
//            } catch (IllegalAccessException e) {
//                LOGGER.error("Init exception", e);
//                throw new RuntimeException(e);
//            }
//        }
//        if (!runtimeLoad || "load".equals(afterStartUp)) {
//            loadDataSource();
//        }
//        return visualDataSource;
//    }
//
//    @Bean
//    @Primary
//    //配置事物管理
//    public DataSourceTransactionManager masterTransactionManager() throws SQLException {
//        return new DataSourceTransactionManager(dataSource());
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
//    /**
//     * 创建的时候，先把配置文件写到用户目录下一份 以后默认读这个数据库配置
//     * wait 启动后等待设置
//     * load 启动后自动配置
//     * 仅在runtimeLoad开启时有效，如果关闭，则一直使用自带的配置文件
//     */
//    public void init() throws IOException, InvocationTargetException, IllegalAccessException {
//        String userDirPath = AraFileUtils.getUserDirectoryAbsolutePath() + "ara" + File.separator;
//        File configFile = new File(userDirPath + configFileName);
//        if (!configFile.exists()) {
//            String dataSourceString = JsonUtil.toJson(dataSourceConfig);
//            FileUtils.forceMkdir(new File(userDirPath));
//            LOGGER.info("config dir {}", userDirPath);
//            FileUtils.writeStringToFile(configFile, dataSourceString, "UTF-8", false);
//        } else {
//            if (useOldConfig) {
//                String dataSourceConfig = FileUtils.readFileToString(configFile, "UTF-8");
//                JSONObject dataSource = JSONObject.parseObject(dataSourceConfig);
//                BeanUtils.populate(dataSourceConfig, dataSource);
//            }
//        }
//    }
//
//    public void loadDataSource() {
//        LOGGER.info("Init dataSource now");
//        DruidDataSource datasource = new DruidDataSource();
//        datasource.setUrl(dataSourceConfig.getUrl());
//        datasource.setUsername(dataSourceConfig.getUsername());
//        datasource.setPassword(dataSourceConfig.getPassword());
//        datasource.setDriverClassName(dataSourceConfig.getDriverClassName());
//
//        //configuration
//        datasource.setInitialSize(dataSourceConfig.getInitialSize());
//        datasource.setMinIdle(dataSourceConfig.getMinIdle());
//        datasource.setMaxActive(dataSourceConfig.getMaxActive());
//        datasource.setMaxWait(dataSourceConfig.getMaxWait());
//        datasource.setTimeBetweenEvictionRunsMillis(dataSourceConfig.getTimeBetweenEvictionRunsMillis());
//        datasource.setMinEvictableIdleTimeMillis(dataSourceConfig.getMinEvictableIdleTimeMillis());
//        datasource.setValidationQuery(dataSourceConfig.getValidationQuery());
//        datasource.setTestWhileIdle(dataSourceConfig.isTestWhileIdle());
//        datasource.setTestOnBorrow(dataSourceConfig.isTestOnBorrow());
//        datasource.setTestOnReturn(dataSourceConfig.isTestOnReturn());
//        datasource.setPoolPreparedStatements(datasource.isPoolPreparedStatements());
//        datasource.setMaxPoolPreparedStatementPerConnectionSize(dataSourceConfig.getMaxPoolPreparedStatementPerConnectionSize());
//        //set filter
//        List filterList = new ArrayList();
//        filterList.add(wallFilter());
//        datasource.setProxyFilters(filterList);
////        try {
////            datasource.setFilters(dataSourceConfig.getFilters());
////        } catch (SQLException e) {
////            System.err.println("druid configuration initialization filter: " + e);
////        }
//        datasource.setConnectionProperties(dataSourceConfig.getConnectionProperties());
//        visualDataSource.setDataSource(datasource);
//    }
//
//
//}
