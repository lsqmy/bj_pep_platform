package com.aratek.framework.core.controller;

import com.aratek.framework.core.annotation.IgnoreAuth;
import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.constant.AraResultCodeConstants;
import com.aratek.framework.core.exception.AraAuthException;
import com.aratek.framework.core.service.LoginService;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.base.controller.BaseController;
import com.aratek.framework.domain.core.AppRegister;
import com.aratek.framework.domain.core.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-04-28
 * @description 登录注销
 */
@Api(tags = "登录注销接口", description = "登录注销相关接口")
@RestController
public class LoginController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @IgnoreAuth
    @ApiOperation(value = "根节点", notes = "重定向到swagger-ui.html")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("swagger-ui.html");
    }

    /**
     * 登录，获取token，记录登录日志
     *
     * @param user
     * @return
     */
    @IgnoreAuth
    @ApiOperation(value = "登录", notes = "登录成功会返回token")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Map login(@RequestBody User user) {
        return loginService.login(user);
    }

    /**
     * 重新登录，获取token，不记录登录日志
     * <p>
     * //     * @param user
     *
     * @return
     */
    /*@IgnoreAuth
    @IgnoreSysLog
    @ApiOperation(value = "刷新token", notes = "刷新成功会返回token")
    @RequestMapping(value = "refreshToken", method = RequestMethod.POST)
    public Map refreshToken(@RequestBody User user) {
        return loginService.refreshToken(user);
    }*/
    @IgnoreAuth
    @ApiOperation(value = "注销", notes = "注销当前用户")
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public Map logout() {
        return loginService.logout();
    }

    @ApiOperation(value = "未登录", notes = "返回响应码401")
    @RequestMapping(value = "401", method = RequestMethod.GET)
    public void unAuthorized() {
        throw new AraAuthException(AraCoreConstants.HTTP_STATUS_UNAUTHORIZED, "未登录");
    }

    @ApiOperation(value = "无权限", notes = "返回响应码403")
    @RequestMapping(value = "403", method = RequestMethod.GET)
    public void forbidden() {
        throw new AraAuthException(AraCoreConstants.HTTP_STATUS_FORBIDDEN, "权限不足");
    }

    @IgnoreAuth
    @ApiOperation(value = "APP登录", notes = "APP登录成功会返回token")
    @RequestMapping(value = "appLogin", method = RequestMethod.POST)
    public Map appLogin(@RequestBody AppRegister appRegister) {
        if (StringUtil.isBlank(appRegister.getfAppID())) {
            LOGGER.warn("appID is null!");
            return Result.error(AraResultCodeConstants.CODE_1403);
        }
        if (StringUtil.isBlank(appRegister.getfSecretKey())) {
            LOGGER.warn("app secret key is null!");
            return Result.error(AraResultCodeConstants.CODE_1407);
        }
        return loginService.appLogin(appRegister);
    }


    /*@SysLogAnnotation("test1")
    @ApiOperation(value = "test1", notes = "test1")
    @RequestMapping(value = "test1", method = RequestMethod.POST)
    public Map test1(@RequestBody User user) {
        return Result.ok();
    }

    @OpenApiAnnotation("test2")
    @ApiOperation(value = "test2", notes = "test2")
    @RequestMapping(value = "test2", method = RequestMethod.POST)
    public Map test2(@RequestBody User user) {
        return Result.ok();
    }

    @SysLogAnnotation
//    @RequiresPermissions(value = {"9999:test"})
    @ApiOperation(value = "test3", notes = "test3")
    @RequestMapping(value = "test3", method = RequestMethod.POST)
    public Map test3(@RequestBody User user) {
        return Result.ok();
    }*/


}
