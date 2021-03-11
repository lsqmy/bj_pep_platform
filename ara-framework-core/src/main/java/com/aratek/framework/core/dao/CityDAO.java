package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.City;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-15
 * @description 城市DAO
 */
public interface CityDAO extends BaseDAO<City> {

    /**
     * 查询城市列表
     *
     * @param city
     * @return
     */
    List<City> selectCityList(City city);

    /**
     * 根据number查询城市数量
     *
     * @param fNumber
     * @return
     */
    int selectCountByNumber(@Param("fNumber") String fNumber);

    /**
     * 根据name查询城市数量
     *
     * @param fName
     * @return
     */
    int selectCountByName(@Param("fName") String fName);

    /**
     * 新增城市
     *
     * @param city
     * @return
     */
    int insertCity(City city);

    /**
     * 修改城市信息
     *
     * @param city
     * @return
     */
    int updateCity(City city);

    /**
     * 批量修改状态
     *
     * @param cityList
     * @return
     */
    int updateCityStatusBatch(List<City> cityList);

    /**
     * 根据ID删除城市
     *
     * @param cityID
     * @return
     */
    int deleteCityByID(@Param("cityID") String cityID);

    /**
     * 根据ID删除城市 - 批量
     *
     * @param idList
     * @return
     */
    int deleteCityBatch(List<String> idList);

    City selectCityByNumber(@Param("fNumber") String fNumber);

    City selectCityByName(@Param("fName") String fName);

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
