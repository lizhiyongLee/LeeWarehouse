package com.ils.modules.mes.material.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.material.entity.ItemReceiptHead;
import com.ils.modules.mes.material.entity.ItemReceiptLine;
import com.ils.modules.mes.material.vo.ItemReceiptHeadVO;

/**
 * @Description: 收货单头
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
public interface ItemReceiptHeadService extends IService<ItemReceiptHead> {

	/**
	 * 添加一对多
	 * @param itemReceiptHead
    * @param itemReceiptLineList
	 */
	public void saveMain(ItemReceiptHead itemReceiptHead,List<ItemReceiptLine> itemReceiptLineList) ;

	/**
	 * 批量添加
	 * @param itemReceiptHeadList
	 */
	public void saveBatchMain(List<ItemReceiptHeadVO> itemReceiptHeadList) ;

	/**
	 * 修改一对多
	 * @param itemReceiptHead
    * @param itemReceiptLineList
	 */
	public void updateMain(ItemReceiptHead itemReceiptHead,List<ItemReceiptLine> itemReceiptLineList);
	
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
