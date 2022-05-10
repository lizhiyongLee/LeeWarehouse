package com.ils.modules.mes.produce.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.PurchaseOrderLine;
import com.ils.modules.mes.produce.vo.PurchaseOrderLinePageVO;

import java.util.List;

/**
 * @Description: 采购清单物料行
 * @Author: Tian
 * @Date: 2021-01-28
 * @Version: V1.0
 */
public interface PurchaseOrderLineService extends IService<PurchaseOrderLine> {

    /**
     * 添加
     *
     * @param purchaseOrderLine
     */
    public void savePurchaseOrderLine(PurchaseOrderLine purchaseOrderLine);

    /**
     * 修改
     *
     * @param purchaseOrderLine
     */
    public void updatePurchaseOrderLine(PurchaseOrderLine purchaseOrderLine);

    /**
     * 删除
     *
     * @param id
     */
    public void delPurchaseOrderLine(String id);

    /**
     * 批量删除
     *
     * @param idList
     */
    public void delBatchPurchaseOrderLine(List<String> idList);

    /**
     * 通过采购清单id查询采购清单物料行集合
     * @param mainId
     * @return
     */
    public List<PurchaseOrderLine> queryByMainId(String mainId);

    /**
     * 分页查询采购清单物料行
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<PurchaseOrderLinePageVO> listPage(Page<PurchaseOrderLinePageVO> page, QueryWrapper<PurchaseOrderLinePageVO> queryWrapper);

}
