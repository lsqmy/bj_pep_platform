//package com.aratek.framework.core.controller;
//
//import com.aratek.framework.core.annotation.UserLogAnnotation;
//import com.aratek.framework.core.constant.AraCoreConstants;
//import com.aratek.framework.core.constant.AraResultCodeConstants;
//import com.aratek.framework.core.service.FeedbackService;
//import com.aratek.framework.core.util.StringUtil;
//import com.aratek.framework.domain.base.Result;
//import com.aratek.framework.domain.core.Feedback;
//import com.github.pagehelper.PageInfo;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * @author shijinlong
// * @date 2018-05-22
// * @description 意见反馈接口
// */
//@Api(tags = "意见反馈接口", description = "意见反馈增删改查相关接口")
//@RestController
//@RequestMapping("feedback")
//public class FeedbackController extends BaseController {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);
//
//    @Autowired
//    private FeedbackService feedbackService;
//
//    @RequiresPermissions(value = {"9999:select"})
//    @UserLogAnnotation(module = "feedback", actType = "select")
//    @ApiOperation(value = "分页查询意见反馈列表", notes = "分页查询意见反馈列表")
//    @RequestMapping(value = "selectFeedbackList", method = RequestMethod.POST)
//    public Map selectFeedbackList(@RequestBody Feedback feedback) {
//        PageInfo pageInfo = new PageInfo(feedbackService.selectFeedbackList(feedback));
//        return Result.ok().putData(pageInfo);
//    }
//
//    @RequiresPermissions(value = {"9999:insert"})
//    @UserLogAnnotation(module = "feedback", actType = "insert")
//    @ApiOperation(value = "新增意见反馈", notes = "新增意见反馈")
//    @RequestMapping(value = "insertFeedback", method = RequestMethod.POST)
//    public Map insertFeedback(@RequestBody Feedback feedback) {
//        if (StringUtil.isBlank(feedback.getfSuggestion())) {
//            LOGGER.warn("意见为空!");
//            return Result.error(AraResultCodeConstants.CODE_1000);
//        }
//        //默认状态为:正常
//        feedback.setfStatus(AraCoreConstants.STATUS_ENABLE);
//        return feedbackService.insertFeedback(feedback);
//    }
//
//    @RequiresPermissions(value = {"9999:update"})
//    @UserLogAnnotation(module = "feedback", actType = "update")
//    @ApiOperation(value = "修改意见反馈信息", notes = "修改意见反馈信息")
//    @RequestMapping(value = "updateFeedback", method = RequestMethod.POST)
//    public Map updateFeedback(@RequestBody Feedback feedback) {
//        if (StringUtil.isBlank(feedback.getfID())) {
//            LOGGER.warn("ID为空!");
//            return Result.error(AraResultCodeConstants.CODE_1001);
//        }
//        return feedbackService.updateFeedback(feedback);
//    }
//
//    @RequiresPermissions(value = {"9999:delete"})
//    @UserLogAnnotation(module = "feedback", actType = "delete")
//    @ApiOperation(value = "根据ID删除意见反馈", notes = "根据ID删除意见反馈")
//    @RequestMapping(value = "deleteFeedbackByID", method = RequestMethod.DELETE)
//    public Map deleteFeedbackByID(@RequestBody String fID) {
//        if (StringUtil.isBlank(fID)) {
//            LOGGER.warn("ID为空!");
//            return Result.error(AraResultCodeConstants.CODE_1001);
//        }
//        return feedbackService.deleteFeedbackByID(fID);
//    }
//
//    @RequiresPermissions(value = {"9999:delete"})
//    @UserLogAnnotation(module = "feedback", actType = "delete")
//    @ApiOperation(value = "批量根据ID删除意见反馈", notes = "批量根据ID删除意见反馈")
//    @RequestMapping(value = "deleteFeedbackBatch", method = RequestMethod.DELETE)
//    public Map deleteFeedbackBatch(@RequestBody List<String> idList) {
//        if (idList == null || idList.size() == 0) {
//            LOGGER.warn("ID为空!");
//            return Result.error(AraResultCodeConstants.CODE_1001);
//        }
//        return feedbackService.deleteFeedbackBatch(idList);
//    }
//}
