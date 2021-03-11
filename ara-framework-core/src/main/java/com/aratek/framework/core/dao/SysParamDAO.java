package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.SysParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-18
 * @description 系统参数DAO
 */
public interface SysParamDAO extends BaseDAO<SysParam> {

    /**
     * 查询系统参数列表
     *
     * @param sysParam
     * @return
     */
    List<SysParam> selectSysParamList(SysParam sysParam);

    /**
     * 根据number查询系统参数
     *
     * @param fNumber
     * @return
     */
    SysParam selectSysParamByNumber(@Param("fNumber") String fNumber);

    /**
     * 根据name查询系统参数数量
     *
     * @param fName
     * @return
     */
    int selectCountByName(@Param("fName") String fName);

    /**
     * 新增系统参数
     *
     * @param sysParam
     * @return
     */
    int insertSysParam(SysParam sysParam);

    /**
     * 修改系统参数信息
     *
     * @param sysParam
     * @return
     */
    int updateSysParam(SysParam sysParam);

    /**
     * 根据ID删除系统参数
     *
     * @param sysParamID
     * @return
     */
    int deleteSysParamByID(@Param("sysParamID") String sysParamID);

    /**
     * 根据ID删除系统参数 - 批量
     *
     * @param idList
     * @return
     */
    int deleteSysParamBatch(List<String> idList);
}
