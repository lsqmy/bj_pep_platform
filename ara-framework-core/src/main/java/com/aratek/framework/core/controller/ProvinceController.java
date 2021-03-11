package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.ProvinceService;
import com.aratek.framework.core.util.CurrentUserUtil;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.Province;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-15
 * @description 省份接口
 */
@Api(tags = "省份接口", description = "省份增删改查相关接口")
@RestController
@RequestMapping("province")
public class ProvinceController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private ProvinceService provinceService;

    @RequiresPermissions(value = {"1008:select"})
    @SysLogAnnotation("分页查询省份列表")
//    @UserLogAnnotation(module = "province", actType = "select")
    @ApiOperation(value = "分页查询省份列表", notes = "分页查询省份列表")
    @RequestMapping(value = "selectProvinceList", method = RequestMethod.POST)
    public Map selectProvinceList(@RequestBody Province province) {
        PageInfo pageInfo = new PageInfo(provinceService.selectProvinceList(province));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1008:export"})
    @SysLogAnnotation("导出省份列表")
    @UserLogAnnotation(module = "province", actType = "export")
    @ApiOperation(value = "导出省份列表", notes = "导出省份列表")
    @RequestMapping(value = "exportProvinceList", method = RequestMethod.POST)
    public void exportProvinceList(@RequestBody Province province) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = provinceService.exportProvinceList(province);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    @RequiresPermissions(value = {"1008:save"})
    @SysLogAnnotation("新增省份")
    @UserLogAnnotation(module = "province", actType = "insert")
    @ApiOperation(value = "新增省份", notes = "新增省份")
    @RequestMapping(value = "insertProvince", method = RequestMethod.POST)
    public Map insertProvince(@RequestBody Province province) {
        if (StringUtil.isBlank(province.getfName())) {
            LOGGER.warn("name为空!");
            return Result.error(AraResultCodeConstants.CODE_1008);
        }
        if (StringUtil.isBlank(province.getfNumber())) {
            LOGGER.warn("number为空!");
            return Result.error(AraResultCodeConstants.CODE_1007);
        }
        return provinceService.insertProvince(province);
    }

    @RequiresPermissions(value = {"1008:update"})
    @SysLogAnnotation("修改省份信息")
    @UserLogAnnotation(module = "province", actType = "update")
    @ApiOperation(value = "修改省份信息", notes = "修改省份信息")
    @RequestMapping(value = "updateProvince", method = RequestMethod.POST)
    public Map updateProvince(@RequestBody Province province) {
        if (StringUtil.isBlank(province.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return provinceService.updateProvince(province);
    }

    @RequiresPermissions(value = {"1008:enable"})
    @SysLogAnnotation("批量启用省份")
    @UserLogAnnotation(module = "province", actType = "update")
    @ApiOperation(value = "批量启用省份", notes = "批量启用省份")
    @RequestMapping(value = "updateProvinceToEnableBatch", method = RequestMethod.POST)
    public Map updateProvinceToEnableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<Province> provinceList = new ArrayList<Province>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            Province province = new Province();
            province.setfID(id);
            province.setfStatus(AraCoreConstants.STATUS_ENABLE);
            province.setCurrentUserID(currentUserID);
            province.setCurrentUserOperationTime(now);
            provinceList.add(province);
        }
        return provinceService.updateProvinceStatusBatch(provinceList);
    }

    @RequiresPermissions(value = {"1008:disable"})
    @SysLogAnnotation("批量禁用省份")
    @UserLogAnnotation(module = "province", actType = "update")
    @ApiOperation(value = "批量禁用省份", notes = "批量禁用省份")
    @RequestMapping(value = "updateProvinceToDisableBatch", method = RequestMethod.POST)
    public Map updateProvinceToDisableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<Province> provinceList = new ArrayList<Province>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            Province province = new Province();
            province.setfID(id);
            province.setfStatus(AraCoreConstants.STATUS_DISABLE);
            province.setCurrentUserID(currentUserID);
            province.setCurrentUserOperationTime(now);
            provinceList.add(province);
        }
        return provinceService.updateProvinceStatusBatch(provinceList);
    }

    /*@UserLogAnnotation(module = "province", actType = "delete")
    @ApiOperation(value = "根据ID删除省份", notes = "根据ID删除省份")
    @RequestMapping(value = "deleteProvinceByID", method = RequestMethod.DELETE)
    public Map deleteProvinceByID(@RequestBody Province province) {
        if (StringUtil.isBlank(province.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return provinceService.deleteProvinceByID(province.getfID());
    }*/

    @RequiresPermissions(value = {"1008:delete"})
    @SysLogAnnotation("批量根据ID删除省份")
    @UserLogAnnotation(module = "province", actType = "delete")
    @ApiOperation(value = "批量根据ID删除省份", notes = "批量根据ID删除省份")
    @RequestMapping(value = "deleteProvinceBatch", method = RequestMethod.DELETE)
    public Map deleteProvinceBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return provinceService.deleteProvinceBatch(idList);
    }
}
