package com.aratek.framework.core.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.dao.ParamRightDAO;
import com.aratek.framework.core.service.ParamRightService;
import com.aratek.framework.core.util.*;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.ParamRight;
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
 * @description 权限参数 实现类
 */
@Service
public class ParamRightServiceImpl implements ParamRightService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParamRightServiceImpl.class);

    @Autowired
    private ParamRightDAO paramRightDAO;


    @Override
    public List<ParamRight> selectParamRightList(ParamRight paramRight) {
        PageHelper.startPage(paramRight.getPageNum(), paramRight.getPageSize(), PageUtil.getSortStr(paramRight.getSortParams()));
        return paramRightDAO.selectParamRightList(paramRight);
    }

    @Override
    public Map exportParamRightList(ParamRight paramRight) {
        //1.查询数据,是否查全部数据由前台传参控制
        PageHelper.startPage(paramRight.getPageNum(), paramRight.getPageSize(), PageUtil.getSortStr(paramRight.getSortParams()));
        //如果pageSize为0，需设置orderByOnly为true
        if (paramRight.getPageSize() == 0) {
            PageHelper.getLocalPage().setOrderByOnly(true);
        }
        List<ParamRight> paramRightList = paramRightDAO.selectParamRightList(paramRight);
        if (paramRightList == null || paramRightList.size() == 0) {
            LOGGER.warn("没有数据!");
            return Result.error(AraResultCodeConstants.CODE_1601);
        }
        //2.设置导出配置
        // 获取导出excel指定模版
        TemplateExportParams params = new TemplateExportParams(AraCoreConstants.EXCEL_TEMPLATE_PARAM_RIGHT);
        // 标题开始行
        params.setHeadingStartRow(0);
        // 标题行数
        params.setHeadingRows(1);
        // 设置sheetName，若不设置该参数，则使用原本sheet名称
        params.setSheetName(AraCoreConstants.MODULE_PARAM_RIGHT);
        //3.模版--单sheet--导出
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < paramRightList.size(); i++) {
            ParamRight paramRight2 = paramRightList.get(i);
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("num", i + 1 + "");
            lm.put("fRightCode", paramRight2.getfRightCode());
            lm.put("fRightName", paramRight2.getfRightName());
            lm.put("fDisplayorder", String.valueOf(paramRight2.getfDisplayorder()));
            lm.put("fCreatorName", paramRight2.getfCreatorName());
            lm.put("fCreateTime", DateUtil.formatDateTime(paramRight2.getfCreateTime()));
            lm.put("fLastUpdateUserName", paramRight2.getfLastUpdateUserName());
            lm.put("fLastUpdateTime", DateUtil.formatDateTime(paramRight2.getfLastUpdateTime()));
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
            fos = new FileOutputStream("D:/temp/excel/" + AraCoreConstants.MODULE_PARAM_RIGHT + ".xlsx");
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
        ExcelUtil.setExcelExportResponse(AraCoreConstants.MODULE_PARAM_RIGHT, response);
        //5.写出数据输出流到页面
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            workbook.write(bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("exportParamRightList.error:{}", e);
            return Result.error(AraResultCodeConstants.CODE_1602);
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("exportParamRightList.error:{}", e);
                    return Result.error(AraResultCodeConstants.CODE_1602);
                }
            }
        }
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertParamRight(ParamRight paramRight) {
        //生成ID
        paramRight.setfID(UUIDUtil.genID());
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(paramRight);
        //insert
        paramRightDAO.insertParamRight(paramRight);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateParamRight(ParamRight paramRight) {
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(paramRight);
        //update
        paramRightDAO.updateParamRight(paramRight);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteParamRightBatch(List<String> idList) {
        paramRightDAO.deleteParamRightBatch(idList);
        return Result.ok();
    }
}
