package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.RegionService;
import com.aratek.framework.core.util.CurrentUserUtil;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.Region;
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
 * @description 区县接口
 */
@Api(tags = "区县接口", description = "区县增删改查相关接口")
@RestController
@RequestMapping("region")
public class RegionController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private RegionService regionService;

    @RequiresPermissions(value = {"1010:select"})
    @SysLogAnnotation("分页查询区县列表")
//    @UserLogAnnotation(module = "region", actType = "select")
    @ApiOperation(value = "分页查询区县列表", notes = "分页查询区县列表")
    @RequestMapping(value = "selectRegionList", method = RequestMethod.POST)
    public Map selectRegionList(@RequestBody Region region) {
        PageInfo pageInfo = new PageInfo(regionService.selectRegionList(region));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1010:export"})
    @SysLogAnnotation("导出区县列表")
    @UserLogAnnotation(module = "region", actType = "export")
    @ApiOperation(value = "导出区县列表", notes = "导出区县列表")
    @RequestMapping(value = "exportRegionList", method = RequestMethod.POST)
    public void exportRegionList(@RequestBody Region region) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = regionService.exportRegionList(region);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    @RequiresPermissions(value = {"1010:save"})
    @SysLogAnnotation("新增区县")
    @UserLogAnnotation(module = "region", actType = "insert")
    @ApiOperation(value = "新增区县", notes = "新增区县")
    @RequestMapping(value = "insertRegion", method = RequestMethod.POST)
    public Map insertRegion(@RequestBody Region region) {
        if (StringUtil.isBlank(region.getfName())) {
            LOGGER.warn("name为空!");
            return Result.error(AraResultCodeConstants.CODE_1008);
        }
        if (StringUtil.isBlank(region.getfNumber())) {
            LOGGER.warn("number为空!");
            return Result.error(AraResultCodeConstants.CODE_1007);
        }
        return regionService.insertRegion(region);
    }

    @RequiresPermissions(value = {"1010:update"})
    @SysLogAnnotation("修改区县信息")
    @UserLogAnnotation(module = "region", actType = "update")
    @ApiOperation(value = "修改区县信息", notes = "修改区县信息")
    @RequestMapping(value = "updateRegion", method = RequestMethod.POST)
    public Map updateRegion(@RequestBody Region region) {
        if (StringUtil.isBlank(region.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return regionService.updateRegion(region);
    }

    @RequiresPermissions(value = {"1010:enable"})
    @SysLogAnnotation("批量启用区县")
    @UserLogAnnotation(module = "region", actType = "update")
    @ApiOperation(value = "批量启用区县", notes = "批量启用区县")
    @RequestMapping(value = "updateRegionToEnableBatch", method = RequestMethod.POST)
    public Map updateRegionToEnableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<Region> regionList = new ArrayList<Region>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            Region region = new Region();
            region.setfID(id);
            region.setfStatus(AraCoreConstants.STATUS_ENABLE);
            region.setCurrentUserID(currentUserID);
            region.setCurrentUserOperationTime(now);
            regionList.add(region);
        }
        return regionService.updateRegionStatusBatch(regionList);
    }

    @RequiresPermissions(value = {"1010:disable"})
    @SysLogAnnotation("批量禁用区县")
    @UserLogAnnotation(module = "region", actType = "update")
    @ApiOperation(value = "批量禁用区县", notes = "批量禁用区县")
    @RequestMapping(value = "updateRegionToDisableBatch", method = RequestMethod.POST)
    public Map updateRegionToDisableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<Region> regionList = new ArrayList<Region>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            Region region = new Region();
            region.setfID(id);
            region.setfStatus(AraCoreConstants.STATUS_DISABLE);
            region.setCurrentUserID(currentUserID);
            region.setCurrentUserOperationTime(now);
            regionList.add(region);
        }
        return regionService.updateRegionStatusBatch(regionList);
    }

    /*@UserLogAnnotation(module = "region", actType = "delete")
    @ApiOperation(value = "根据ID删除区县", notes = "根据ID删除区县")
    @RequestMapping(value = "deleteRegionByID", method = RequestMethod.DELETE)
    public Map deleteRegionByID(@RequestBody Region region) {
        if (StringUtil.isBlank(region.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return regionService.deleteRegionByID(region.getfID());
    }*/

    @RequiresPermissions(value = {"1010:delete"})
    @SysLogAnnotation("批量根据ID删除区县")
    @UserLogAnnotation(module = "region", actType = "delete")
    @ApiOperation(value = "批量根据ID删除区县", notes = "批量根据ID删除区县")
    @RequestMapping(value = "deleteRegionBatch", method = RequestMethod.DELETE)
    public Map deleteRegionBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return regionService.deleteRegionBatch(idList);
    }
}
