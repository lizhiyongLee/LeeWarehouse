package com.ils.modules.mes.produce.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.SaleOrder;
import com.ils.modules.mes.produce.entity.SaleOrderLine;
import com.ils.modules.mes.produce.vo.SaleOrderVO;

/**
 * @Description: 销售订单
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
public interface SaleOrderService extends IService<SaleOrder> {

	/**
	 * 添加一对多
	 * @param saleOrder
    * @param saleOrderLineList
	 */
	public void saveMain(SaleOrder saleOrder,List<SaleOrderLine> saleOrderLineList) ;
	
	/**
	 * 修改一对多
	 * @param saleOrder
    * @param saleOrderLineList
	 */
	public void updateMain(SaleOrder saleOrder,List<SaleOrderLine> saleOrderLineList);
	
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
	 * 批量更改状态
	 * @param status
	 * @param idList
	 */
	public void updateStatus(String status , List<String> idList);
}
