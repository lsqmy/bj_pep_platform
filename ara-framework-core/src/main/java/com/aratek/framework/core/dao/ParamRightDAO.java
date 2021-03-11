package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.ParamRight;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-06-22
 * @description 权限参数DAO
 */
public interface ParamRightDAO extends BaseDAO<ParamRight> {

    /**
     * 查询权限参数列表
     *
     * @param paramRight
     * @return
     */
    List<ParamRight> selectParamRightList(ParamRight paramRight);

    /**
     * 新增权限参数
     *
     * @param paramRight
     * @return
     */
    int insertParamRight(ParamRight paramRight);

    /**
     * 修改权限参数
     *
     * @param paramRight
     * @return
     */
    int updateParamRight(ParamRight paramRight);

    /**
     * 批量删除权限参数
     *
     * @param idList
     * @return
     */
    int deleteParamRightBatch(List<String> idList);

}
