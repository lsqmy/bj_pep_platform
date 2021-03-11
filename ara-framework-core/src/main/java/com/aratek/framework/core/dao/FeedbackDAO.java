package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.Feedback;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-22
 * @description 意见反馈DAO
 */
public interface FeedbackDAO extends BaseDAO<Feedback> {

    /**
     * 查询意见反馈列表
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
    int insertFeedback(Feedback feedback);

    /**
     * 修改意见反馈
     *
     * @param feedback
     * @return
     */
    int updateFeedback(Feedback feedback);

    /**
     * 根据ID删除意见反馈
     *
     * @param fID
     * @return
     */
    int deleteFeedbackByID(@Param("fID") String fID);

    /**
     * 根据ID删除意见反馈 - 批量
     *
     * @param idList
     * @return
     */
    int deleteFeedbackBatch(List<String> idList);

}
