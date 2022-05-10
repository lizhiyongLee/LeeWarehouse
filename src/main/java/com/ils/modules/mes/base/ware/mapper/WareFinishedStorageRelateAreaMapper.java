package com.ils.modules.mes.base.ware.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.ware.entity.WareFinishedStorageRelateArea;
import com.ils.modules.mes.base.ware.vo.RelatedVO;

import java.util.List;

/**
 * @Description: 完工仓位
 * @Author: Tian
 * @Date:   2020-12-04
 * @Version: V1.0
 */
public interface WareFinishedStorageRelateAreaMapper extends ILSMapper<WareFinishedStorageRelateArea> {

    /**
     * 查询完工仓位
     * @param id
     * @return
     */
    public WareFinishedStorageRelateArea selectFinishStorageByAreaId(String id);

    /**
     * 查询完工仓位By区域id
     * @return
     */
    public List<RelatedVO> selectFinishStoragesByAreaId();

    /**
     * 通过区域id删除关联仓位
     * @param areaId
     */
    public void delByAreaId(String areaId);
}
