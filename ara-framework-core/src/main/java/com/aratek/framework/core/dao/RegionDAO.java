package com.aratek.framework.core.dao;

import com.aratek.framework.domain.base.mapper.BaseDAO;
import com.aratek.framework.domain.core.Region;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijinlong
 * @date 2018-05-15
 * @description 区县DAO
 */
public interface RegionDAO extends BaseDAO<Region> {

    /**
     * 查询区县列表
     *
     * @param region
     * @return
     */
    List<Region> selectRegionList(Region region);

    /**
     * 根据number查询区县数量
     *
     * @param fNumber
     * @return
     */
    int selectCountByNumber(@Param("fNumber") String fNumber);

    /**
     * 根据name查询区县数量
     *
     * @param fName
     * @return
     */
    int selectCountByName(@Param("fName") String fName);

    /**
     * 新增区县
     *
     * @param region
     * @return
     */
    int insertRegion(Region region);

    /**
     * 修改区县信息
     *
     * @param region
     * @return
     */
    int updateRegion(Region region);

    /**
     * 批量修改状态
     *
     * @param regionList
     * @return
     */
    int updateRegionStatusBatch(List<Region> regionList);

    /**
     * 根据ID删除区县
     *
     * @param regionID
     * @return
     */
    int deleteRegionByID(@Param("regionID") String regionID);

    /**
     * 根据ID删除区县 - 批量
     *
     * @param idList
     * @return
     */
    int deleteRegionBatch(List<String> idList);

    Region selectRegionByNumber(@Param("fNumber") String fNumber);

    Region selectRegionByName(@Param("fName") String fName);
}
