package com.ils.modules.mes.base.material.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.entity.ItemUnit;
import com.ils.modules.mes.base.material.vo.ItemVO;

/**
 * @Description: 物料定义
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
public interface ItemService extends IService<Item> {

	/**
     * 添加一对多
     * 
     * @param itemVO
     * @return Item
     */
    public Item saveMain(ItemVO itemVO);
	
	/**
     * 
     * 修改一对多
     * 
     * @param itemVO
     * @date 2020年10月29日
     */
    public void updateMain(ItemVO itemVO);
	
	/**
	 * 删除一对多
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 * @param idList
	 */
	public void delBatchMain (List<String> idList);
	
    /**
     * 
     * 根据ID查询主单位，转换单位服务
     * 
     * @param mainId
     * @return List<Unit>
     * @date 2020年11月11日
     */
    public List<Unit> queryItemUnitListByMainId(String mainId);

    /**
     * 
     * 根据ID查询主单位，转换单位服务
     * 
     * @param itemId
     * @return List<ItemUnit>
     * @date 2020年11月11日
     */
    public List<ItemUnit> queryItemUnitByItemId(String itemId);

    /**
     * 
     * 根据ID查询是否发生业务数据
     * 
     * @param itemId
     * @return
     */
    int queryBussDataByItemId(String itemId);

}
