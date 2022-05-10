package com.ils.modules.mes.base.product.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.product.entity.Product;
import com.ils.modules.mes.base.product.vo.ProductListVO;

import java.util.List;

/**
 * @Description: 产品
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
public interface ProductMapper extends ILSMapper<Product> {

    /**
     * 
     * 查询产品 BOM ListVO
     * 
     * @param page
     * @param queryWrapper
     * @return
     * @date 2020年11月6日
     */
    public IPage<ProductListVO> listPage(Page<ProductListVO> page,
        @Param("ew") QueryWrapper<ProductListVO> queryWrapper);

    /**
     *
     * 根据物料id查询
     *
     * @param itemId
     * @return List<Product>
     * @date 2020年11月13日
     */
    public List<Product> selectByItemIdAndOrderByUpdateTime(String itemId);
}
