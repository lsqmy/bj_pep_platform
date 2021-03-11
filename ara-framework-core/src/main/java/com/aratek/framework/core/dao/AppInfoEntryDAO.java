package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.AppInfoEntry;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-17
 * @description App升级信息明细DAO
 */
public interface AppInfoEntryDAO extends BaseDAO<AppInfoEntry> {

    /**
     * 查询App升级信息明细列表
     *
     * @param appInfoEntry
     * @return
     */
    List<AppInfoEntry> selectAppInfoEntryList(AppInfoEntry appInfoEntry);

    /**
     * 新增App升级信息明细
     *
     * @param appInfoEntry
     * @return
     */
    int insertAppInfoEntry(AppInfoEntry appInfoEntry);

    /**
     * 批量新增App升级信息明细
     *
     * @param appInfoEntryList
     * @return
     */
    int insertAppInfoEntryBatch(List<AppInfoEntry> appInfoEntryList);

    /**
     * 批量删除App升级信息明细
     *
     * @param appInfoEntryList
     * @return
     */
    int deleteAppInfoEntryBatch(List<AppInfoEntry> appInfoEntryList);
}
