package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.AppInfoWhiteRule;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-06-22
 * @description APP升级白名单 接口类
 */
public interface AppWhiteRuleService {


    List<AppInfoWhiteRule> selectAppInfoWhiteRuleList(AppInfoWhiteRule appInfoWhiteRule);

    Map exportAppInfoWhiteRuleList(AppInfoWhiteRule appInfoWhiteRule);

    Map insertAppInfoWhiteRule(AppInfoWhiteRule appInfoWhiteRule);

    Map updateAppInfoWhiteRule(AppInfoWhiteRule appInfoWhiteRule);

    Map updateAppInfoWhiteRuleStatusBatch(List<AppInfoWhiteRule> appInfoWhiteRuleList);

    Map deleteAppInfoWhiteRuleBatch(List<String> idList);


}
