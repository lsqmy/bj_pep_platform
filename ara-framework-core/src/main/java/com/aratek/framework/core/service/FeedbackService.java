package com.aratek.framework.core.service;

import com.aratek.framework.domain.core.Feedback;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-22
 * @description 意见反馈服务 接口类
 */
public interface FeedbackService {

    /**
     * 分页查询意见反馈列表
     *
     * @param feedback
     * @return
     */
    List<Feedback> selectFeedbackList(Feedback feedback);

    /**
     * 新增意见反馈
     *
     * @param feedback
     * @return
     */
    Map insertFeedback(Feedback feedback);

    /**
     * 修改意见反馈信息
     *
     * @param feedback
     * @return
     */
    Map updateFeedback(Feedback feedback);

    /**
     * 根据ID删除意见反馈
     *
     * @param id
     * @return
     */
    Map deleteFeedbackByID(String id);

    /**
     * 批量根据ID删除意见反馈
     *
     * @param idList
     * @return
     */
    Map deleteFeedbackBatch(List<String> idList);
}
