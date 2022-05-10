package com.ils.modules.mes.base.product.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.product.entity.ItemBomDetail;
import com.ils.modules.mes.base.product.vo.ItemBomDetailVO;
import com.ils.modules.mes.base.product.vo.ItemBomUnitVO;

/**
 * @Description: 物料BOM明细表
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
public interface ItemBomDetailService extends IService<ItemBomDetail> {

    /**
     * 通过父ID查询列表
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
