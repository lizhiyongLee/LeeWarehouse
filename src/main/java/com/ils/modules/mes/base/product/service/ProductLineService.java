package com.ils.modules.mes.base.product.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.product.entity.ProductLine;
import com.ils.modules.mes.base.product.vo.ProductLineVO;

/**
 * @Description: 产品工艺路线明细
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
public interface ProductLineService extends IService<ProductLine> {

    /**
     * 通过父ID查询列表
     * 
     * @param productId
     * @return
     */
    public List<ProductLineVO> selectByProductId(String productId);
}
