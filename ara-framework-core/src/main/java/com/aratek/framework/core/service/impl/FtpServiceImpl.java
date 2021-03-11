package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.FtpInfoDAO;
import com.aratek.framework.core.service.FtpService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.FtpInfo;
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
 * @date 2018-05-22
 * @description ftp服务 实现类
 */
@Service
public class FtpServiceImpl implements FtpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpServiceImpl.class);

    @Autowired
    private FtpInfoDAO ftpInfoDAO;

    @Override
    public List<FtpInfo> selectFtpInfoList(FtpInfo ftpInfo) {
        PageHelper.startPage(ftpInfo.getPageNum(), ftpInfo.getPageSize(), PageUtil.getSortStr(ftpInfo.getSortParams()));
        return ftpInfoDAO.selectFtpInfoList(ftpInfo);
    }

    @Override
    public Map exportFtpInfoList(FtpInfo ftpInfo) {
        //1.查询数据,是否查全部数据由前台传参控制
        PageHelper.startPage(ftpInfo.getPageNum(), ftpInfo.getPageSize(), PageUtil.getSortStr(ftpInfo.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (ftpInfo.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<FtpInfo> ftpInfoList = ftpInfoDAO.selectFtpInfoList(ftpInfo);
        if (ftpInfoList == null || ftpInfoList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_FTP);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_FTP);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < ftpInfoList.size(); i++) {
            FtpInfo ftpInfo2 = ftpInfoList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fStatus", ExcelUtil.dealWithStatus(ftpInfo2.getfStatus()));
            lm.put("fHost", ftpInfo2.getfHost());
            lm.put("fPort", String.valueOf(ftpInfo2.getfPort()));
            lm.put("fUserName", ftpInfo2.getfUserName());
            lm.put("fPassWord", ftpInfo2.getfPassWord());
            lm.put("fCreatorName", ftpInfo2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(ftpInfo2.getfCreateTime()));
            lm.put("fLastUpdateUserName", ftpInfo2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(ftpInfo2.getfLastUpdateTime()));
            lm.put("fDescription", ftpInfo2.getfDescription());
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
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_FTP + ".xlsx");
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
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_FTP, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportFtpInfoList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportFtpInfoList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    public FtpInfo selectFtpInfoByID(String id) {
        return ftpInfoDAO.selectFtpInfoByID(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertFtpInfo(FtpInfo ftpInfo) {
        //生成ID
        ftpInfo.setfID(UUIDUtil.genID());
        //设置状态初始值
        ftpInfo.setfStatus(AraCoreConstants.STATUS_SAVE);
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(ftpInfo);
        //insert
        ftpInfoDAO.insertFtpInfo(ftpInfo);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateFtpInfo(FtpInfo ftpInfo) {
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(ftpInfo);
        //update
        ftpInfoDAO.updateFtpInfo(ftpInfo);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateFtpInfoStatusBatch(List<FtpInfo> ftpInfoList) {
        //update
        ftpInfoDAO.updateFtpInfoStatusBatch(ftpInfoList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteFtpInfoByID(String id) {
        //delete
        ftpInfoDAO.deleteFtpInfoByID(id);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteFtpInfoBatch(List<String> idList) {
        //delete
        ftpInfoDAO.deleteFtpInfoBatch(idList);
        return Result.ok();
    }
}
