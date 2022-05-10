package com.ils.modules.mes.base.product.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.product.entity.ItemBomDetail;
import com.ils.modules.mes.base.product.vo.ItemBomDetailVO;
import com.ils.modules.mes.base.product.vo.ItemBomUnitVO;

/**
 * @Description: 物料BOM明细表
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
public interface ItemBomDetailMapper extends ILSMapper<ItemBomDetail> {
    
    /**
     * 通过主表 Id 删除
     * @param mainId
     * @return
     */
	public boolean deleteByMainId(String mainId);
    
    /**
     * 通过主表 Id 查询
     * @param mainId
     * @return
     */
    public List<ItemBomDetailVO> selectByMainId(String mainId);

    /**
     * 通过主表 Id 查询物料清单明细+对应单位的转换关系
     * 
     * @param mainId
     * @return
     */
    public List<ItemBomUnitVO> selectDetailInfoByMainId(String mainId);

    /**
     * 根据bom id查找
     *
     * @param bomId
     * @return List<ItemBomDetailVO>
     */
    public List<ItemBomDetail> selectByBomId(String bomId);
}
