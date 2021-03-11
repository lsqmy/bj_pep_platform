package com.aratek.framework.gen.controller;

import com.aratek.framework.core.annotation.IgnoreAuth;
import com.aratek.framework.core.exception.AraBaseException;
import com.aratek.framework.core.util.HttpContextUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.gen.service.GeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @Author 姜寄羽
 * 代码生成器
 * @Date 2018/5/30 14:02
 */

/**
 * @author shijinlong
 * @date 2018-05-16
 * @description FTP接口
 */
@Api(tags = "代码生成器", description = "下载代码")
@RestController
@RequestMapping("gen")
public class GeneratorController {

    @Autowired
    GeneratorService generatorService;

    @IgnoreAuth
    @ApiOperation(value = "下载代码包", notes = "下载代码包")
    @GetMapping(value = "code/{table}")
    public Map code(@PathVariable("table") String table) throws IOException, AraBaseException {
        String[] tableNames = table.split(",");
        //true 代表直接覆盖原有文件
        byte[] data = generatorService.generatorCode(tableNames);
        HttpContextUtil.getHttpServletResponse().reset();
        HttpContextUtil.getHttpServletResponse().setHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
        HttpContextUtil.getHttpServletResponse().addHeader("Content-Length", "" + data.length);
        HttpContextUtil.getHttpServletResponse().setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, HttpContextUtil.getHttpServletResponse().getOutputStream());
        return Result.ok();
    }
}
