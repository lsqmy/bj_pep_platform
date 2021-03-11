package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.CityDAO;
import com.aratek.framework.core.service.CityService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.City;
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
 * @description 城市服务 实现类
 */
@Service
public class CityServiceImpl implements CityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityServiceImpl.class);

    @Autowired
    private CityDAO cityDAO;

    @Override
    public List<City> selectCityList(City city) {
        PageHelper.startPage(city.getPageNum(), city.getPageSize(), PageUtil.getSortStr(city.getSortParams()));
        return cityDAO.selectCityList(city);
    }

    @Override
    public Map exportCityList(City city) {
        //1.查询数据,是否查全部数据由前台传参控制
        PageHelper.startPage(city.getPageNum(), city.getPageSize(), PageUtil.getSortStr(city.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (city.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<City> cityList = cityDAO.selectCityList(city);
        if (cityList == null || cityList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_CITY);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_CITY);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < cityList.size(); i++) {
            City city2 = cityList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fStatus", ExcelUtil.dealWithStatus(city2.getfStatus()));
            lm.put("fNumber", city2.getfNumber());
            lm.put("fName", city2.getfName());
            lm.put("fProvinceName", city2.getfProvinceName());
            lm.put("fSimpleName", city2.getfSimpleName());
            lm.put("fCreatorName", city2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(city2.getfCreateTime()));
            lm.put("fLastUpdateUserName", city2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(city2.getfLastUpdateTime()));
            lm.put("fDescription", city2.getfDescription());
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
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_CITY + ".xlsx");
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
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_CITY, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportCityList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportCityList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertCity(City city) {
        //校验number
        if (cityDAO.selectCountByNumber(city.getfNumber()) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1004);
        }
        //校验name
        if (cityDAO.selectCountByName(city.getfName()) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1005);
        }
        //生成ID
        city.setfID(UUIDUtil.genID());
        //设置状态初始值
        city.setfStatus(AraCoreConstants.STATUS_ENABLE);
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(city);
        //insert
        cityDAO.insertCity(city);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateCity(City city) {
        //校验number
        if (StringUtil.isNotBlank(city.getfNumber())) {
            City city1 = cityDAO.selectCityByNumber(city.getfNumber());
            if (city1 != null) {
                if (!city.getfID().equals(city1.getfID())) {
                    return Result.error(AraResultCodeConstants.CODE_1004);
                }
            }
        }
        //校验name
        if (StringUtil.isNotBlank(city.getfName())) {
            City city2 = cityDAO.selectCityByName(city.getfName());
            if (city2 != null) {
                if (!city.getfID().equals(city2.getfID())) {
                    return Result.error(AraResultCodeConstants.CODE_1005);
                }
            }
        }
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(city);
        //update
        cityDAO.updateCity(city);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateCityStatusBatch(List<City> cityList) {
//        for (City city : cityList) {
//            //设置当前操作人及操作时间
//            CurrentUserUtil.setOperatorInfo(city);
//        }
        //update
        cityDAO.updateCityStatusBatch(cityList);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteCityByID(String cityID) {
        //校验引用
        if (cityDAO.selectCountReferenceByID(cityID) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1006);
        }
        //delete
        cityDAO.deleteCityByID(cityID);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteCityBatch(List<String> idList) {
        //校验引用
        if (cityDAO.selectCountReferenceByIDList(idList) > 0) {
            return Result.error(AraResultCodeConstants.CODE_1006);
        }
        //delete
        cityDAO.deleteCityBatch(idList);
        return Result.ok();
    }
}
