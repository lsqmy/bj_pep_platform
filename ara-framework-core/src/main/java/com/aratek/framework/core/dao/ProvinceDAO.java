package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.Province;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-15
 * @description 省份DAO
 */
public interface ProvinceDAO extends BaseDAO<Province> {

    /**
     * 查询省份列表
     *
     * @param province
     * @return
     */
    List<Province> selectProvinceList(Province province);

    /**
     * 根据number查询省份数量
     *
     * @param fNumber
     * @return
     */
    int selectCountByNumber(@Param("fNumber") String fNumber);

    /**
     * 根据name查询省份数量
     *
     * @param fName
     * @return
     */
    int selectCountByName(@Param("fName") String fName);

    /**
     * 新增省份
     *
     * @param province
     * @return
     */
    int insertProvince(Province province);

    /**
     * 修改省份信息
     *
     * @param province
     * @return
     */
    int updateProvince(Province province);

    /**
     * 批量修改状态
     *
     * @param provinceList
     * @return
     */
    int updateProvinceStatusBatch(List<Province> provinceList);

    /**
     * 根据ID删除省份
     *
     * @param provinceID
     * @return
     */
    int deleteProvinceByID(@Param("provinceID") String provinceID);

    /**
     * 根据ID删除省份 - 批量
     *
     * @param idList
     * @return
     */
    int deleteProvinceBatch(List<String> idList);

    Province selectProvinceByNumber(@Param("fNumber") String fNumber);

    Province selectProvinceByName(@Param("fName") String fName);

    /**
     * 根据ID查询引用计数
     *
     * @param fID
     * @return
     */
    int selectCountReferenceByID(@Param("fID") String fID);

    /**
     * 根据ID list查询引用计数
     *
     * @param idList
     * @return
     */
    int selectCountReferenceByIDList(List<String> idList);
}
