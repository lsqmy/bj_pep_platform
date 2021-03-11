package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.SysLogAnnotation;
import com.aratek.framework.core.annotation.UserLogAnnotation;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.service.FtpService;
import com.aratek.framework.core.util.CurrentUserUtil;
import com.aratek.framework.core.util.ExcelUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.constant.AraDomainConstants;
import com.aratek.framework.domain.core.FtpInfo;
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
 * @date 2018-05-16
 * @description FTP接口
 */
@Api(tags = "FTP接口", description = "FTP相关接口")
@RestController
@RequestMapping("ftp")
public class FtpController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpController.class);

    @Autowired
    private FtpService ftpService;

    /*@IgnoreSysLog
    @UserLogAnnotation(module = "ftp", actType = "fileUpload")
    @ApiOperation(value = "上传文件到ftp", notes = "上传文件到ftp")
    @RequestMapping(value = "uploadFile2Ftp", method = RequestMethod.POST)
    public Map uploadFile2Ftp(@RequestParam("file") MultipartFile file) {
        try {
            LOGGER.warn("file.getOriginalFilename={}", file.getOriginalFilename());
            FtpUtil.uploadFile2Ftp(file.getInputStream(), "/" + file.getOriginalFilename());
        } catch (IOException e) {
            LOGGER.error("uploadFile2Ftp.error:", e);
            return Result.error(AraResultCodeConstants.CODE_1301);
        }
        return Result.ok();
    }*/

    /*@IgnoreSysLog
    @UserLogAnnotation(module = "ftp", actType = "fileUpload")
    @ApiOperation(value = "批量上传文件到ftp", notes = "批量上传文件到ftp")
    @RequestMapping(value = "uploadFile2FtpBatch", method = RequestMethod.POST)
    public Map uploadFile2FtpBatch(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        boolean uploadResult = true;
        List<String> failNames = new ArrayList<String>();
        for (MultipartFile file : files) {
            try {
                LOGGER.warn("uploadFile2FtpBatch.file.getOriginalFilename={}", file.getOriginalFilename());
                FtpUtil.uploadFile2Ftp(file.getInputStream(), "/" + file.getOriginalFilename());
            } catch (IOException e) {
                LOGGER.error("uploadFile2FtpBatch.error:", e);
                uploadResult = false;
                failNames.add(file.getOriginalFilename());
            }
        }
        if (!uploadResult) {
            return Result.error(AraResultCodeConstants.CODE_1301).put("failNames", failNames);
        }
        return Result.ok();
    }*/

    /*@IgnoreSysLog
    @UserLogAnnotation(module = "ftp", actType = "fileDownload")
    @ApiOperation(value = "从ftp下载文件", notes = "从ftp下载文件")
    @RequestMapping(value = "downloadFileFromFtp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadFileFromFtp(@RequestParam("remoteFilePath") String remoteFilePath) {
        try {
            LOGGER.warn("remoteFilePath={}", remoteFilePath);
            FtpUtil.downloadFile(remoteFilePath, HttpContextUtil.getHttpServletResponse());
        } catch (IOException e) {
            LOGGER.error("downloadFileFromFtp.error:", e);
        }
//        return Result.ok();
    }*/

    @RequiresPermissions(value = {"1005:select"})
    @SysLogAnnotation("分页查询ftp列表")
