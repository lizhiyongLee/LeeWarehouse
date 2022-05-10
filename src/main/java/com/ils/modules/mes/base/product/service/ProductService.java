package com.ils.modules.mes.base.product.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.product.entity.Product;
import com.ils.modules.mes.base.product.vo.ProductLineBomVO;
import com.ils.modules.mes.base.product.vo.ProductListVO;
import com.ils.modules.mes.base.product.vo.ProductVO;

/**
 * @Description: 产品
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
public interface ProductService extends IService<Product> {

	/**
     * 
     * 保存产品 BOM
     * 
     * @param productVO
     * @return Product
     * @date 2020年11月5日
     */
    public Product saveMain(ProductVO productVO);
	
    /**
     * 
     * 修改产品 BOM
     * 
     * @param productVO
     * @date 2020年11月5日
     */
    public void updateMain(ProductVO productVO);

    /**
     * 
     * 查询产品VO，相关数据一次返回
     * 
     * @param id
     * @return ProductVO
     * @date 2020年11月5日
     */
    public ProductVO queryById(String id);
	

    /**
     * 
     * 查询产品 BOM ListVO
     * 
     * @param page
     * @param queryWrapper
     * @return
     * @date 2020年11月6日
     */
    public IPage<ProductListVO> listPage(Page<ProductListVO> page, QueryWrapper<ProductListVO> queryWrapper);

    /**
     * 
     * 查询产品 工艺以及物料 BOM
     * 
     * @param id
     * @return ProductLineBomVO
     * @date 2020年11月13日
     */
    public List<ProductLineBomVO> queryProductLineBomById(String id);

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
