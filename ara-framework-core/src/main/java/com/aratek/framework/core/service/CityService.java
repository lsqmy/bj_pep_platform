package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.City;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-15
 * @description 城市服务 接口类
 */
public interface CityService {

    /**
     * 查询城市列表
     *
     * @param city 城市
     * @return
     */
    List<City> selectCityList(City city);

    /**
     * 导出城市列表
     *
     * @param city
     * @return
     */
    Map exportCityList(City city);

    /**
     * 新增城市
     *
     * @param city 城市
     * @return
     */
    Map insertCity(City city);

    /**
     * 修改城市信息
     *
     * @param city 城市
     * @return
     */
    Map updateCity(City city);

    /**
     * 批量修改城市状态
     *
     * @param cityList 城市List
     * @return
     */
    Map updateCityStatusBatch(List<City> cityList);

    /**
     * 根据ID删除城市
     *
     * @param cityID 城市ID
     * @return
     */
    Map deleteCityByID(String cityID);

    /**
     * 根据ID删除城市 - 批量
     *
     * @param idList 城市ID List
     * @return
     */
    Map deleteCityBatch(List<String> idList);


}