//    @UserLogAnnotation(module = "ftp", actType = "select")
    @ApiOperation(value = "分页查询ftp列表", notes = "分页查询ftp列表")
    @RequestMapping(value = "selectFtpInfoList", method = RequestMethod.POST)
    public Map selectFtpInfoList(@RequestBody FtpInfo ftpInfo) {
        PageInfo pageInfo = new PageInfo(ftpService.selectFtpInfoList(ftpInfo));
        return Result.ok().putData(pageInfo);
    }

    @RequiresPermissions(value = {"1005:export"})
    @SysLogAnnotation("导出ftp列表")
    @UserLogAnnotation(module = "ftp", actType = "export")
    @ApiOperation(value = "导出ftp列表", notes = "导出ftp列表")
    @RequestMapping(value = "exportFtpInfoList", method = RequestMethod.POST)
    public void exportFtpInfoList(@RequestBody FtpInfo ftpInfo) {
        try {
            //设置允许接收的响应头
            ExcelUtil.setResponseExposeHeaders();
            Map resultMap = ftpService.exportFtpInfoList(ftpInfo);
            ExcelUtil.setExportResultResponse((String) resultMap.get(AraDomainConstants.DEFAULT_RESULT_CODE_NAME));
        } catch (Exception e) {
            LOGGER.error("导出异常:{}", e);
            ExcelUtil.setExportResultResponse(AraResultCodeConstants.CODE_1602);
        }
    }

    /*@UserLogAnnotation(module = "ftp", actType = "select")
    @ApiOperation(value = "根据ID查询ftp", notes = "根据ID查询ftp")
    @RequestMapping(value = "selectFtpInfoByID", method = RequestMethod.POST)
    public Map selectFtpInfoByID(@RequestBody FtpInfo ftpInfo) {
        FtpInfo info = ftpService.selectFtpInfoByID(ftpInfo.getfID());
        if (info == null) {
            return Result.error(AraResultCodeConstants.CODE_1501);
        }
        return Result.ok().putData(info);
    }*/

    @RequiresPermissions(value = {"1005:save"})
    @SysLogAnnotation("新增ftp")
    @UserLogAnnotation(module = "ftp", actType = "insert")
    @ApiOperation(value = "新增ftp", notes = "新增ftp")
    @RequestMapping(value = "insertFtpInfo", method = RequestMethod.POST)
    public Map insertFtpInfo(@RequestBody FtpInfo ftpInfo) {
        if (StringUtil.isBlank(ftpInfo.getfHost())) {
            LOGGER.warn("host is null!");
            return Result.error(AraResultCodeConstants.CODE_1502);
        }
        if (null == ftpInfo.getfPort()) {
            LOGGER.warn("port is null!");
            return Result.error(AraResultCodeConstants.CODE_1503);
        }
        if (StringUtil.isBlank(ftpInfo.getfUserName())) {
            LOGGER.warn("username is null!");
            return Result.error(AraResultCodeConstants.CODE_1002);
        }
        if (StringUtil.isBlank(ftpInfo.getfPassWord())) {
            LOGGER.warn("password is null!");
            return Result.error(AraResultCodeConstants.CODE_1009);
        }
        return ftpService.insertFtpInfo(ftpInfo);
    }

    @RequiresPermissions(value = {"1005:update"})
    @SysLogAnnotation("修改ftp信息")
    @UserLogAnnotation(module = "ftp", actType = "update")
    @ApiOperation(value = "修改ftp信息", notes = "修改ftp信息")
    @RequestMapping(value = "updateFtpInfo", method = RequestMethod.POST)
    public Map updateFtpInfo(@RequestBody FtpInfo ftpInfo) {
        if (StringUtil.isBlank(ftpInfo.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return ftpService.updateFtpInfo(ftpInfo);
    }

    @RequiresPermissions(value = {"1005:enable"})
    @SysLogAnnotation("批量启用ftp")
    @UserLogAnnotation(module = "ftp", actType = "update")
    @ApiOperation(value = "批量启用ftp", notes = "批量启用ftp")
    @RequestMapping(value = "updateFtpToEnableBatch", method = RequestMethod.POST)
    public Map updateFtpToEnableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<FtpInfo> ftpInfoList = new ArrayList<FtpInfo>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            FtpInfo ftpInfo = new FtpInfo();
            ftpInfo.setfID(id);
            ftpInfo.setfStatus(AraCoreConstants.STATUS_ENABLE);
            ftpInfo.setCurrentUserID(currentUserID);
            ftpInfo.setCurrentUserOperationTime(now);
            ftpInfoList.add(ftpInfo);
        }
        return ftpService.updateFtpInfoStatusBatch(ftpInfoList);
    }

    @RequiresPermissions(value = {"1005:disable"})
    @SysLogAnnotation("批量禁用ftp")
    @UserLogAnnotation(module = "ftp", actType = "update")
    @ApiOperation(value = "批量禁用ftp", notes = "批量禁用ftp")
    @RequestMapping(value = "updateFtpToDisableBatch", method = RequestMethod.POST)
    public Map updateFtpToDisableBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        List<FtpInfo> ftpInfoList = new ArrayList<FtpInfo>();
        String currentUserID = CurrentUserUtil.getCurrentUserID();
        Date now = new Date();
        for (String id : idList) {
            FtpInfo ftpInfo = new FtpInfo();
            ftpInfo.setfID(id);
            ftpInfo.setfStatus(AraCoreConstants.STATUS_DISABLE);
            ftpInfo.setCurrentUserID(currentUserID);
            ftpInfo.setCurrentUserOperationTime(now);
            ftpInfoList.add(ftpInfo);
        }
        return ftpService.updateFtpInfoStatusBatch(ftpInfoList);
    }

    /*@UserLogAnnotation(module = "ftp", actType = "delete")
    @ApiOperation(value = "根据ID删除ftp", notes = "根据ID删除ftp")
    @RequestMapping(value = "deleteFtpInfoByID", method = RequestMethod.DELETE)
    public Map deleteFtpInfoByID(@RequestBody FtpInfo ftpInfo) {
        if (StringUtil.isBlank(ftpInfo.getfID())) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return ftpService.deleteFtpInfoByID(ftpInfo.getfID());
    }*/

    @RequiresPermissions(value = {"1005:delete"})
    @SysLogAnnotation("批量根据ID删除ftp")
    @UserLogAnnotation(module = "ftp", actType = "delete")
    @ApiOperation(value = "批量根据ID删除ftp", notes = "批量根据ID删除ftp")
    @RequestMapping(value = "deleteFtpInfoBatch", method = RequestMethod.DELETE)
    public Map deleteFtpInfoBatch(@RequestBody List<String> idList) {
        if (idList == null || idList.size() == 0) {
            LOGGER.warn("ID为空!");
            return Result.error(AraResultCodeConstants.CODE_1001);
        }
        return ftpService.deleteFtpInfoBatch(idList);
    }

}
