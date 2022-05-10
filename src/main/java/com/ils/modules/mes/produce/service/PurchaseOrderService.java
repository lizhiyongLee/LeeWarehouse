package com.ils.modules.mes.produce.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.PurchaseOrder;
import com.ils.modules.mes.produce.vo.PurchaseOrderDetailVO;
import com.ils.modules.mes.produce.vo.PurchaseOrderLineVO;
import com.ils.modules.mes.produce.vo.PurchaseOrderVO;

import java.util.List;

/**
 * @Description: 采购清单
 * @Author: Tian
 * @Date: 2021-01-28
 * @Version: V1.0
 */
public interface PurchaseOrderService extends IService<PurchaseOrder> {

    /**
     * 添加
     *
     * @param purchaseOrderLineVO
     */
    public void savePurchaseOrder(PurchaseOrderLineVO purchaseOrderLineVO);

    /**
     * 修改
     *
     * @param purchaseOrderLineVO
     */
    public void updatePurchaseOrder(PurchaseOrderLineVO purchaseOrderLineVO);

    /**
     * 删除
     *
     * @param id
     */
    public void delPurchaseOrder(String id);

    /**
     * 批量删除
     *
     * @param idList
     */
    public void delBatchPurchaseOrder(List<String> idList);

    /**
     * 采购清单分页查询
     *
     * @param purchaseOrderPage
     * @param purchaseOrderQueryWrapper
     * @param itemCode
     * @param itemName
     * @return
     */
    public IPage listPage(Page<PurchaseOrderVO> purchaseOrderPage,
                          QueryWrapper<PurchaseOrderVO> purchaseOrderQueryWrapper, String itemCode, String itemName);

    /**
     * 通过采购订单id查询采购订单详情
     *
     * @param orderId
     * @return
     */
    public PurchaseOrderDetailVO queryDetailById(String orderId);


    /**
     * 批量更改状态
     * @param status
     * @param idList
     */
    public void updateStatus(String status , List<String> idList);
}
