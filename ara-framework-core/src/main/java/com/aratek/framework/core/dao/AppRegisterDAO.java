package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.AppRegister;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-23
 * @description APP注册信息 DAO
 */
public interface AppRegisterDAO extends BaseDAO<AppRegister> {

    /**
     * 查询APP注册信息列表
     *
     * @param appRegister
     * @return
     */
    List<AppRegister> selectAppRegisterList(AppRegister appRegister);

    /**
     * 根据ID查询APP注册信息
     *
     * @param id
     * @return
     */
    AppRegister selectAppRegisterByID(@Param("id") String id);

    /**
     * 根据AppID查询APP注册信息
     *
     * @param id
     * @return
     */
    AppRegister selectAppRegisterByAppID(@Param("id") String id);

    /**
     * 新增APP注册信息
     *
     * @param appRegister
     * @return
     */
    int insertAppRegister(AppRegister appRegister);

    /**
     * 修改APP注册信息
     *
     * @param appRegister
     * @return
     */
    int updateAppRegister(AppRegister appRegister);

    /**
     * 批量修改状态
     *
     * @param appRegisterList
     * @return
     */
    int updateAppRegisterStatusBatch(List<AppRegister> appRegisterList);

    /**
     * 根据ID删除APP注册信息
     *
     * @param id
     * @return
     */
    int deleteAppRegisterByID(@Param("id") String id);

    /**
     * 根据ID删除APP注册信息 - 批量
     *
     * @param idList
     * @return
     */
    int deleteAppRegisterBatch(List<String> idList);

    /**
     * 根据AppID和SecretKey查询APP注册信息
     *
     * @param appRegister
     * @return
     */
    AppRegister selectAppRegisterByIDAndSecretKey(AppRegister appRegister);
}
