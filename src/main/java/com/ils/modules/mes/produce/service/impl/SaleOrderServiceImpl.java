package com.ils.modules.mes.produce.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ils.modules.mes.enums.SaleOrderStatusEnum;
import com.ils.modules.mes.produce.vo.SaleOrderVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.util.BigDecimalUtils;
import com.ils.modules.mes.base.factory.entity.Customer;
import com.ils.modules.mes.base.factory.service.CustomerService;
import com.ils.modules.mes.base.material.service.ItemUnitService;
import com.ils.modules.mes.base.material.vo.ConvertUnitVO;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.produce.entity.SaleOrder;
import com.ils.modules.mes.produce.entity.SaleOrderLine;
import com.ils.modules.mes.produce.entity.WorkOrderRelateSale;
import com.ils.modules.mes.produce.mapper.SaleOrderLineMapper;
import com.ils.modules.mes.produce.mapper.SaleOrderMapper;
import com.ils.modules.mes.produce.mapper.WorkOrderRelateSaleMapper;
import com.ils.modules.mes.produce.service.SaleOrderService;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.service.CodeGeneratorService;

/**
 * @Description: 销售订单
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
@Service
public class SaleOrderServiceImpl extends ServiceImpl<SaleOrderMapper, SaleOrder> implements SaleOrderService {

    @Autowired
    private SaleOrderMapper saleOrderMapper;
    @Autowired
    private SaleOrderLineMapper saleOrderLineMapper;

    @Autowired
    private WorkOrderRelateSaleMapper workOrderRelateSaleMapper;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ItemUnitService itemUnitService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMain(SaleOrder saleOrder, List<SaleOrderLine> saleOrderLineList) {
        if (StringUtils.isBlank(saleOrder.getSaleOrderNo())) {
            // 生成单号
            String saleOrderNo = codeGeneratorService.getNextCode(CommonUtil.getTenantId(),
                    MesCommonConstant.SALE_ORDER_NO, saleOrder);
            saleOrder.setSaleOrderNo(saleOrderNo);
        }

        Customer customer = customerService.getById(saleOrder.getCustomerId());
        saleOrder.setCustomerName(customer.getCustomerName());
        saleOrderMapper.insert(saleOrder);
        //子表数据插入
        saleOrderLineInsert(saleOrder, saleOrderLineList);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMain(SaleOrder saleOrder, List<SaleOrderLine> saleOrderLineList) {

        this.checkCondition(saleOrder);
        Customer customer = customerService.getById(saleOrder.getCustomerId());
        saleOrder.setCustomerName(customer.getCustomerName());
        saleOrderMapper.updateById(saleOrder);

        //1.先删除子表数据
        saleOrderLineMapper.deleteByMainId(saleOrder.getId());

        //2.子表数据重新插入
        saleOrderLineInsert(saleOrder, saleOrderLineList);
    }

    private void saleOrderLineInsert(SaleOrder saleOrder, List<SaleOrderLine> saleOrderLineList) {
        if (!CommonUtil.isEmptyOrNull(saleOrderLineList)) {
            this.calMainQty(saleOrderLineList);
            List<SaleOrderLine> lstSaleOrderLineVO = new ArrayList<SaleOrderLine>(saleOrderLineList.size());
            for (SaleOrderLine entity : saleOrderLineList) {
                //外键设置
                entity.setSaleOrderId(saleOrder.getId());
                entity.setSaleOrderNo(saleOrder.getSaleOrderNo());
                entity.setCompleteQty(BigDecimal.ZERO);
                entity.setPlanQty(BigDecimal.ZERO);
                entity.setReturnQty(BigDecimal.ZERO);
                entity.setSaleOrderCompleteQty(BigDecimal.ZERO);
                entity.setCompleteMainQty(BigDecimal.ZERO);
                entity.setPlanMainQty(BigDecimal.ZERO);
                entity.setReturnMainQty(BigDecimal.ZERO);
                entity.setSaleOrderCompleteMainQty(BigDecimal.ZERO);
                entity.setStatus(saleOrder.getStatus());
                CommonUtil.setSysParam(entity, saleOrder);
                lstSaleOrderLineVO.add(entity);
            }
            saleOrderLineMapper.insertBatchSomeColumn(lstSaleOrderLineVO);
        }
    }

    /**
     * 订单保存条件校验
     *
     * @param saleOrder
     * @date 2021年1月20日
     */
    private void checkCondition(SaleOrder saleOrder) {
        if (StringUtils.isNotBlank(saleOrder.getId())) {
            QueryWrapper<WorkOrderRelateSale> queryWrapper = new QueryWrapper();
            queryWrapper.eq("sale_order_id", saleOrder.getId());
            int count = workOrderRelateSaleMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new ILSBootException("P-OW-0063");
            }
        }
    }

    private void calMainQty(List<SaleOrderLine> saleOrderLineList) {
        for (SaleOrderLine saleOrderLine : saleOrderLineList) {
            ConvertUnitVO convertUnit =
                    itemUnitService.selectConvertUnit(saleOrderLine.getUnit(), saleOrderLine.getItemId());
            saleOrderLine.setMainUnit(convertUnit.getMainUnit());
            BigDecimal mainQty = BigDecimalUtils.divide(
                    BigDecimalUtils.multiply(saleOrderLine.getSaleOrderQty(), convertUnit.getMainUnitQty()),
                    convertUnit.getConvertQty(), 6);
            saleOrderLine.setSaleOrderMainQty(mainQty);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMain(String id) {
        saleOrderLineMapper.deleteByMainId(id);
        saleOrderMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMain(List<String> idList) {
        for (Serializable id : idList) {
            saleOrderLineMapper.deleteByMainId(id.toString());
            saleOrderMapper.deleteById(id);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateStatus(String status , List<String> idList){
        for (String id : idList) {
            SaleOrder saleOrder = saleOrderMapper.selectById(id);
            //订单结束
            saleOrder.setStatus(status);
            //订单行结束
            List<SaleOrderLine> saleOrderLineList = saleOrderLineMapper.selectByMainId(id);
            saleOrderLineList.forEach(saleOrderLine -> saleOrderLine.setStatus(SaleOrderStatusEnum.FINISH.getValue()));
            //更新主表
            Customer customer = customerService.getById(saleOrder.getCustomerId());
            saleOrder.setCustomerName(customer.getCustomerName());
            saleOrderMapper.updateById(saleOrder);
            //更新子表-先删除子表数据
            saleOrderLineMapper.deleteByMainId(saleOrder.getId());
            //更新子表-子表数据重新插入
            saleOrderLineInsert(saleOrder, saleOrderLineList);
        }
    }
}
