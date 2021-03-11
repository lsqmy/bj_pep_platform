package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.AppInfo;
import com.aratek.framework.domain.core.AppInfoEntry;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-17
 * @description App服务 接口类
 */
public interface AppService {


    /**
     * 分页查询App升级信息列表
     *
     * @param appInfo
     * @return
     */
    List<AppInfo> selectAppInfoList(AppInfo appInfo);

    /**
     * 导出App升级信息列表
     *
     * @param appInfo
     * @return
     */
    Map exportAppInfoList(AppInfo appInfo);

    /**
     * 新增App升级信息
     *
     * @param appInfo
     * @return
     */
    Map insertAppInfo(AppInfo appInfo);

    /**
     * 修改App升级信息
     *
     * @param appInfo
     * @return
     */
    Map updateAppInfo(AppInfo appInfo);

    /**
     * 批量删除App升级信息
     *
     * @param idList
     * @return
     */
    Map deleteAppInfoBatch(List<String> idList);

    /**
     * 查询App升级信息明细列表
     *
     * @param appInfoEntry
     * @return
     */
    List<AppInfoEntry> selectAppInfoEntryList(AppInfoEntry appInfoEntry);

    /**
     * 批量新增App升级信息明细
     *
     * @param appInfo
     * @return
     */
    Map insertAppInfoEntryBatch(AppInfo appInfo);

    /**
     * 批量删除App升级信息明细
     *
     * @param appInfoEntryList
     * @return
     */
    Map deleteAppInfoEntryBatch(List<AppInfoEntry> appInfoEntryList);

    /**
     * 新增App升级信息明细(上传升级附件)
     *
     * @param appID
     * @param parentID
     * @param localDirectory
     * @param file
     * @return
     */
    Map insertAppInfoEntry(String appID, String parentID, String localDirectory, MultipartFile file);
}
