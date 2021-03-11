package com.aratek.framework.core.profile;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shijinlong
 * @date 2018-05-16
 * @description Ftp配置属性类，建立ftpClient时使用
 */
@ConfigurationProperties(prefix = "ftp")
public class FtpConfigProperties {

    private String host = "localhost";

    private int port = FTPClient.DEFAULT_PORT;

    private String username;

    private String password;

    private int bufferSize = 8096;

    /**
     * 初始化连接数
     */
    private Integer initialSize = 0;

    /**
     * 编码,默认UTF-8
     */
    private String encoding = "UTF-8";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
