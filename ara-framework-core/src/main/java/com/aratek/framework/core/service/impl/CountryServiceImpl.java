package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.CountryDAO;
import com.aratek.framework.core.service.CountryService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.Country;
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
 * @description 国家服务 实现类
 */
@Service
public class CountryServiceImpl implements CountryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryServiceImpl.class);

    @Autowired
    private CountryDAO countryDAO;

    @Override
    public List<Country> selectCountryList(Country country) {
        PageHelper.startPage(country.getPageNum(), country.getPageSize(), PageUtil.getSortStr(country.getSortParams()));
        return countryDAO.selectCountryList(country);
    }

    @Override
    public Map exportCountryList(Country country) {
        //1.查询数据,是否查全部数据由前台传参控制
        PageHelper.startPage(country.getPageNum(), country.getPageSize(), PageUtil.getSortStr(country.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (country.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<Country> countryList = countryDAO.selectCountryList(country);
        if (countryList == null || countryList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_COUNTRY);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_COUNTRY);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < countryList.size(); i++) {
            Country country2 = countryList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fStatus", ExcelUtil.dealWithStatus(country2.getfStatus()));
            lm.put("fNumber", country2.getfNumber());
            lm.put("fName", country2.getfName());
            lm.put("fSimpleName", country2.getfSimpleName());
            lm.put("fCreatorName", country2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(country2.getfCreateTime()));
            lm.put("fLastUpdateUserName", country2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(country2.getfLastUpdateTime()));
            lm.put("fDescription", country2.getfDescription());
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
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_COUNTRY + ".xlsx");
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
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_COUNTRY, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportCountryList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportCountryList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertCountry(Country country) {
        //校验number
        if (countryDAO.selectCountByNumber(country.getfNumber()) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1004);
        }
        //校验name
        if (countryDAO.selectCountByName(country.getfName()) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1005);
        }
        //生成ID
        country.setfID(UUIDUtil.genID());
        //设置状态初始值
        country.setfStatus(AraCoreConstants.STATUS_ENABLE);
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(country);
        //insert
        countryDAO.insertCountry(country);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateCountry(Country country) {
        //校验number
        if (StringUtil.isNotBlank(country.getfNumber())) {
            Country country1 = countryDAO.selectCountryByNumber(country.getfNumber());
            if (country1 != null) {
                if (!country.getfID().equals(country1.getfID())) {
                    return Result.error(AraResultCodeConstants.CODE_1004);
                }
            }
        }
        //校验name
        if (StringUtil.isNotBlank(country.getfName())) {
            Country country2 = countryDAO.selectCountryByName(country.getfName());
            if (country2 != null) {
                if (!country.getfID().equals(country2.getfID())) {
                    return Result.error(AraResultCodeConstants.CODE_1005);
                }
            }
        }
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(country);
        //update
        countryDAO.updateCountry(country);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateCountryStatusBatch(List<Country> countryList) {
//        for (Country country : countryList) {
//            //设置当前操作人及操作时间
//            CurrentUserUtil.setOperatorInfo(country);
//        }
        //update
        countryDAO.updateCountryStatusBatch(countryList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteCountryByID(String countryID) {
        //校验引用
        if (countryDAO.selectCountReferenceByID(countryID) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1006);
        }
        //delete
        countryDAO.deleteCountryByID(countryID);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteCountryBatch(List<String> idList) {
        //校验引用
        if (countryDAO.selectCountReferenceByIDList(idList) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1006);
        }
        //delete
        countryDAO.deleteCountryBatch(idList);
        return Result.ok();
    }
}
