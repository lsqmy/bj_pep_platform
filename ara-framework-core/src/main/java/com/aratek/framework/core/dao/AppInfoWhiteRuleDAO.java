package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.AppInfoWhiteRule;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-06-22
 * @description APP升级白名单DAO
 */
public interface AppInfoWhiteRuleDAO extends BaseDAO<AppInfoWhiteRule> {


    /**
     * 查询APP升级白名单列表
     *
     * @param appInfoWhiteRule
     * @return
     */
    List<AppInfoWhiteRule> selectAppInfoWhiteRuleList(AppInfoWhiteRule appInfoWhiteRule);

    /**
     * 新增APP升级白名单
     *
     * @param appInfoWhiteRule
     * @return
     */
    int insertAppInfoWhiteRule(AppInfoWhiteRule appInfoWhiteRule);

    /**
     * 修改APP升级白名单
     *
     * @param appInfoWhiteRule
     * @return
     */
    int updateAppInfoWhiteRule(AppInfoWhiteRule appInfoWhiteRule);

    /**
     * 批量修改APP升级白名单状态
     *
     * @param appInfoWhiteRuleList
     * @return
     */
    int updateAppInfoWhiteRuleStatusBatch(List<AppInfoWhiteRule> appInfoWhiteRuleList);

    /**
     * 批量删除APP升级白名单
     *
     * @param idList
     * @return
     */
    int deleteAppInfoWhiteRuleBatch(List<String> idList);
}
