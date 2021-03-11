package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.SysParamDAO;
import com.aratek.framework.core.service.SysParamService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.SysParam;
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
 * @date 2018-05-18
 * @description 系统参数 实现类
 */
@Service
public class SysParamServiceImpl implements SysParamService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysParamServiceImpl.class);

    @Autowired
    private SysParamDAO sysParamDAO;

    @Override
    public List<SysParam> selectSysParamList(SysParam sysParam) {
        PageHelper.startPage(sysParam.getPageNum(), sysParam.getPageSize(), PageUtil.getSortStr(sysParam.getSortParams()));
        return sysParamDAO.selectSysParamList(sysParam);
    }

    @Override
    public Map exportSysParamList(SysParam sysParam) {
        //1.查询数据,是否查全部数据由前台传参控制
        PageHelper.startPage(sysParam.getPageNum(), sysParam.getPageSize(), PageUtil.getSortStr(sysParam.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (sysParam.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<SysParam> sysParamList = sysParamDAO.selectSysParamList(sysParam);
        if (sysParamList == null || sysParamList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_PARAM);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_PARAM);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < sysParamList.size(); i++) {
            SysParam sysParam2 = sysParamList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fNumber", sysParam2.getfNumber());
            lm.put("fName", sysParam2.getfName());
            lm.put("fValue", sysParam2.getfValue());
            lm.put("fCreatorName", sysParam2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(sysParam2.getfCreateTime()));
            lm.put("fLastUpdateUserName", sysParam2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(sysParam2.getfLastUpdateTime()));
            lm.put("fDescription", sysParam2.getfDescription());
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
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_PARAM + ".xlsx");
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
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_PARAM, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportSysParamList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportSysParamList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertSysParam(SysParam sysParam) {
        //校验number
        if (sysParamDAO.selectSysParamByNumber(sysParam.getfNumber()) != null) {
            return Result.error(AraResultCodeConstants.CODE_1004);
        }
        //校验name
        if (sysParamDAO.selectCountByName(sysParam.getfName()) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1005);
        }
        //生成ID
        sysParam.setfID(UUIDUtil.genID());
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(sysParam);
        //insert
        sysParamDAO.insertSysParam(sysParam);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateSysParam(SysParam sysParam) {
        //校验number
        if (StringUtil.isNotBlank(sysParam.getfNumber())) {
            SysParam dbSysParam = sysParamDAO.selectSysParamByNumber(sysParam.getfNumber());
            if (dbSysParam != null) {
                if (!dbSysParam.getfID().equals(sysParam.getfID())) {
                    return Result.error(AraResultCodeConstants.CODE_1004);
                }
            }
        }
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(sysParam);
        //update
        sysParamDAO.updateSysParam(sysParam);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteSysParamByID(String sysParamID) {
        //delete
        sysParamDAO.deleteSysParamByID(sysParamID);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteSysParamBatch(List<String> idList) {
        //delete
        sysParamDAO.deleteSysParamBatch(idList);
        return Result.ok();
    }
}
