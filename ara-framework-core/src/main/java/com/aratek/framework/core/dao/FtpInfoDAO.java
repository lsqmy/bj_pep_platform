package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.FtpInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-22
 * @description ftp DAO
 */
public interface FtpInfoDAO extends BaseDAO<FtpInfo> {

    /**
     * 查询ftp列表
     *
     * @param ftpInfo
     * @return
     */
    List<FtpInfo> selectFtpInfoList(FtpInfo ftpInfo);

    /**
     * 根据ID查询ftp
     *
     * @param id
     * @return
     */
    FtpInfo selectFtpInfoByID(@Param("id") String id);

    /**
     * 新增ftp
     *
     * @param ftpInfo
     * @return
     */
    int insertFtpInfo(FtpInfo ftpInfo);

    /**
     * 修改ftp信息
     *
     * @param ftpInfo
     * @return
     */
    int updateFtpInfo(FtpInfo ftpInfo);

    /**
     * 批量修改状态
     *
     * @param ftpInfoList
     * @return
     */
    int updateFtpInfoStatusBatch(List<FtpInfo> ftpInfoList);

    /**
     * 根据ID删除ftp
     *
     * @param id
     * @return
     */
    int deleteFtpInfoByID(@Param("id") String id);

    /**
     * 根据ID删除ftp - 批量
     *
     * @param idList
     * @return
     */
    int deleteFtpInfoBatch(List<String> idList);

}
