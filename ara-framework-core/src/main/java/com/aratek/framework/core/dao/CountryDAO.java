package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.Country;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-15
 * @description 国家DAO
 */
public interface CountryDAO extends BaseDAO<Country> {

    /**
     * 查询国家列表
     *
     * @param country
     * @return
     */
    List<Country> selectCountryList(Country country);

    /**
     * 根据number查询国家数量
     *
     * @param fNumber
     * @return
     */
    int selectCountByNumber(@Param("fNumber") String fNumber);

    /**
     * 根据name查询国家数量
     *
     * @param fName
     * @return
     */
    int selectCountByName(@Param("fName") String fName);

    /**
     * 新增国家
     *
     * @param country
     * @return
     */
    int insertCountry(Country country);

    /**
     * 修改国家信息
     *
     * @param country
     * @return
     */
    int updateCountry(Country country);

    /**
     * 批量修改状态
     *
     * @param countryList
     * @return
     */
    int updateCountryStatusBatch(List<Country> countryList);

    /**
     * 根据ID删除国家
     *
     * @param countryID
     * @return
     */
    int deleteCountryByID(@Param("countryID") String countryID);

    /**
     * 根据ID删除国家 - 批量
     *
     * @param idList
     * @return
     */
    int deleteCountryBatch(List<String> idList);

    Country selectCountryByNumber(@Param("fNumber") String fNumber);

    Country selectCountryByName(@Param("fName") String fName);

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
