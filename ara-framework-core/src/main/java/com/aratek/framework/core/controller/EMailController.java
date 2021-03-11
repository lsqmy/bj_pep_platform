//package com.aratek.framework.core.controller;
//
//import com.aratek.framework.core.annotation.UserLogAnnotation;
//import com.aratek.framework.core.service.EmailService;
//import com.aratek.framework.domain.base.Result;
//import com.aratek.framework.domain.core.Email;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
///**
// * @author shijinlong
// * @date 2018-05-11
// * @description 邮件接口
// */
//@Api(tags = "邮件接口", description = "邮件相关接口")
//@RestController
//@RequestMapping("mail")
//public class EMailController extends BaseController {
//
//    @Autowired
//    private EmailService emailService;
//
//    @UserLogAnnotation(module = "mail", actType = "sendEmail")
//    @ApiOperation(value = "发送简单邮件", notes = "发送简单邮件")
//    @RequestMapping(value = "sendSimpleMail", method = RequestMethod.POST)
//    public Map sendSimpleMail(@RequestBody Email email) {
//        emailService.sendSimpleMail(email);
//        return Result.ok();
//    }
//
//    @UserLogAnnotation(module = "mail", actType = "sendEmail")
//    @ApiOperation(value = "发送简单邮件-带附件", notes = "发送简单邮件-带附件")
//    @RequestMapping(value = "sendAttachmentsMail", method = RequestMethod.POST)
//    public Map sendAttachmentsMail(@RequestBody Email email) {
//        emailService.sendAttachmentsMail(email);
//        return Result.ok();
//    }
//
//    @UserLogAnnotation(module = "mail", actType = "sendEmail")
//    @ApiOperation(value = "发送模板邮件", notes = "发送模板邮件")
//    @RequestMapping(value = "sendTemplateMail", method = RequestMethod.POST)
//    public Map sendTemplateMail(@RequestBody Email email) {
//        emailService.sendTemplateMail(email);
//        return Result.ok();
//    }
//}
