package com.ils.modules.mes.produce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.produce.entity.PurchaseOrderLine;
import com.ils.modules.mes.produce.mapper.PurchaseOrderLineMapper;
import com.ils.modules.mes.produce.service.PurchaseOrderLineService;
import com.ils.modules.mes.produce.vo.PurchaseOrderLinePageVO;
import com.ils.modules.mes.util.CommonUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 采购清单物料行
 * @Author: Conner
 * @Date:   2021-01-28
 * @Version: V1.0
 */
@Service
public class PurchaseOrderLineServiceImpl extends ServiceImpl<PurchaseOrderLineMapper, PurchaseOrderLine> implements PurchaseOrderLineService {

    @Override
    public void savePurchaseOrderLine(PurchaseOrderLine purchaseOrderLine) {
         baseMapper.insert(purchaseOrderLine);
    }

    @Override
    public void updatePurchaseOrderLine(PurchaseOrderLine purchaseOrderLine) {
        baseMapper.updateById(purchaseOrderLine);
    }

    @Override
    public void delPurchaseOrderLine(String id) {
        baseMapper.deleteById(id);
    }



    @Override
    public void delBatchPurchaseOrderLine(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public List<PurchaseOrderLine> queryByMainId(String mainId) {
        QueryWrapper<PurchaseOrderLine> purchaseOrderLineQueryWrapper = new QueryWrapper<>();
        purchaseOrderLineQueryWrapper.eq("purchase_order_id",mainId);
        purchaseOrderLineQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<PurchaseOrderLine> purchaseOrderLineList = baseMapper.selectList(purchaseOrderLineQueryWrapper);
        if (CommonUtil.isEmptyOrNull(purchaseOrderLineList)){
            return new ArrayList<PurchaseOrderLine>(0);
        }
        return purchaseOrderLineList;
    }

    @Override
    public IPage<PurchaseOrderLinePageVO> listPage(Page<PurchaseOrderLinePageVO> page, QueryWrapper<PurchaseOrderLinePageVO> queryWrapper) {
        return baseMapper.listPage(page,queryWrapper);
    }
}
