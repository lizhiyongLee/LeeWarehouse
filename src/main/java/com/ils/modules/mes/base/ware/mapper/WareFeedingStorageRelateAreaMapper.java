package com.ils.modules.mes.base.ware.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.ware.entity.WareFeedingStorageRelateArea;
import com.ils.modules.mes.base.ware.vo.RelatedStorageVO;
import com.ils.modules.mes.base.ware.vo.RelatedVO;
import com.ils.modules.mes.machine.entity.MachineRepairTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 投料仓位
 * @Author: Tian
 * @Date:   2020-12-04
 * @Version: V1.0
 */
public interface WareFeedingStorageRelateAreaMapper extends ILSMapper<WareFeedingStorageRelateArea> {

    /**
     * 查询关联仓位
     * @param id
     * @return
     */
    public List<WareFeedingStorageRelateArea> selectFeedingStorageByAreaId(String id);

    /**
     * 查询投料仓位By区域Id
     * @return
     */
    public List<RelatedVO> selectFeedingStoragesByAreaId();

    /**
     * 查询线边仓
     * @param queryWrapper
     * @return
     */
    public List<RelatedStorageVO> selectRelatedStorageVO(@Param("ew") QueryWrapper<RelatedStorageVO> queryWrapper);
    /**
     * 通过区域id删除关联仓位
     * @param areaId
     */
    public void delByAreaId(String areaId);
}
