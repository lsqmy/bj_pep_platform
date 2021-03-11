package com.aratek.framework.core.service;


import com.aratek.framework.domain.core.Region;

import java.util.List;
import java.util.Map;

/**
 * @author shijinlong
 * @date 2018-05-15
 * @description 区县服务 接口类
 */
public interface RegionService {

    /**
     * 查询区县列表
     *
     * @param region 区县
     * @return
     */
    List<Region> selectRegionList(Region region);

    /**
     * 导出区县列表
     *
     * @param region
     * @return
     */
    Map exportRegionList(Region region);

    /**
     * 新增区县
     *
     * @param region 区县
     * @return
     */
    Map insertRegion(Region region);

    /**
     * 修改区县信息
     *
     * @param region 区县
     * @return
     */
    Map updateRegion(Region region);

    /**
     * 批量修改区县状态
     *
     * @param regionList 区县List
     * @return
     */
    Map updateRegionStatusBatch(List<Region> regionList);

    /**
     * 根据ID删除区县
     *
     * @param regionID 区县ID
     * @return
     */
    Map deleteRegionByID(String regionID);

    /**
     * 根据ID删除区县 - 批量
     *
     * @param idList 区县ID List
     * @return
     */
    Map deleteRegionBatch(List<String> idList);


}
