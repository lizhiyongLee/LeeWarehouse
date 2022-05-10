package com.ils.modules.mes.base.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.product.entity.ItemBom;
import com.ils.modules.mes.base.product.vo.ItemBomVO;

import java.util.List;

/**
 * @Description: 物料BOM
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
public interface ItemBomService extends IService<ItemBom> {

	/**
     * 添加一对多
     * 
     * @param itemBomVO
     * @return ItemBom
     */
    public ItemBom saveMain(ItemBomVO itemBomVO);
	
	/**
     * 修改一对多
     * 
     * @param itemBomVO
     */
    public void updateMain(ItemBomVO itemBomVO);

    /**
     * 根据物料id查找
     *
     * @param itemId
     * @return List<ItemBom>
     */
    public List<ItemBom> selectByItemIdAndOrderByUpdateTime(String itemId);

}
