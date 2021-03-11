//package com.aratek.framework.core.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
///**
// * @Author 姜寄羽
// * 设置DataSource基本变量
// * @Date 2018/4/28 17:59
// */
//@Component
//public class DataSourceConfig {
//
//    /**
//     * DB config
//     *****************************************************************************************************/
//    @Value("${spring.datasource.url:}")
//    private String url;
//    @Value("${spring.datasource.username:}")
//    private String username;
//    @Value("${spring.datasource.password:}")
//    private String password;
//    @Value("${spring.datasource.driverClassName:}")
//    private String driverClassName;
//
//    @Value("${datasource.runtimeLoad:false}")
//    private boolean runtimeLoad;
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
//     * 打开PSCache，并且指定每个连接上PSCache的大小
//     */
//    @Value("${druid.poolPreparedStatements:true}")
//    private boolean poolPreparedStatements;
//
//    /**
//     * 指定每个连接上PSCache的大小
//     */
//    @Value("${druid.maxPoolPreparedStatementPerConnectionSize:20}")
//    private int maxPoolPreparedStatementPerConnectionSize;
//
//    /**
//     * 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
//     */
//    @Value("${druid.filters:stat,wall,slf4j}")
//    private String filters;
//
//    /**
//     * 通过connectProperties属性来打开mergeSql功能；慢SQL记录
//     */
//    @Value("${druid.connectionProperties:druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000}")
//    private String connectionProperties;
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//
//    public String getPassword() {
//        return password;
//    }
//
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//
//    public String getDriverClassName() {
//        return driverClassName;
//    }
//
//
//    public void setDriverClassName(String driverClassName) {
//        this.driverClassName = driverClassName;
//    }
//
//
//    public int getInitialSize() {
//        return initialSize;
//    }
//
//
//    public void setInitialSize(int initialSize) {
//        this.initialSize = initialSize;
//    }
//
//
//    public int getMinIdle() {
//        return minIdle;
//    }
//
//
//    public void setMinIdle(int minIdle) {
//        this.minIdle = minIdle;
//    }
//
//
//    public int getMaxActive() {
//        return maxActive;
//    }
//
//
//    public void setMaxActive(int maxActive) {
//        this.maxActive = maxActive;
//    }
//
//
//    public int getMaxWait() {
//        return maxWait;
//    }
//
//
//    public void setMaxWait(int maxWait) {
//        this.maxWait = maxWait;
//    }
//
//
//    public int getTimeBetweenEvictionRunsMillis() {
//        return timeBetweenEvictionRunsMillis;
//    }
//
//
//    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
//        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
//    }
//
//
//    public int getMinEvictableIdleTimeMillis() {
//        return minEvictableIdleTimeMillis;
//    }
//
//
//    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
//        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
//    }
//
//
//    public String getValidationQuery() {
//        return validationQuery;
//    }
//
//
//    public void setValidationQuery(String validationQuery) {
//        this.validationQuery = validationQuery;
//    }
//
//
//    public boolean isTestWhileIdle() {
//        return testWhileIdle;
//    }
//
//
//    public void setTestWhileIdle(boolean testWhileIdle) {
//        this.testWhileIdle = testWhileIdle;
//    }
//
//
//    public boolean isTestOnBorrow() {
//        return testOnBorrow;
//    }
//
//
//    public void setTestOnBorrow(boolean testOnBorrow) {
//        this.testOnBorrow = testOnBorrow;
//    }
//
//
//    public boolean isTestOnReturn() {
//        return testOnReturn;
//    }
//
//
//    public void setTestOnReturn(boolean testOnReturn) {
//        this.testOnReturn = testOnReturn;
//    }
//
//
//    public boolean isPoolPreparedStatements() {
//        return poolPreparedStatements;
//    }
//
//
//    public void setPoolPreparedStatements(boolean poolPreparedStatements) {
//        this.poolPreparedStatements = poolPreparedStatements;
//    }
//
//
//    public int getMaxPoolPreparedStatementPerConnectionSize() {
//        return maxPoolPreparedStatementPerConnectionSize;
//    }
//
//
//    public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
//        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
//    }
//
//
//    public String getFilters() {
//        return filters;
//    }
//
//
//    public void setFilters(String filters) {
//        this.filters = filters;
//    }
//
//
//    public String getConnectionProperties() {
//        return connectionProperties;
//    }
//
//
//    public void setConnectionProperties(String connectionProperties) {
//        this.connectionProperties = connectionProperties;
//    }
//
//    public boolean isRuntimeLoad() {
//        return runtimeLoad;
//    }
//
//    public void setRuntimeLoad(boolean runtimeLoad) {
//        this.runtimeLoad = runtimeLoad;
//    }
//}
