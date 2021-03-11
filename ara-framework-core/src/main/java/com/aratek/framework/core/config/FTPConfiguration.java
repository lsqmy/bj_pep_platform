package com.aratek.framework.core.config;

import com.aratek.framework.core.profile.FtpConfigProperties;
import com.aratek.framework.core.util.FtpUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * @author shijinlong
 * @date 2018-05-16
 * @description FTP配置类
 */
@Configuration
@ConditionalOnClass({GenericObjectPool.class, FTPClient.class})
@ConditionalOnProperty(value = "ftp.enabled", havingValue = "true")
@EnableConfigurationProperties(FtpConfigProperties.class)
public class FTPConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(FTPConfiguration.class);

    private ObjectPool<FTPClient> pool;

    public FTPConfiguration(FtpConfigProperties props) {
        // 默认最大连接数与最大空闲连接数都为8，最小空闲连接数为0
        // 其他未设置属性使用默认值，可根据需要添加相关配置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(60000);
        poolConfig.setSoftMinEvictableIdleTimeMillis(50000);
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);
        pool = new GenericObjectPool<FTPClient>(new FtpClientPooledObjectFactory(props), poolConfig);
        preLoadingFtpClient(props.getInitialSize(), poolConfig.getMaxIdle());
        // 初始化ftp工具类中的ftpClientPool
        FtpUtil.init(pool);
    }

    /**
     * 预先加载FTPClient连接到对象池中
     *
     * @param initialSize 初始化连接数
     * @param maxIdle     最大空闲连接数
     */
    private void preLoadingFtpClient(Integer initialSize, int maxIdle) {
        if (initialSize == null || initialSize <= 0) {
            return;
        }

        int size = Math.min(initialSize.intValue(), maxIdle);
        for (int i = 0; i < size; i++) {
            try {
                pool.addObject();
            } catch (Exception e) {
                LOGGER.error("preLoadingFtpClient error...", e);
            }
        }
    }

    @PreDestroy
    public void destroy() {
        if (pool != null) {
            pool.close();
            LOGGER.info("销毁ftpClientPool...");
        }
    }

    /**
     * FtpClient对象工厂类
     */
    static class FtpClientPooledObjectFactory implements PooledObjectFactory<FTPClient> {

        private FtpConfigProperties props;

        public FtpClientPooledObjectFactory(FtpConfigProperties props) {
            this.props = props;
        }

        @Override
        public PooledObject<FTPClient> makeObject() throws Exception {
            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect(props.getHost(), props.getPort());
                ftpClient.login(props.getUsername(), props.getPassword());
                LOGGER.debug("ftp.host={}", props.getHost());
                LOGGER.debug("ftp.port={}", props.getPort());
                LOGGER.debug("ftp.username={}", props.getUsername());
//                LOGGER.debug("ftp.password={}", props.getPassword());
                LOGGER.debug("连接FTP服务器返回码{}", ftpClient.getReplyCode());
                ftpClient.setBufferSize(props.getBufferSize());
                ftpClient.setControlEncoding(props.getEncoding());
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                return new DefaultPooledObject<FTPClient>(ftpClient);
            } catch (Exception e) {
                LOGGER.error("建立FTP连接失败", e);
                if (ftpClient.isAvailable()) {
                    ftpClient.disconnect();
                }
                ftpClient = null;
                throw new Exception("建立FTP连接失败", e);
            }
        }

        @Override
        public void destroyObject(PooledObject<FTPClient> p) throws Exception {
            FTPClient ftpClient = getObject(p);
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        }

        @Override
        public boolean validateObject(PooledObject<FTPClient> p) {
            FTPClient ftpClient = getObject(p);
            if (ftpClient == null || !ftpClient.isConnected()) {
                return false;
            }
            try {
                ftpClient.changeWorkingDirectory("/");
                return true;
            } catch (Exception e) {
                LOGGER.error("验证FTP连接失败::{}", e);
                return false;
            }
        }

        @Override
        public void activateObject(PooledObject<FTPClient> p) throws Exception {
        }

        @Override
        public void passivateObject(PooledObject<FTPClient> p) throws Exception {
        }

        private FTPClient getObject(PooledObject<FTPClient> p) {
            if (p == null || p.getObject() == null) {
                return null;
            }
            return p.getObject();
        }

    }
}
