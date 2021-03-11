package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.AppRegister;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-23
 * @description APP注册信息 接口类
 */
public interface AppRegisterService {

    /**
     * 查询APP注册信息列表
     *
     * @param appRegister APP注册信息
     * @return
     */
    List<AppRegister> selectAppRegisterList(AppRegister appRegister);

    /**
     * 导出App升级信息列表
     *
     * @param appRegister
     * @return
     */
    Map exportAppRegisterList(AppRegister appRegister);

    /**
     * 根据ID查询APP注册信息
     *
     * @param id APP注册信息ID
     * @return
     */
    AppRegister selectAppRegisterByID(String id);

    /**
     * 新增APP注册信息
     *
     * @param appRegister APP注册信息
     * @return
     */
    Map insertAppRegister(AppRegister appRegister);

    /**
     * 修改APP注册信息信息
     *
     * @param appRegister APP注册信息
     * @return
     */
    Map updateAppRegister(AppRegister appRegister);

    /**
     * 批量修改APP注册信息状态
     *
     * @param appRegisterList APP注册信息List
     * @return
     */
    Map updateAppRegisterStatusBatch(List<AppRegister> appRegisterList);

    /**
     * 根据ID删除APP注册信息
     *
     * @param id APP注册信息ID
     * @return
     */
    Map deleteAppRegisterByID(String id);

    /**
     * 根据ID删除APP注册信息 - 批量
     *
     * @param idList APP注册信息ID List
     * @return
     */
    Map deleteAppRegisterBatch(List<String> idList);


}
