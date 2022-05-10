package com.ils.modules.mes.produce.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.produce.entity.SaleOrderLine;
import com.ils.modules.mes.produce.vo.SaleOrderLineVO;

/**
 * @Description: 销售订单物料行
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
public interface SaleOrderLineMapper extends ILSMapper<SaleOrderLine> {
    
    /**
     * 通过主表 Id 删除
     * @param mainId
     * @return
     */
	public boolean deleteByMainId(String mainId);
    
    /**
     * 通过主表 Id 查询
     * @param mainId
     * @return
     */
	public List<SaleOrderLine> selectByMainId(String mainId);

    /**
     * 
     * 分页查询订单行数据
     * 
     * @param page
     * @param queryWrapper
     * @return
     * @date 2021年1月22日
     */
    public IPage<SaleOrderLineVO> saleOrderLinePage(Page<SaleOrderLineVO> page,
        @Param("ew") QueryWrapper<SaleOrderLineVO> queryWrapper);

    /**
     * 
     * 订单行减少工单下单数量
     * 
     * @param planQty
     * @param id
     * @date 2021年1月25日
     */
    public void updateDecreaseSaleOrderLinePlanQty(@Param("planQty") BigDecimal planQty, @Param("id") String id);

    /**
     * 
     * 订单行减少工单下单数量
     * 
     * @param planQty
     * @param id
     * @date 2021年1月25日
     */
    public void updateAddSaleOrderLinePlanQty(@Param("planQty") BigDecimal planQty, @Param("id") String id);


}
