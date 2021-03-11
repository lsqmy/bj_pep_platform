package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.Country;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-15
 * @description 国家服务 接口类
 */
public interface CountryService {

    /**
     * 查询国家列表
     *
     * @param country 国家
     * @return
     */
    List<Country> selectCountryList(Country country);

    /**
     * 导出国家列表
     *
     * @param country
     * @return
     */
    Map exportCountryList(Country country);

    /**
     * 新增国家
     *
     * @param country 国家
     * @return
     */
    Map insertCountry(Country country);

    /**
     * 修改国家信息
     *
     * @param country 国家
     * @return
     */
    Map updateCountry(Country country);

    /**
     * 批量修改国家状态
     *
     * @param countryList 国家List
     * @return
     */
    Map updateCountryStatusBatch(List<Country> countryList);

    /**
     * 根据ID删除国家
     *
     * @param countryID 国家ID
     * @return
     */
    Map deleteCountryByID(String countryID);

    /**
     * 根据ID删除国家 - 批量
     *
     * @param idList 国家ID List
     * @return
     */
    Map deleteCountryBatch(List<String> idList);


}
