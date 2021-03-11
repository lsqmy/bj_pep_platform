package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.CountryService;
import com.aratek.framework.core.util.CurrentUserUtil;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.Country;
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
 * @description 国家接口
 */
@Api(tags = "国家接口", description = "国家增删改查相关接口")
@RestController
@RequestMapping("country")
public class CountryController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    private CountryService countryService;

    @RequiresPermissions(value = {"1007:select"})
    @SysLogAnnotation("分页查询国家列表")
//    @UserLogAnnotation(module = "country", actType = "select")
    @ApiOperation(value = "分页查询国家列表", notes = "分页查询国家列表")
    @RequestMapping(value = "selectCountryList", method = RequestMethod.POST)
    public Map selectCountryList(@RequestBody Country country) {
        PageInfo pageInfo = new PageInfo(countryService.selectCountryList(country));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1007:export"})
    @SysLogAnnotation("导出国家列表")
    @UserLogAnnotation(module = "country", actType = "export")
    @ApiOperation(value = "导出国家列表", notes = "导出国家列表")
    @RequestMapping(value = "exportCountryList", method = RequestMethod.POST)
    public void exportCountryList(@RequestBody Country country) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = countryService.exportCountryList(country);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    @RequiresPermissions(value = {"1007:save"})
    @SysLogAnnotation("新增国家")
    @UserLogAnnotation(module = "country", actType = "insert")
    @ApiOperation(value = "新增国家", notes = "新增国家")
    @RequestMapping(value = "insertCountry", method = RequestMethod.POST)
    public Map insertCountry(@RequestBody Country country) {
        if (StringUtil.isBlank(country.getfName())) {
            LOGGER.warn("name为空!");
            return Result.error(AraResultCodeConstants.CODE_1008);
        }
        if (StringUtil.isBlank(country.getfNumber())) {
            LOGGER.warn("number为空!");
            return Result.error(AraResultCodeConstants.CODE_1007);
        }
        return countryService.insertCountry(country);
    }

    @RequiresPermissions(value = {"1007:update"})
    @SysLogAnnotation("修改国家信息")
    @UserLogAnnotation(module = "country", actType = "update")
    @ApiOperation(value = "修改国家信息", notes = "修改国家信息")
    @RequestMapping(value = "updateCountry", method = RequestMethod.POST)
    public Map updateCountry(@RequestBody Country country) {
        if (StringUtil.isBlank(country.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return countryService.updateCountry(country);
    }

    @RequiresPermissions(value = {"1007:enable"})
    @SysLogAnnotation("批量启用国家")
    @UserLogAnnotation(module = "country", actType = "update")
    @ApiOperation(value = "批量启用国家", notes = "批量启用国家")
    @RequestMapping(value = "updateCountryToEnableBatch", method = RequestMethod.POST)
    public Map updateCountryToEnableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<Country> countryList = new ArrayList<Country>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            Country country = new Country();
            country.setfID(id);
            country.setfStatus(AraCoreConstants.STATUS_ENABLE);
            country.setCurrentUserID(currentUserID);
            country.setCurrentUserOperationTime(now);
            countryList.add(country);
        }
        return countryService.updateCountryStatusBatch(countryList);
    }

    @RequiresPermissions(value = {"1007:disable"})
    @SysLogAnnotation("批量禁用国家")
    @UserLogAnnotation(module = "country", actType = "update")
    @ApiOperation(value = "批量禁用国家", notes = "批量禁用国家")
    @RequestMapping(value = "updateCountryToDisableBatch", method = RequestMethod.POST)
    public Map updateCountryToDisableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<Country> countryList = new ArrayList<Country>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            Country country = new Country();
            country.setfID(id);
            country.setfStatus(AraCoreConstants.STATUS_DISABLE);
            country.setCurrentUserID(currentUserID);
            country.setCurrentUserOperationTime(now);
            countryList.add(country);
        }
        return countryService.updateCountryStatusBatch(countryList);
    }

    /*@UserLogAnnotation(module = "country", actType = "delete")
    @ApiOperation(value = "根据ID删除国家", notes = "根据ID删除国家")
    @RequestMapping(value = "deleteCountryByID", method = RequestMethod.DELETE)
    public Map deleteCountryByID(@RequestBody Country country) {
        if (StringUtil.isBlank(country.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return countryService.deleteCountryByID(country.getfID());
    }*/

    @RequiresPermissions(value = {"1007:delete"})
    @SysLogAnnotation("批量根据ID删除国家")
    @UserLogAnnotation(module = "country", actType = "delete")
    @ApiOperation(value = "批量根据ID删除国家", notes = "批量根据ID删除国家")
    @RequestMapping(value = "deleteCountryBatch", method = RequestMethod.DELETE)
    public Map deleteCountryBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return countryService.deleteCountryBatch(idList);
    }
}
