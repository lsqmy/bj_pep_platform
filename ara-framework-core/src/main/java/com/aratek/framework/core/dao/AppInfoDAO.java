package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.AppInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-17
 * @description App升级信息DAO
 */
public interface AppInfoDAO extends BaseDAO<AppInfo> {


    /**
     * 查询App升级信息列表
     *
     * @param appInfo
     * @return
     */
    List<AppInfo> selectAppInfoList(AppInfo appInfo);

    /**
     * 根据AppID查询App升级信息
     *
     * @param fAppID
     * @return
     */
    AppInfo selectAppInfoByAppID(@Param("fAppID") String fAppID);

    int updateAppInfo(AppInfo appInfo);

    /**
     * 批量删除App升级信息
     *
     * @param idList
     * @return
     */
    int deleteAppInfoBatch(List<String> idList);
}
