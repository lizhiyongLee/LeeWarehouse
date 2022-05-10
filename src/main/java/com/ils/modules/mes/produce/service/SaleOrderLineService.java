package com.ils.modules.mes.produce.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.SaleOrderLine;
import com.ils.modules.mes.produce.vo.SaleOrderLineVO;

/**
 * @Description: 销售订单物料行
 * @Author: fengyi
 * @Date: 2021-01-19
 * @Version: V1.0
 */
public interface SaleOrderLineService extends IService<SaleOrderLine> {

    /**
     * 通过父ID查询列表
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
        QueryWrapper<SaleOrderLineVO> queryWrapper);

}
