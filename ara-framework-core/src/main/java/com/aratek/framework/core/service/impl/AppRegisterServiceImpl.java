package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.AppRegisterDAO;
import com.aratek.framework.core.service.AppRegisterService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.AppRegister;
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
 * @date 2018-05-23
 * @description APP注册信息 实现类
 */
@Service
public class AppRegisterServiceImpl implements AppRegisterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppRegisterServiceImpl.class);

    @Autowired
    private AppRegisterDAO appRegisterDAO;

    @Override
    public List<AppRegister> selectAppRegisterList(AppRegister appRegister) {
        PageHelper.startPage(appRegister.getPageNum(), appRegister.getPageSize(), PageUtil.getSortStr(appRegister.getSortParams()));
        return appRegisterDAO.selectAppRegisterList(appRegister);
    }

    @Override
    public Map exportAppRegisterList(AppRegister appRegister) {
        //1.查询数据,是否查全部数据由前台传参控制
        PageHelper.startPage(appRegister.getPageNum(), appRegister.getPageSize(), PageUtil.getSortStr(appRegister.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (appRegister.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<AppRegister> appRegisterList = appRegisterDAO.selectAppRegisterList(appRegister);
        if (appRegisterList == null || appRegisterList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_APP_REGISTER);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_APP_REGISTER);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < appRegisterList.size(); i++) {
            AppRegister appRegister2 = appRegisterList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fStatus", ExcelUtil.dealWithStatus(appRegister2.getfStatus()));
            lm.put("fAppID", appRegister2.getfAppID());
            lm.put("fAppName", appRegister2.getfAppName());
            lm.put("fSecretKey", appRegister2.getfSecretKey());
            lm.put("fCreatorName", appRegister2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(appRegister2.getfCreateTime()));
            lm.put("fLastUpdateUserName", appRegister2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(appRegister2.getfLastUpdateTime()));
            lm.put("fDescription", appRegister2.getfDescription());
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
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_APP_REGISTER + ".xlsx");
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
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_APP_REGISTER, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportAppRegisterList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportAppRegisterList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    public AppRegister selectAppRegisterByID(String id) {
        return appRegisterDAO.selectAppRegisterByID(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertAppRegister(AppRegister appRegister) {
        //校验AppID
        if (appRegisterDAO.selectAppRegisterByAppID(appRegister.getfAppID()) != null) {
            LOGGER.debug("AppID已存在!");
            return Result.error(AraResultCodeConstants.CODE_1401);
        }
        //生成ID
        appRegister.setfID(UUIDUtil.genID());
        //设置状态初始值
        appRegister.setfStatus(AraCoreConstants.STATUS_SAVE);
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(appRegister);
        //insert
        appRegisterDAO.insertAppRegister(appRegister);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateAppRegister(AppRegister appRegister) {
        //校验AppID
        if (StringUtil.isNotBlank(appRegister.getfAppID())) {
            AppRegister dbAppRegister = appRegisterDAO.selectAppRegisterByAppID(appRegister.getfAppID());
            if (dbAppRegister != null && !appRegister.getfID().equals(dbAppRegister.getfID())) {
                LOGGER.debug("AppID已存在!");
                return Result.error(AraResultCodeConstants.CODE_1401);
            }
        }
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(appRegister);
        //update
        appRegisterDAO.updateAppRegister(appRegister);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateAppRegisterStatusBatch(List<AppRegister> appRegisterList) {
        //update
        appRegisterDAO.updateAppRegisterStatusBatch(appRegisterList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteAppRegisterByID(String id) {
        //delete
        appRegisterDAO.deleteAppRegisterByID(id);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteAppRegisterBatch(List<String> idList) {
        //delete
        appRegisterDAO.deleteAppRegisterBatch(idList);
        return Result.ok();
    }
}
