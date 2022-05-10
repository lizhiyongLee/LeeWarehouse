package com.ils.modules.mes.produce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.factory.entity.Supplier;
import com.ils.modules.mes.base.factory.mapper.SupplierMapper;
import com.ils.modules.mes.produce.entity.PurchaseOrder;
import com.ils.modules.mes.produce.entity.PurchaseOrderLine;
import com.ils.modules.mes.produce.mapper.PurchaseOrderLineMapper;
import com.ils.modules.mes.produce.mapper.PurchaseOrderMapper;
import com.ils.modules.mes.produce.service.PurchaseOrderService;
import com.ils.modules.mes.produce.vo.PurchaseOrderDetailVO;
import com.ils.modules.mes.produce.vo.PurchaseOrderLineVO;
import com.ils.modules.mes.produce.vo.PurchaseOrderVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 采购清单
 * @Author: Conner
 * @Date: 2021-01-28
 * @Version: V1.0
 */
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderLineMapper purchaseOrderLineMapper;
    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public void savePurchaseOrder(PurchaseOrderLineVO purchaseOrderLineVO) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        BeanUtils.copyProperties(purchaseOrderLineVO, purchaseOrder);
        Supplier supplier = supplierMapper.selectById(purchaseOrder.getSupplierId());
        purchaseOrder.setSupplierName(supplier.getSupplierName());
        baseMapper.insert(purchaseOrder);
        for (PurchaseOrderLine purchaseOrderLine : purchaseOrderLineVO.getPurchaseOrderLineList()) {
            purchaseOrderLine.setPurchaseOrderId(purchaseOrder.getId());
            purchaseOrderLineMapper.insert(purchaseOrderLine);
        }
    }

    @Override
    public void updatePurchaseOrder(PurchaseOrderLineVO purchaseOrderLineVO) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        BeanUtils.copyProperties(purchaseOrderLineVO, purchaseOrder);
        PurchaseOrder purchaseOrderOld = baseMapper.selectById(purchaseOrderLineVO.getId());
        purchaseOrder.setStatus(purchaseOrderOld.getStatus());
        baseMapper.updateById(purchaseOrder);
        List<PurchaseOrderLine> purchaseOrderLineList = purchaseOrderLineVO.getPurchaseOrderLineList();
        for (PurchaseOrderLine purchaseOrderLine : purchaseOrderLineList) {
            if(StringUtils.isNotBlank(purchaseOrderLine.getId())){purchaseOrderLineMapper.updateById(purchaseOrderLine);}else {
                purchaseOrderLine.setPurchaseOrderId(purchaseOrder.getId());
                purchaseOrderLineMapper.insert(purchaseOrderLine);
            }
        }
    }

    @Override
    public void delPurchaseOrder(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void delBatchPurchaseOrder(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public IPage listPage(Page<PurchaseOrderVO> purchaseOrderPage, QueryWrapper<PurchaseOrderVO> purchaseOrderQueryWrapper, String itemCode, String itemName) {
        return baseMapper.listPage(purchaseOrderPage, purchaseOrderQueryWrapper, itemCode, itemName);
    }

    @Override
    public PurchaseOrderDetailVO queryDetailById(String orderId) {
        PurchaseOrder purchaseOrder = baseMapper.selectById(orderId);
        QueryWrapper<PurchaseOrderLine> purchaseOrderLineQueryWrapper = new QueryWrapper<>();
        purchaseOrderLineQueryWrapper.eq("purchase_order_id", orderId);
        List<PurchaseOrderLine> purchaseOrderLineList = purchaseOrderLineMapper.selectList(purchaseOrderLineQueryWrapper);
        PurchaseOrderDetailVO purchaseOrderDetailVO = new PurchaseOrderDetailVO();
        BeanUtils.copyProperties(purchaseOrder, purchaseOrderDetailVO);
        purchaseOrderDetailVO.setPurchaseOrderLineList(purchaseOrderLineList);
        return purchaseOrderDetailVO;
    }

    @Override
    public void updateStatus(String status, List<String> idList) {
        for (String id : idList) {
            PurchaseOrder updateHead = new PurchaseOrder();
            updateHead.setId(id);
            updateHead.setStatus(status);
            baseMapper.updateById(updateHead);
        }
    }
}
