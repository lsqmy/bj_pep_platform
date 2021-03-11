package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.AppInfoDAO;
import com.aratek.framework.core.dao.AppInfoEntryDAO;
import com.aratek.framework.core.service.AppService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.AppInfo;
import com.aratek.framework.domain.core.AppInfoEntry;
import com.github.pagehelper.PageHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author shijinlong
 * @date 2018-05-17
 * @description App服务 实现类
 */
@Service
public class AppServiceImpl implements AppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppServiceImpl.class);

    @Autowired
    private AppInfoDAO appInfoDAO;

    @Autowired
    private AppInfoEntryDAO appInfoEntryDAO;

    @Value("${ftp.basePath:}")
    public String ftpbasepath;


    @Override
    public List<AppInfo> selectAppInfoList(AppInfo appInfo) {
        PageHelper.startPage(appInfo.getPageNum(), appInfo.getPageSize(), PageUtil.getSortStr(appInfo.getSortParams()));
        return appInfoDAO.selectAppInfoList(appInfo);
    }

    @Override
    public Map exportAppInfoList(AppInfo appInfo) {
        //1.查询数据,是否查全部数据由前台传参控制
        PageHelper.startPage(appInfo.getPageNum(), appInfo.getPageSize(), PageUtil.getSortStr(appInfo.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (appInfo.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<AppInfo> appInfoList = appInfoDAO.selectAppInfoList(appInfo);
        if (appInfoList == null || appInfoList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_APP_INFO);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_APP_INFO);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < appInfoList.size(); i++) {
            AppInfo appInfo2 = appInfoList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fForce", ExcelUtil.dealWithYesNo(appInfo2.getfForce()));
            lm.put("fAppID", appInfo2.getfAppID());
            lm.put("fAppName", appInfo2.getfAppName());
            lm.put("fPassWord", appInfo2.getfPassWord());
            lm.put("fAppVersion", appInfo2.getfAppVersion());
            lm.put("fCreatorName", appInfo2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(appInfo2.getfCreateTime()));
            lm.put("fLastUpdateUserName", appInfo2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(appInfo2.getfLastUpdateTime()));
            lm.put("fDescription", appInfo2.getfDescription());
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
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_APP_INFO + ".xlsx");
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
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_APP_INFO, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportAppInfoList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportAppInfoList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertAppInfo(AppInfo appInfo) {
        //校验AppID
        if (appInfoDAO.selectAppInfoByAppID(appInfo.getfAppID()) != null) {
            return Result.error(AraResultCodeConstants.CODE_1401);
        }
        //set id,time
        appInfo.setfID(UUIDUtil.genID());
        Date now = new Date();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        appInfo.setfCreatorID(currentUserID);
        appInfo.setfCreateTime(now);
        appInfo.setfLastUpdateUserID(currentUserID);
        appInfo.setfLastUpdateTime(now);
        //insert
        appInfoDAO.insertSelective(appInfo);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateAppInfo(AppInfo appInfo) {
        //校验AppID
        if (StringUtil.isNotBlank(appInfo.getfAppID())) {
            AppInfo dbAppInfo = appInfoDAO.selectAppInfoByAppID(appInfo.getfAppID());
            if (dbAppInfo != null && !appInfo.getfID().equals(dbAppInfo.getfID())) {
                return Result.error(AraResultCodeConstants.CODE_1401);
            }
        }
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(appInfo);
        //update
        appInfoDAO.updateAppInfo(appInfo);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteAppInfoBatch(List<String> idList) {
        //delete appInfo and appInfoEntry
        appInfoDAO.deleteAppInfoBatch(idList);
        return Result.ok();
    }

    @Override
    public List<AppInfoEntry> selectAppInfoEntryList(AppInfoEntry appInfoEntry) {
//        PageHelper.startPage(appInfoEntry.getPageNum(), appInfoEntry.getPageSize());
        return appInfoEntryDAO.selectAppInfoEntryList(appInfoEntry);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertAppInfoEntryBatch(AppInfo appInfo) {
//        appInfoEntry.setfID(UUIDUtil.genID());
//        appInfoEntryDAO.insertAppInfoEntryBatch(appInfoEntryList);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteAppInfoEntryBatch(List<AppInfoEntry> appInfoEntryList) {
        //1.delete ftp
        for (AppInfoEntry appInfoEntry : appInfoEntryList) {
            try {
                boolean result = FtpUtil.deleteFile(appInfoEntry.getfAppPath());
                LOGGER.debug("result={}", result);
            } catch (IOException e) {
                LOGGER.error("deleteAppInfoEntryBatch.deleteFile.error:{}", e);
                return Result.error(AraResultCodeConstants.CODE_1409);
            }
        }
        //2.delete db
        appInfoEntryDAO.deleteAppInfoEntryBatch(appInfoEntryList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertAppInfoEntry(String appID, String parentID, String localDirectory, MultipartFile file) {
        //1.如果该app下已有此文件,先删除掉记录
        String fileName = file.getOriginalFilename();
        String appPath = ftpbasepath + "/app/" + appID + "/" + fileName;
        String md5;
        try {
            md5 = HashUtil.md5(file.getBytes());
        } catch (IOException e) {
            LOGGER.error("insertAppInfoEntry.md5.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1301);
        }
        Example example = new Example(AppInfoEntry.class);
        example.createCriteria().andEqualTo("fParentID", parentID).andEqualTo("fFileName", fileName);
        appInfoEntryDAO.deleteByExample(example);
        //2.上传ftp
        try {
            FtpUtil.uploadFile2Ftp(file.getInputStream(), appPath);
        } catch (IOException e) {
            LOGGER.error("insertAppInfoEntry.uploadFile2Ftp.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1301);
        }
        //3.记录到数据库
        AppInfoEntry appInfoEntry = new AppInfoEntry();
        appInfoEntry.setfID(UUIDUtil.genID());
        appInfoEntry.setfFileName(fileName);
        appInfoEntry.setfParentID(parentID);
        appInfoEntry.setfLocalDirectory(localDirectory);
        appInfoEntry.setfAppPath(appPath);
        appInfoEntry.setfMD5(md5);
        appInfoEntryDAO.insertAppInfoEntry(appInfoEntry);
        return Result.ok();
    }
}
