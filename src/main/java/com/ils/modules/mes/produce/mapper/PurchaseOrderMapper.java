package com.ils.modules.mes.produce.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.produce.entity.PurchaseOrder;
import com.ils.modules.mes.produce.vo.PurchaseOrderVO;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购清单
 * @Author: Tian
 * @Date: 2021-01-28
 * @Version: V1.0
 */
public interface PurchaseOrderMapper extends ILSMapper<PurchaseOrder> {
    /**
     * 采购清单分页查询
     *
     * @param purchaseOrderPage
     * @param purchaseOrderQueryWrapper
     * @param itemCode
     * @param itemName
     * @return
     */
    public IPage listPage(Page<PurchaseOrderVO> purchaseOrderPage, @Param("ew") QueryWrapper<PurchaseOrderVO> purchaseOrderQueryWrapper, String itemCode, String itemName);

}
