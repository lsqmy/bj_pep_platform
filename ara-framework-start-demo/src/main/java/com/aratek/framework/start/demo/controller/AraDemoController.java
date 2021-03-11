package com.aratek.framework.start.demo.controller;

import com.aratek.framework.domain.core.User;
import com.aratek.framework.start.demo.service.AraDemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(value = "demo模块接口测试", description = "接口测试", tags = "demo")
@RestController
@RequestMapping("/demo")
public class AraDemoController {

    private static Logger log = LoggerFactory.getLogger(AraDemoController.class);

    @Autowired
    private AraDemoService araDemoService;

    //    @IgnoreAuth
    @ApiOperation(value = "demo1接口", notes = "demo1接口contents")
    @RequestMapping(value = "/demo1", method = RequestMethod.POST)
    public String demo1() {
        log.info("AraDemoController.demo1");
        return "demo1";
    }

    //    @IgnoreAuth
    @ApiOperation(value = "demo2接口", notes = "demo2接口contents")
    @RequestMapping(value = "/demo2", method = RequestMethod.GET)
    public List<User> demo2() {
        log.info("AraDemoController.demo2");
        User user = new User();
        return araDemoService.findUserListDemo(user);
    }


}
