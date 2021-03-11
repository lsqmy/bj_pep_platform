package com.aratek.framework.core.util;

import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.domain.constant.AraDomainConstants;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author shijinlong
 * @date 2018-06-26
 * @description excel帮助类
 */
public class ExcelUtil {

    /**
     * 设置response expose headers
     */
    public static void setResponseExposeHeaders() {
        HttpServletResponse response = HttpContextUtil.getHttpServletResponse();
        response.setHeader("Access-Control-Expose-Headers", AraDomainConstants.DEFAULT_RESULT_CODE_NAME + "," + AraDomainConstants.DEFAULT_RESULT_MESSAGE_NAME);
    }

    /**
     * excel导出:下载的响应头设置
     *
     * @param modelName
     * @param response
     */
    public static void setExcelExportResponse(String modelName, HttpServletResponse response) {
        //指定下载的文件名--设置响应头
        String filename = AraCoreConstants.EXCEL_PREFIX + modelName + "_" + DateUtil.getNowByPattern("yyyyMMddHHmmss") + AraCoreConstants.EXCEL_SUFFIX;
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
//        response.setHeader("Access-Control-Expose-Headers", AraDomainConstants.DEFAULT_RESULT_CODE_NAME);
        response.setDateHeader("Expires", 0);
        response.setHeader(AraDomainConstants.DEFAULT_RESULT_CODE_NAME, AraDomainConstants.DEFAULT_RESULT_CODE_VALUE_OK);
    }

    /**
     * excel导出:处理状态字段
     *
     * @param status
     * @return
     */
    public static String dealWithStatus(Integer status) {
        if (status == null) {
            return null;
        }
        if (AraCoreConstants.STATUS_SAVE == status) {
            return "保存";
        } else if (AraCoreConstants.STATUS_ENABLE == status) {
            return "启用";
        } else if (AraCoreConstants.STATUS_DISABLE == status) {
            return "禁用";
        } else {
            return "异常";
        }
    }

    /**
     * excel导出:处理字段,是否
     *
     * @param flag
     * @return
     */
    public static String dealWithYesNo(Integer flag) {
        if (flag == null) {
            return null;
        }
        if (AraCoreConstants.YES == flag) {
            return "是";
        } else if (AraCoreConstants.NO == flag) {
            return "否";
        } else {
            return "异常";
        }
    }

    /**
     * excel导出:处理白名单类型
     *
     * @param whiteType
     * @return
     */
    public static String dealWithWhiteType(String whiteType) {
        if (StringUtil.isBlank(whiteType)) {
            return null;
        }
        if ("1".equals(whiteType)) {
            return "IPv4";
        } else if ("2".equals(whiteType)) {
            return "SN";
        } else {
            return "异常";
        }
    }

    /**
     * excel导出:处理校验类型
     *
     * @param checkType
     * @return
     */
    public static String dealWithCheckType(String checkType) {
        if (StringUtil.isBlank(checkType)) {
            return null;
        }
        if ("1".equals(checkType)) {
            return "精准匹配";
        } else if ("2".equals(checkType)) {
            return "通配符匹配";
        } else if ("3".equals(checkType)) {
            return "区间范围";
        } else {
            return "异常";
        }
    }

    /**
     * 导出结果的响应头设置
     *
     * @param code
     */
    public static void setExportResultResponse(String code) {
        //设置响应头
        HttpServletResponse response = HttpContextUtil.getHttpServletResponse();
        response.setHeader(AraDomainConstants.DEFAULT_RESULT_CODE_NAME, code);
        if (!AraDomainConstants.DEFAULT_RESULT_CODE_VALUE_OK.equals(code)) {
            Locale locale = RequestContextUtils.getLocale(HttpContextUtil.getHttpServletRequest());
            if ("CN".equals(locale.getCountry())) {
                response.setHeader(AraDomainConstants.DEFAULT_RESULT_MESSAGE_NAME, "导出失败!");
            } else {
                response.setHeader(AraDomainConstants.DEFAULT_RESULT_MESSAGE_NAME, "Failed to export!");
            }
        }
    }

}
