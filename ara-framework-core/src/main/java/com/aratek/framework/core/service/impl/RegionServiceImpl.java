package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.RegionDAO;
import com.aratek.framework.core.service.RegionService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.Region;
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
 * @date 2018-05-15
 * @description 区县服务 实现类
 */
@Service
public class RegionServiceImpl implements RegionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegionServiceImpl.class);

    @Autowired
    private RegionDAO regionDAO;

    @Override
    public List<Region> selectRegionList(Region region) {
        PageHelper.startPage(region.getPageNum(), region.getPageSize(), PageUtil.getSortStr(region.getSortParams()));
        return regionDAO.selectRegionList(region);
    }

    @Override
    public Map exportRegionList(Region region) {
        //1.查询数据,是否查全部数据由前台传参控制
        PageHelper.startPage(region.getPageNum(), region.getPageSize(), PageUtil.getSortStr(region.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (region.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<Region> regionList = regionDAO.selectRegionList(region);
        if (regionList == null || regionList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_REGION);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_REGION);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < regionList.size(); i++) {
            Region region2 = regionList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fStatus", ExcelUtil.dealWithStatus(region2.getfStatus()));
            lm.put("fNumber", region2.getfNumber());
            lm.put("fName", region2.getfName());
            lm.put("fCityName", region2.getfCityName());
            lm.put("fSimpleName", region2.getfSimpleName());
            lm.put("fCreatorName", region2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(region2.getfCreateTime()));
            lm.put("fLastUpdateUserName", region2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(region2.getfLastUpdateTime()));
            lm.put("fDescription", region2.getfDescription());
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
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_REGION + ".xlsx");
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
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_REGION, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportRegionList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportRegionList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertRegion(Region region) {
        //校验number
        if (regionDAO.selectCountByNumber(region.getfNumber()) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1004);
        }
        //校验name
        if (regionDAO.selectCountByName(region.getfName()) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1005);
        }
        //生成ID
        region.setfID(UUIDUtil.genID());
        //设置状态初始值
        region.setfStatus(AraCoreConstants.STATUS_ENABLE);
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(region);
        //insert
        regionDAO.insertRegion(region);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateRegion(Region region) {
        //校验number
        if (StringUtil.isNotBlank(region.getfNumber())) {
            Region region1 = regionDAO.selectRegionByNumber(region.getfNumber());
            if (region1 != null) {
                if (!region.getfID().equals(region1.getfID())) {
                    return Result.error(AraResultCodeConstants.CODE_1004);
                }
            }
        }
        //校验name
        if (StringUtil.isNotBlank(region.getfName())) {
            Region region2 = regionDAO.selectRegionByName(region.getfName());
            if (region2 != null) {
                if (!region.getfID().equals(region2.getfID())) {
                    return Result.error(AraResultCodeConstants.CODE_1005);
                }
            }
        }
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(region);
        //update
        regionDAO.updateRegion(region);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateRegionStatusBatch(List<Region> regionList) {
//        for (Region region : regionList) {
//            //设置当前操作人及操作时间
//            CurrentUserUtil.setOperatorInfo(region);
//        }
        //update
        regionDAO.updateRegionStatusBatch(regionList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteRegionByID(String regionID) {
        //delete
        regionDAO.deleteRegionByID(regionID);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteRegionBatch(List<String> idList) {
        //delete
        regionDAO.deleteRegionBatch(idList);
        return Result.ok();
    }
}
