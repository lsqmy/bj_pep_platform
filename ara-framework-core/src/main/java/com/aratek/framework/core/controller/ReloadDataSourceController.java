//package com.aratek.framework.core.controller;
//
//import com.aratek.framework.core.annotation.IgnoreAuth;
//import com.aratek.framework.core.config.DataSourceConfig;
//import com.aratek.framework.core.config.DruidConfig;
//import com.aratek.framework.core.config.VisualDataSource;
//import com.aratek.framework.core.util.AraFileUtils;
//import com.aratek.framework.core.util.JsonUtil;
//import com.aratek.framework.domain.base.Result;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.io.FileUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.File;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
///**
// * @Author 姜寄羽
// * 运行时重载数据源
// * @Date 2018/4/28 17:19
// */
//@Api(value = "重载数据源", description = "启动或者重新部署时设置数据库", tags = "数据库配置")
//@RestController
//@RequestMapping("/dataSource")
//public class ReloadDataSourceController extends BaseController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadDataSourceController.class);
//
//    @Value("${datasource.configFileName:ara-datasource.json}")
//    private String configFileName;
//
//    @Autowired
//    DruidConfig druidConfig;
//
//    @Autowired
//    DataSourceConfig dataSourceConfig;
//
//    /**
//     * 修改数据库配置
//     * 仅在runtimeLoad开启时有效，如果关闭，则一直使用自带的配置文件
//     *
//     * @return
//     */
//    @IgnoreAuth
//    @ApiOperation(value = "changeConfig", notes = "修改DataSource设置")
//    @RequestMapping(value = "/changeConfig", method = RequestMethod.POST)
//    public Result changeConfig(@RequestBody DataSourceConfigRequest dataSourceConfigRequest) throws InvocationTargetException, IllegalAccessException {
//        if (dataSourceConfig.isRuntimeLoad()) {
//            try {
//                //检查连接的正确性
//                Class.forName(dataSourceConfig.getDriverClassName()).newInstance();
//                Connection connection = DriverManager.getConnection(dataSourceConfigRequest.getUrl()
//                        , dataSourceConfigRequest.getUsername(), dataSourceConfigRequest.getPassword());
//                connection.close();
//            } catch (InstantiationException e) {
//                LOGGER.warn("check error on db {}", dataSourceConfigRequest.toString());
//                return Result.error("-1", e.getMessage());
//            } catch (SQLException e) {
//                LOGGER.warn("check error on db {}", dataSourceConfigRequest.toString());
//                return Result.error("-1", e.getMessage());
//            } catch (ClassNotFoundException e) {
//                LOGGER.error("Load db driver error", e);
//            }
//            //切换链接
//            BeanUtils.copyProperties(dataSourceConfig, dataSourceConfigRequest);
//            VisualDataSource dataSource = (VisualDataSource) druidConfig.dataSource();
//            dataSource.des();
//            druidConfig.loadDataSource();
//            return Result.ok();
//        } else {
//            return Result.error("-2", "Forbidden to reload dataSource");
//        }
//    }
//
//    /**
//     * 设置一个开关，在设置后禁止修改数据库配置
//     * 关闭初始化开关
//     *
//     * @return
//     */
//    @IgnoreAuth
//    @ApiOperation(value = "closeRuntimeReload", notes = "关闭重载入口")
//    @RequestMapping(value = "/closeRuntimeReload", method = RequestMethod.POST)
//    public Result closeRuntimeReload() throws IOException {
//        VisualDataSource dataSource = (VisualDataSource) druidConfig.dataSource();
//        String userDirPath = AraFileUtils.getUserDirectoryAbsolutePath() + "ara" + File.separator;
//        File configFile = new File(userDirPath + configFileName);
//        if (dataSource.isVisable()) {
//            if (dataSourceConfig.isRuntimeLoad()) {
//                return Result.error("-1", "Datasource already closed");
//            }
//            dataSourceConfig.setRuntimeLoad(false);
//            String dataSourceString = JsonUtil.toJson(dataSourceConfig);
//            FileUtils.forceMkdir(new File(userDirPath));
//            LOGGER.info("config dir {}", userDirPath);
//            FileUtils.writeStringToFile(configFile, dataSourceString, "UTF-8", false);
//            return Result.ok();
//        } else {
//            return Result.error("-2", "DataSource not init");
//        }
//    }
//
//    @IgnoreAuth
//    @ApiOperation(value = "config", notes = "读取数据源配置")
//    @RequestMapping(value = "/config", method = RequestMethod.GET)
//    public Result dataSourceConfig() throws InvocationTargetException, IllegalAccessException {
//        if (dataSourceConfig.isRuntimeLoad()) {
//            DataSourceConfigRequest dataSourceConfigRequest = new DataSourceConfigRequest();
//            BeanUtils.copyProperties(dataSourceConfigRequest, dataSourceConfig);
//            return Result.ok().put("dataSource", dataSourceConfigRequest);
//        } else {
//            return Result.error("-2", "Not allow to reload db");
//        }
//    }
//
//    @IgnoreAuth
//    @ApiOperation(value = "isReady", notes = "检查数据源是否加载完毕")
//    @RequestMapping(value = "/isReady", method = RequestMethod.GET)
//    public Result dataSourceIsReady() {
//        VisualDataSource dataSource = (VisualDataSource) druidConfig.dataSource();
//        if (dataSource.isVisable()) {
//            return Result.ok();
//        } else {
//            return Result.error();
//        }
//    }
//
//
//    public static class DataSourceConfigRequest {
//        private String url;
//        private String username;
//        private String password;
//        private String driverClassName;
//        private Integer initialSize;
//        private Integer minIdle;
//        private Integer maxActive;
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//
//        public String getUsername() {
//            return username;
//        }
//
//        public void setUsername(String username) {
//            this.username = username;
//        }
//
//        public String getPassword() {
//            return password;
//        }
//
//        public void setPassword(String password) {
//            this.password = password;
//        }
//
//        public String getDriverClassName() {
//            return driverClassName;
//        }
//
//        public void setDriverClassName(String driverClassName) {
//            this.driverClassName = driverClassName;
//        }
//
//        public Integer getInitialSize() {
//            return initialSize;
//        }
//
//        public void setInitialSize(Integer initialSize) {
//            this.initialSize = initialSize;
//        }
//
//        public Integer getMinIdle() {
//            return minIdle;
//        }
//
//        public void setMinIdle(Integer minIdle) {
//            this.minIdle = minIdle;
//        }
//
//        public Integer getMaxActive() {
//            return maxActive;
//        }
//
//        public void setMaxActive(Integer maxActive) {
//            this.maxActive = maxActive;
//        }
//
//        @Override
//        public String toString() {
//            return "DataSourceConfigRequest{" +
//                    "url='" + url + '\'' +
//                    ", username='" + username + '\'' +
//                    ", password='" + password + '\'' +
//                    ", driverClassName='" + driverClassName + '\'' +
//                    ", initialSize=" + initialSize +
//                    ", minIdle=" + minIdle +
//                    ", maxActive=" + maxActive +
//                    '}';
//        }
//    }
//}
