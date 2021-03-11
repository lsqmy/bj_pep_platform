package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.Province;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-15
 * @description 省份服务 接口类
 */
public interface ProvinceService {

    /**
     * 查询省份列表
     *
     * @param province 省份
     * @return
     */
    List<Province> selectProvinceList(Province province);

    /**
     * 导出省份列表
     *
     * @param province
     * @return
     */
    Map exportProvinceList(Province province);

    /**
     * 新增省份
     *
     * @param province 省份
     * @return
     */
    Map insertProvince(Province province);

    /**
     * 修改省份信息
     *
     * @param province 省份
     * @return
     */
    Map updateProvince(Province province);

    /**
     * 批量修改省份状态
     *
     * @param provinceList 省份List
     * @return
     */
    Map updateProvinceStatusBatch(List<Province> provinceList);

    /**
     * 根据ID删除省份
     *
     * @param provinceID 省份ID
     * @return
     */
    Map deleteProvinceByID(String provinceID);

    /**
     * 根据ID删除省份 - 批量
     *
     * @param idList 省份ID List
     * @return
     */
    Map deleteProvinceBatch(List<String> idList);


}
