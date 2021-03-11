package com.aratek.framework.core.controller;

import com.aratek.framework.core.service.MixService;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-14
 * @description 混合接口
 */
@Api(tags = "混合接口", description = "混合接口")
@RestController
@RequestMapping("mix")
public class MixController extends BaseController {

    @Autowired
    private MixService mixService;

    //    @UserLogAnnotation(module = "mix", actType = "select")
    @ApiOperation(value = "查询最大number加1", notes = "查询最大number加1")
    @RequestMapping(value = "selectNumber/{tableName}/{columeName}", method = RequestMethod.GET)
    public Map selectNumber(@PathVariable String tableName, @PathVariable String columeName) {
        Integer number = mixService.selectNumber(tableName, columeName);
        //如果为null或者0，给默认值1000
        if (number == null || number == 0) {
            number = 1000;
        }
        return Result.ok().put("number", String.valueOf(number));
    }

    //    @UserLogAnnotation(module = "mix", actType = "select")
    @ApiOperation(value = "判断值是否存在", notes = "判断值是否存在:true,存在;false,不存在;")
    @RequestMapping(value = "isValueExist/{tableName}/{columeName}/{columeValue}", method = RequestMethod.GET)
    public Map isValueExist(@PathVariable String tableName, @PathVariable String columeName, @PathVariable String columeValue) {
        boolean result = mixService.isValueExist(tableName, columeName, columeValue);
        return Result.ok().put("exist", result);
    }

}
