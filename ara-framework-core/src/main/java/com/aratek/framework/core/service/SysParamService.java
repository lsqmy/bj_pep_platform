package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.SysParam;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-18
 * @description 系统参数 接口类
 */
public interface SysParamService {

    /**
     * 查询系统参数列表
     *
     * @param sysParam 系统参数
     * @return
     */
    List<SysParam> selectSysParamList(SysParam sysParam);

    /**
     * 导出系统参数列表
     *
     * @param sysParam
     * @return
     */
    Map exportSysParamList(SysParam sysParam);

    /**
     * 新增系统参数
     *
     * @param sysParam 系统参数
     * @return
     */
    Map insertSysParam(SysParam sysParam);

    /**
     * 修改系统参数信息
     *
     * @param sysParam 系统参数
     * @return
     */
    Map updateSysParam(SysParam sysParam);

    /**
     * 根据ID删除系统参数
     *
     * @param sysParamID 系统参数ID
     * @return
     */
    Map deleteSysParamByID(String sysParamID);

    /**
     * 根据ID删除系统参数 - 批量
     *
     * @param idList 系统参数ID List
     * @return
     */
    Map deleteSysParamBatch(List<String> idList);


}
