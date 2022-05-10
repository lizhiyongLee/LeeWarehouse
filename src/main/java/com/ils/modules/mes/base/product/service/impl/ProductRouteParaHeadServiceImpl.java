package com.ils.modules.mes.base.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.product.entity.ProductRouteParaDetail;
import com.ils.modules.mes.base.product.entity.ProductRouteParaHead;
import com.ils.modules.mes.base.product.mapper.ProductRouteParaDetailMapper;
import com.ils.modules.mes.base.product.mapper.ProductRouteParaHeadMapper;
import com.ils.modules.mes.base.product.service.ProductRouteParaHeadService;
import com.ils.modules.mes.base.product.vo.ProductRouteParaVO;
import com.ils.modules.mes.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/18 16:34
 */
@Service
public class ProductRouteParaHeadServiceImpl extends ServiceImpl<ProductRouteParaHeadMapper, ProductRouteParaHead> implements ProductRouteParaHeadService {
    @Autowired
    private ProductRouteParaDetailMapper productRouteParaDetailMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveProductRoutePara(ProductRouteParaVO productRouteParaVO) {
        //保存主表
        baseMapper.insert(productRouteParaVO);
        List<ProductRouteParaDetail> productRouteParaDetailList = productRouteParaVO.getProductRouteParaDetailList();
        //保存从表
        if (!CommonUtil.isEmptyOrNull(productRouteParaDetailList)) {
            productRouteParaDetailList.forEach(productRouteParaDetail -> {
                productRouteParaDetail.setParaHeadId(productRouteParaVO.getId());
                productRouteParaDetail.setId(null);
                productRouteParaDetail.setCreateBy(null);
                productRouteParaDetail.setCreateTime(null);
                productRouteParaDetailMapper.insert(productRouteParaDetail);
            });
        }
    }

    @Override
    public void updateProductRoutePara(ProductRouteParaVO productRouteParaVO) {
        baseMapper.updateById(productRouteParaVO);
        //删除从表
        productRouteParaDetailMapper.deleteByMainId(productRouteParaVO.getId());

        //重新插入从表
        List<ProductRouteParaDetail> productRouteParaDetailList = productRouteParaVO.getProductRouteParaDetailList();
        productRouteParaDetailList.forEach(productRouteParaDetail -> productRouteParaDetailMapper.insert(productRouteParaDetail));
    }

    @Override
    public ProductRouteParaVO queryById(String id) {
        ProductRouteParaVO productRouteParaVO = new ProductRouteParaVO();
        //查询主表
        ProductRouteParaHead productRouteParaHead = baseMapper.selectById(id);
        BeanUtils.copyProperties(productRouteParaHead, productRouteParaVO);
        //查询从表
        QueryWrapper<ProductRouteParaDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("para_head_id", id);
        List<ProductRouteParaDetail> productRouteParaDetailList = productRouteParaDetailMapper.selectList(queryWrapper);

        productRouteParaVO.setProductRouteParaDetailList(productRouteParaDetailList);
        return productRouteParaVO;
    }

    @Override
    public List<ProductRouteParaVO> queryByProductId(String productId) {
        List<ProductRouteParaVO> productRouteParaVOList = new ArrayList<>();
        //查询主表
        QueryWrapper<ProductRouteParaHead> headQueryWrapper = new QueryWrapper<>();
        headQueryWrapper.eq("product_id", productId);
        List<ProductRouteParaHead> productRouteParaHeadList = baseMapper.selectList(headQueryWrapper);
        productRouteParaHeadList.forEach(productRouteParaHead -> {
            ProductRouteParaVO productRouteParaVO = new ProductRouteParaVO();
            BeanUtils.copyProperties(productRouteParaHead, productRouteParaVO);
            //查询从表
            QueryWrapper<ProductRouteParaDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.eq("para_head_id", productRouteParaHead.getId());
            List<ProductRouteParaDetail> productRouteParaDetailList = productRouteParaDetailMapper.selectList(detailQueryWrapper);

            productRouteParaVO.setProductRouteParaDetailList(productRouteParaDetailList);
            productRouteParaVOList.add(productRouteParaVO);
        });
        return productRouteParaVOList;
    }

    @Override
    public void deleteByProductId(String productId) {
        List<String> ids = new ArrayList<>();
        //查询主表
        QueryWrapper<ProductRouteParaHead> headQueryWrapper = new QueryWrapper<>();
        headQueryWrapper.eq("product_id", productId);
        List<ProductRouteParaHead> productRouteParaHeadList = baseMapper.selectList(headQueryWrapper);
        //删除主从表
        productRouteParaHeadList.forEach(productRouteParaHead -> ids.add(productRouteParaHead.getId()));
        baseMapper.deleteByMainId(productId);
        if (!CommonUtil.isEmptyOrNull(ids)) {
            ids.forEach(id -> productRouteParaDetailMapper.selectByMainId(id));
        }
    }
}
