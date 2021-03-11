package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.FtpInfo;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-22
 * @description ftp服务 接口类
 */
public interface FtpService {

    /**
     * 查询ftp列表
     *
     * @param ftpInfo ftp
     * @return
     */
    List<FtpInfo> selectFtpInfoList(FtpInfo ftpInfo);

    /**
     * 导出ftp列表
     *
     * @param ftpInfo
     * @return
     */
    Map exportFtpInfoList(FtpInfo ftpInfo);

    /**
     * 根据ID查询ftp
     *
     * @param id ftpID
     * @return
     */
    FtpInfo selectFtpInfoByID(String id);

    /**
     * 新增ftp
     *
     * @param ftpInfo ftp
     * @return
     */
    Map insertFtpInfo(FtpInfo ftpInfo);

    /**
     * 修改ftp信息
     *
     * @param ftpInfo ftp
     * @return
     */
    Map updateFtpInfo(FtpInfo ftpInfo);

    /**
     * 批量修改ftp状态
     *
     * @param ftpInfoList ftpList
     * @return
     */
    Map updateFtpInfoStatusBatch(List<FtpInfo> ftpInfoList);

    /**
     * 根据ID删除ftp
     *
     * @param id ftpID
     * @return
     */
    Map deleteFtpInfoByID(String id);

    /**
     * 根据ID删除ftp - 批量
     *
     * @param idList ftpID List
     * @return
     */
    Map deleteFtpInfoBatch(List<String> idList);


}
