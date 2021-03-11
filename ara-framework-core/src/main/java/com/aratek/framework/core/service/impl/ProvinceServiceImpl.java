package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.ProvinceDAO;
import com.aratek.framework.core.service.ProvinceService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.Province;
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
 * @description 省份服务 实现类
 */
@Service
public class ProvinceServiceImpl implements ProvinceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProvinceServiceImpl.class);

    @Autowired
    private ProvinceDAO provinceDAO;

    @Override
    public List<Province> selectProvinceList(Province province) {
        PageHelper.startPage(province.getPageNum(), province.getPageSize(), PageUtil.getSortStr(province.getSortParams()));
        return provinceDAO.selectProvinceList(province);
    }

    @Override
    public Map exportProvinceList(Province province) {
        //1.查询数据,是否查全部数据由前台传参控制
        PageHelper.startPage(province.getPageNum(), province.getPageSize(), PageUtil.getSortStr(province.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (province.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<Province> provinceList = provinceDAO.selectProvinceList(province);
        if (provinceList == null || provinceList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_PROVINCE);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_PROVINCE);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < provinceList.size(); i++) {
            Province province2 = provinceList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fStatus", ExcelUtil.dealWithStatus(province2.getfStatus()));
            lm.put("fNumber", province2.getfNumber());
            lm.put("fName", province2.getfName());
            lm.put("fCountryName", province2.getfCountryName());
            lm.put("fSimpleName", province2.getfSimpleName());
            lm.put("fCreatorName", province2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(province2.getfCreateTime()));
            lm.put("fLastUpdateUserName", province2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(province2.getfLastUpdateTime()));
            lm.put("fDescription", province2.getfDescription());
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
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_PROVINCE + ".xlsx");
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
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_PROVINCE, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportProvinceList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportProvinceList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertProvince(Province province) {
        //校验number
        if (provinceDAO.selectCountByNumber(province.getfNumber()) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1004);
        }
        //校验name
        if (provinceDAO.selectCountByName(province.getfName()) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1005);
        }
        //生成ID
        province.setfID(UUIDUtil.genID());
        //设置状态初始值
        province.setfStatus(AraCoreConstants.STATUS_ENABLE);
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(province);
        //insert
        provinceDAO.insertProvince(province);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateProvince(Province province) {
        //校验number
        if (StringUtil.isNotBlank(province.getfNumber())) {
            Province province1 = provinceDAO.selectProvinceByNumber(province.getfNumber());
            if (province1 != null) {
                if (!province.getfID().equals(province1.getfID())) {
                    return Result.error(AraResultCodeConstants.CODE_1004);
                }
            }
        }
        //校验name
        if (StringUtil.isNotBlank(province.getfName())) {
            Province province2 = provinceDAO.selectProvinceByName(province.getfName());
            if (province2 != null) {
                if (!province.getfID().equals(province2.getfID())) {
                    return Result.error(AraResultCodeConstants.CODE_1005);
                }
            }
        }
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(province);
        //update
        provinceDAO.updateProvince(province);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateProvinceStatusBatch(List<Province> provinceList) {
//        for (Province province : provinceList) {
//            //设置当前操作人及操作时间
//            CurrentUserUtil.setOperatorInfo(province);
//        }
        //update
        provinceDAO.updateProvinceStatusBatch(provinceList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteProvinceByID(String provinceID) {
        //校验引用
        if (provinceDAO.selectCountReferenceByID(provinceID) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1006);
        }
        //delete
        provinceDAO.deleteProvinceByID(provinceID);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteProvinceBatch(List<String> idList) {
        //校验引用
        if (provinceDAO.selectCountReferenceByIDList(idList) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1006);
        }
        //delete
        provinceDAO.deleteProvinceBatch(idList);
        return Result.ok();
    }
}
