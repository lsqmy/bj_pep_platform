package com.aratek.framework.domain.core;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 系统日志对象
 */
public class SysLogEntity implements Serializable {

    private static final long serialVersionUID = -1725873102067837517L;

    //ID
    private String id;
    //用户ID
    private String userID;
    //APPID
    private String appID;
    //用户名
    private String username;
    //用户操作
    private String operation;
    //请求class name + method name
    private String method;
    //请求参数
    private String params;
    //执行时长(毫秒)
    private Long time;
    //IP地址
    private String ip;
    //平台
    private String platForm;
    //设备编号
    private String equipmentNo;
    //创建时间
    private Date createDate;
    //token
    private String token;

    /**
     * 设置：
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public String getId() {
        return id;
    }



    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    /**
     * 设置：用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取：用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置：用户操作
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * 获取：用户操作
     */
    public String getOperation() {
        return operation;
    }

    /**
     * 设置：请求方法
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 获取：请求方法
     */
    public String getMethod() {
        return method;
    }

    /**
     * 设置：请求参数
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * 获取：请求参数
     */
    public String getParams() {
        return params;
    }

    /**
     * 获取：执行时长(毫秒)
     */
    public Long getTime() {
        return time;
    }

    /**
     * 设置：执行时长(毫秒)
     */
    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * 设置：IP地址
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取：IP地址
     */
    public String getIp() {
        return ip;
    }

    public String getPlatForm() {
        return platForm;
    }

    public void setPlatForm(String platForm) {
        this.platForm = platForm;
    }

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    /**
     * 设置：创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
