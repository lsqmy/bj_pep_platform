package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.CityService;
import com.aratek.framework.core.util.CurrentUserUtil;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.City;
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
 * @description 城市接口
 */
@Api(tags = "城市接口", description = "城市增删改查相关接口")
@RestController
@RequestMapping("city")
public class CityController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityController.class);

    @Autowired
    private CityService cityService;

    @RequiresPermissions(value = {"1009:select"})
    @SysLogAnnotation("分页查询城市列表")
//    @UserLogAnnotation(module = "city", actType = "select")
    @ApiOperation(value = "分页查询城市列表", notes = "分页查询城市列表")
    @RequestMapping(value = "selectCityList", method = RequestMethod.POST)
    public Map selectCityList(@RequestBody City city) {
        PageInfo pageInfo = new PageInfo(cityService.selectCityList(city));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1009:export"})
    @SysLogAnnotation("导出城市列表")
    @UserLogAnnotation(module = "city", actType = "export")
    @ApiOperation(value = "导出城市列表", notes = "导出城市列表")
    @RequestMapping(value = "exportCityList", method = RequestMethod.POST)
    public void exportCityList(@RequestBody City city) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = cityService.exportCityList(city);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    @RequiresPermissions(value = {"1009:save"})
    @SysLogAnnotation("新增城市")
    @UserLogAnnotation(module = "city", actType = "insert")
    @ApiOperation(value = "新增城市", notes = "新增城市")
    @RequestMapping(value = "insertCity", method = RequestMethod.POST)
    public Map insertCity(@RequestBody City city) {
        if (StringUtil.isBlank(city.getfName())) {
            LOGGER.warn("name为空!");
            return Result.error(AraResultCodeConstants.CODE_1008);
        }
        if (StringUtil.isBlank(city.getfNumber())) {
            LOGGER.warn("number为空!");
            return Result.error(AraResultCodeConstants.CODE_1007);
        }
        return cityService.insertCity(city);
    }

    @RequiresPermissions(value = {"1009:update"})
    @SysLogAnnotation("修改城市信息")
    @UserLogAnnotation(module = "city", actType = "update")
    @ApiOperation(value = "修改城市信息", notes = "修改城市信息")
    @RequestMapping(value = "updateCity", method = RequestMethod.POST)
    public Map updateCity(@RequestBody City city) {
        if (StringUtil.isBlank(city.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return cityService.updateCity(city);
    }

    @RequiresPermissions(value = {"1009:enable"})
    @SysLogAnnotation("批量启用城市")
    @UserLogAnnotation(module = "city", actType = "update")
    @ApiOperation(value = "批量启用城市", notes = "批量启用城市")
    @RequestMapping(value = "updateCityToEnableBatch", method = RequestMethod.POST)
    public Map updateCityToEnableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<City> cityList = new ArrayList<City>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            City city = new City();
            city.setfID(id);
            city.setfStatus(AraCoreConstants.STATUS_ENABLE);
            city.setCurrentUserID(currentUserID);
            city.setCurrentUserOperationTime(now);
            cityList.add(city);
        }
        return cityService.updateCityStatusBatch(cityList);
    }

    @RequiresPermissions(value = {"1009:disable"})
    @SysLogAnnotation("批量禁用城市")
    @UserLogAnnotation(module = "city", actType = "update")
    @ApiOperation(value = "批量禁用城市", notes = "批量禁用城市")
    @RequestMapping(value = "updateCityToDisableBatch", method = RequestMethod.POST)
    public Map updateCityToDisableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<City> cityList = new ArrayList<City>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            City city = new City();
            city.setfID(id);
            city.setfStatus(AraCoreConstants.STATUS_DISABLE);
            city.setCurrentUserID(currentUserID);
            city.setCurrentUserOperationTime(now);
            cityList.add(city);
        }
        return cityService.updateCityStatusBatch(cityList);
    }

    /*@UserLogAnnotation(module = "city", actType = "delete")
    @ApiOperation(value = "根据ID删除城市", notes = "根据ID删除城市")
    @RequestMapping(value = "deleteCityByID", method = RequestMethod.DELETE)
    public Map deleteCityByID(@RequestBody City city) {
        if (StringUtil.isBlank(city.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return cityService.deleteCityByID(city.getfID());
    }*/

    @RequiresPermissions(value = {"1009:delete"})
    @SysLogAnnotation("批量根据ID删除城市")
    @UserLogAnnotation(module = "city", actType = "delete")
    @ApiOperation(value = "批量根据ID删除城市", notes = "批量根据ID删除城市")
    @RequestMapping(value = "deleteCityBatch", method = RequestMethod.DELETE)
    public Map deleteCityBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return cityService.deleteCityBatch(idList);
    }
}
