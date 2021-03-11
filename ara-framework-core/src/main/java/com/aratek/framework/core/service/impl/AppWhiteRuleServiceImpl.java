package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.AppInfoWhiteRuleDAO;
import com.aratek.framework.core.service.AppWhiteRuleService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.AppInfoWhiteRule;
import com.github.pagehelper.PageHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-06-22
 * @description APP升级白名单 实现类
 */
@Service
public class AppWhiteRuleServiceImpl implements AppWhiteRuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppWhiteRuleServiceImpl.class);

    @Autowired
    private AppInfoWhiteRuleDAO appInfoWhiteRuleDAO;

    @Override
    public List<AppInfoWhiteRule> selectAppInfoWhiteRuleList(AppInfoWhiteRule appInfoWhiteRule) {
        PageHelper.startPage(appInfoWhiteRule.getPageNum(), appInfoWhiteRule.getPageSize());
        return appInfoWhiteRuleDAO.selectAppInfoWhiteRuleList(appInfoWhiteRule);
    }

    @Override
    public Map exportAppInfoWhiteRuleList(AppInfoWhiteRule appInfoWhiteRule) {
        //1.查询数据,是否查全部数据由前台传参控制
        PageHelper.startPage(appInfoWhiteRule.getPageNum(), appInfoWhiteRule.getPageSize(), PageUtil.getSortStr(appInfoWhiteRule.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (appInfoWhiteRule.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<AppInfoWhiteRule> appInfoWhiteRuleList = appInfoWhiteRuleDAO.selectAppInfoWhiteRuleList(appInfoWhiteRule);
        if (appInfoWhiteRuleList == null || appInfoWhiteRuleList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_APP_INFO_WHITE_RULE);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_APP_INFO_WHITE_RULE);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < appInfoWhiteRuleList.size(); i++) {
            AppInfoWhiteRule appInfoWhiteRule2 = appInfoWhiteRuleList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fStatus", ExcelUtil.dealWithStatus(appInfoWhiteRule2.getfStatus()));
            lm.put("fAppID", appInfoWhiteRule2.getfAppID());
            lm.put("fWhiteType", ExcelUtil.dealWithWhiteType(appInfoWhiteRule2.getfWhiteType()));
            lm.put("fCheckType", ExcelUtil.dealWithCheckType(appInfoWhiteRule2.getfCheckType()));
            lm.put("fValueOne", appInfoWhiteRule2.getfValueOne());
            lm.put("fValueTwo", appInfoWhiteRule2.getfValueTwo());
            lm.put("fCreatorName", appInfoWhiteRule2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(appInfoWhiteRule2.getfCreateTime()));
            lm.put("fLastUpdateUserName", appInfoWhiteRule2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(appInfoWhiteRule2.getfLastUpdateTime()));
            lm.put("fDescription", appInfoWhiteRule2.getfDescription());
            //add to list
            listMap.add(lm);
        }
        map.put("mapList", listMap);
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        if (workbook == null) {
            LOGGER.error("生成excel失败!");
            return Result.error(AraResultCodeConstants.CODE_1602);
        }
        //test
        /*File savefile = new File("D:/temp/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_APP_INFO_WHITE_RULE + ".xlsx");
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        //4.指定下载的文件名--设置响应头
        HttpServletResponse response = HttpContextUtil.getHttpServletResponse();
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_APP_INFO_WHITE_RULE, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportAppInfoWhiteRuleList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportAppInfoWhiteRuleList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertAppInfoWhiteRule(AppInfoWhiteRule appInfoWhiteRule) {
        //生成ID
        appInfoWhiteRule.setfID(UUIDUtil.genID());
        //设置状态初始值
        appInfoWhiteRule.setfStatus(AraCoreConstants.STATUS_SAVE);
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(appInfoWhiteRule);
        //insert
        appInfoWhiteRuleDAO.insertAppInfoWhiteRule(appInfoWhiteRule);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateAppInfoWhiteRule(AppInfoWhiteRule appInfoWhiteRule) {
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(appInfoWhiteRule);
        //update
        appInfoWhiteRuleDAO.updateAppInfoWhiteRule(appInfoWhiteRule);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateAppInfoWhiteRuleStatusBatch(List<AppInfoWhiteRule> appInfoWhiteRuleList) {
        appInfoWhiteRuleDAO.updateAppInfoWhiteRuleStatusBatch(appInfoWhiteRuleList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteAppInfoWhiteRuleBatch(List<String> idList) {
        appInfoWhiteRuleDAO.deleteAppInfoWhiteRuleBatch(idList);
        return Result.ok();
    }
}
