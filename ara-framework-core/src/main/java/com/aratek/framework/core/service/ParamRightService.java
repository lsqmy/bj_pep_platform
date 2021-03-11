package com.aratek.framework.core.service;

import com.aratek.framework.domain.core.ParamRight;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-06-22
 * @description 权限参数 接口类
 */
public interface ParamRightService {
    /**
     * 分页查询权限参数列表
     *
     * @param paramRight
     * @return
     */
    List<ParamRight> selectParamRightList(ParamRight paramRight);

    /**
     * 导出权限参数列表
     *
     * @param paramRight
     * @return
     */
    Map exportParamRightList(ParamRight paramRight);

    /**
     * 新增权限参数
     *
     * @param paramRight
     * @return
     */
    Map insertParamRight(ParamRight paramRight);

    /**
     * 修改权限参数信息
     *
     * @param paramRight
     * @return
     */
    Map updateParamRight(ParamRight paramRight);

    /**
     * 批量根据ID删除权限参数
     *
     * @param idList
     * @return
     */
    Map deleteParamRightBatch(List<String> idList);


}
