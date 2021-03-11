package com.aratek.framework.core.service.impl;

import com.aratek.framework.core.constant.AraCoreConstants;
import com.aratek.framework.core.dao.FeedbackDAO;
import com.aratek.framework.core.service.FeedbackService;
import com.aratek.framework.core.util.CurrentUserUtil;
import com.aratek.framework.core.util.PageUtil;
import com.aratek.framework.core.util.UUIDUtil;
import com.aratek.framework.domain.base.Result;
import com.aratek.framework.domain.core.Feedback;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * @author shijinlong
 * @date 2018-05-22
 * @description 意见反馈服务 实现类
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    @Autowired
    private FeedbackDAO feedbackDAO;

    @Override
    public List<Feedback> selectFeedbackList(Feedback feedback) {
        PageHelper.startPage(feedback.getPageNum(), feedback.getPageSize(), PageUtil.getSortStr(feedback.getSortParams()));
        return feedbackDAO.selectFeedbackList(feedback);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map insertFeedback(Feedback feedback) {
        //生成ID
        feedback.setfID(UUIDUtil.genID());
        //设置状态初始值
        feedback.setfStatus(AraCoreConstants.STATUS_ENABLE);
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(feedback);
        //insert
        feedbackDAO.insertFeedback(feedback);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map updateFeedback(Feedback feedback) {
        //设置当前操作人及操作时间
        CurrentUserUtil.setOperatorInfo(feedback);
        //update
        feedbackDAO.updateFeedback(feedback);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteFeedbackByID(String feedbackID) {
        //delete
        feedbackDAO.deleteFeedbackByID(feedbackID);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map deleteFeedbackBatch(List<String> idList) {
        //delete
        feedbackDAO.deleteFeedbackBatch(idList);
        return Result.ok();
    }
}
