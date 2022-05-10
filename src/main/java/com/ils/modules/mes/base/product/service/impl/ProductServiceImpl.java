package com.ils.modules.mes.base.product.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.ils.modules.mes.base.craft.entity.Route;
import com.ils.modules.mes.base.craft.mapper.RouteMapper;
import com.ils.modules.mes.base.product.entity.*;
import com.ils.modules.mes.base.product.service.ProductRouteParaHeadService;
import com.ils.modules.mes.base.product.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.util.BigDecimalUtils;
import com.ils.modules.mes.base.material.entity.ItemUnit;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.product.mapper.ItemBomDetailMapper;
import com.ils.modules.mes.base.product.mapper.ProductBomMapper;
import com.ils.modules.mes.base.product.mapper.ProductBomSubstituteMapper;
import com.ils.modules.mes.base.product.mapper.ProductLineMapper;
import com.ils.modules.mes.base.product.mapper.ProductMapper;
import com.ils.modules.mes.base.product.mapper.ProductRouteMethodMapper;
import com.ils.modules.mes.base.product.mapper.ProductRouteStationMapper;
import com.ils.modules.mes.base.product.service.ItemBomService;
import com.ils.modules.mes.base.product.service.ProductService;
import com.ils.modules.mes.util.CommonUtil;

/**
 * @Description: 产品
 * @Author: fengyi
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private ProductLineMapper productLineMapper;
    @Autowired
    private ProductBomMapper productBomMapper;
    @Autowired
    private ProductRouteStationMapper productRouteStationMapper;
    @Autowired
    private ProductRouteMethodMapper productRouteMethodMapper;
    @Autowired
    private ProductRouteParaHeadService productRouteParaHeadService;
    @Autowired
    private ProductBomSubstituteMapper productBomSubstituteMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemBomService itemBomService;

    @Autowired
    private ItemBomDetailMapper itemBomDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product saveMain(ProductVO productVO) {
        // 数据校验
        this.checkCondition(productVO);

        Product product = new Product();
        BeanUtils.copyProperties(productVO, product);
        productMapper.insert(product);

        // 保存明细
        this.saveDetail(productVO, product);

        return product;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMain(ProductVO productVO) {

        // 数据校验
        this.checkCondition(productVO);

        Product product = new Product();
        BeanUtils.copyProperties(productVO, product);
        productMapper.updateById(product);

        // 1.先删除子表数据
        productLineMapper.deleteByProductId(product.getId());
        productBomMapper.deleteByProductId(product.getId());
        productRouteStationMapper.deleteByProductId(product.getId());
        productRouteMethodMapper.deleteByProductId(product.getId());
        productRouteParaHeadService.deleteByProductId(product.getId());
        productBomSubstituteMapper.deleteByProductId(product.getId());

        // 保存明细
        this.saveDetail(productVO, product);

    }

    /**
     * 更新或者保存明细数据
     *
     * @param productVO
     * @param product
     * @date 2020年11月16日
     */
    private void saveDetail(ProductVO productVO, Product product) {

        List<ProductLineVO> productLineList = productVO.getProductLineList();
        if (productLineList == null || productLineList.size() == 0) {
            return;
        }
        // 接收批量新增VO list
        List<ProductRouteStation> lstProductRouteStationVO = new ArrayList<ProductRouteStation>(16);
        List<ProductRouteMethod> lstProductRouteMethodVO = new ArrayList<ProductRouteMethod>(16);
        List<ProductBomSubstitute> lstProductBomSubstituteVO = new ArrayList<ProductBomSubstitute>(16);

        for (ProductLineVO productLineVO : productLineList) {
            // 工艺路线工序设置
            productLineVO.setProductId(product.getId());
            productLineVO.setRouteId(product.getRouteId());
            productLineVO.setId(null);
            productLineMapper.insert(productLineVO);

            // 产品路线工位
            List<ProductRouteStationVO> productRouteStationList = productLineVO.getProductRouteStationList();
            if (productRouteStationList != null) {
                for (ProductRouteStationVO productRouteStationVO : productRouteStationList) {
                    // 外键设置
                    productRouteStationVO.setId(null);
                    productRouteStationVO.setProductId(product.getId());
                    productRouteStationVO.setProductLineId(productLineVO.getId());
                    CommonUtil.setSysParam(productRouteStationVO, product);
                    lstProductRouteStationVO.add(productRouteStationVO);
                }
            }


            // 产品质检方案
            List<ProductRouteMethodVO> productRouteMethodList = productLineVO.getProductRouteMethodList();
            if (productRouteMethodList != null) {
                for (ProductRouteMethodVO productRouteMethodVO : productRouteMethodList) {
                    // 外键设置
                    productRouteMethodVO.setId(null);
                    productRouteMethodVO.setProductId(product.getId());
                    productRouteMethodVO.setProductLineId(productLineVO.getId());
                    CommonUtil.setSysParam(productRouteMethodVO, product);
                    lstProductRouteMethodVO.add(productRouteMethodVO);
                }

            }
            // 参数模板
            List<ProductRouteParaVO> productRouteParaVOList = productLineVO.getProductRouteParaVOList();
            if (!CommonUtil.isEmptyOrNull(productRouteParaVOList)) {
                productRouteParaVOList.forEach(productRouteParaVO -> {
                    productRouteParaVO.setProductId(product.getId());
                    productRouteParaVO.setProductLineId(productLineVO.getId());
                    productRouteParaVO.setId(null);
                    productRouteParaVO.setCreateBy(null);
                    productRouteParaVO.setCreateTime(null);
                    productRouteParaHeadService.saveProductRoutePara(productRouteParaVO);
                });
            }

            // 物料 BOM设置
            List<ProductBomVO> productBomList = productLineVO.getProductBomList();
            if (productBomList != null) {
                for (ProductBomVO productBomVO : productBomList) {
                    // 外键设置
                    productBomVO.setProductId(product.getId());
                    productBomVO.setProductLineId(productLineVO.getId());
                    productBomMapper.insert(productBomVO);

                    // 物料替代物设置
                    List<ProductBomSubstituteVO> productBomSubstituteList = productBomVO.getItemBomSubstituteList();
                    if (productBomSubstituteList != null) {
                        for (ProductBomSubstituteVO productBomSubstituteVO : productBomSubstituteList) {
                            // 外键设置
                            productBomSubstituteVO.setId(null);
                            productBomSubstituteVO.setProductId(product.getId());
                            productBomSubstituteVO.setProductBomId(productBomVO.getId());
                            CommonUtil.setSysParam(productBomSubstituteVO, product);
                            lstProductBomSubstituteVO.add(productBomSubstituteVO);
                        }
                    }
                }
            }
        }

        // 批量新增
        if (!CommonUtil.isEmptyOrNull(lstProductRouteStationVO)) {
            productRouteStationMapper.insertBatchSomeColumn(lstProductRouteStationVO);
        }
        if (!CommonUtil.isEmptyOrNull(lstProductRouteMethodVO)) {
            productRouteMethodMapper.insertBatchSomeColumn(lstProductRouteMethodVO);
        }
        if (!CommonUtil.isEmptyOrNull(lstProductBomSubstituteVO)) {
            productBomSubstituteMapper.insertBatchSomeColumn(lstProductBomSubstituteVO);
        }
    }

    /**
     * 保存或者更新时检验数据是否合规
     *
     * @param productVO
     * @return true 数据合规
     * @date 2020年11月16日
     */
    private boolean checkCondition(ProductVO productVO) {
        // 检验编码版本
        QueryWrapper<Product> queryWrapper = new QueryWrapper<Product>();
        queryWrapper.eq("version", productVO.getVersion());
        queryWrapper.eq("item_id", productVO.getItemId());
        if (StringUtils.isNoneBlank(productVO.getId())) {
            queryWrapper.ne("id", productVO.getId());
        }
        Product obj = baseMapper.selectOne(queryWrapper);
        if (obj != null) {
            throw new ILSBootException("P-OW-0006");
        }

        //itemId物料定义中的ID
        String itemId = productVO.getItemId();
        String unit = productVO.getUnit();

        // 查询物料定义中的转换单位
        List<ItemUnit> lstItemUnit = itemService.queryItemUnitByItemId(itemId);
        List<String> units =
                lstItemUnit.stream().map(itemUnit -> itemUnit.getConvertUnit()).collect(Collectors.toList());

        // 如果产品 BOM 单位为空或者不是物料定义里面的转换单位
        if (StringUtils.isBlank(unit) || !units.contains(unit)) {
            throw new ILSBootException("P-OW-0001 ");
        }

        // 校验物料 BOM 为物料定义下的版本
        String itemBomId = productVO.getItemBomId();
        if (StringUtils.isNotBlank(itemBomId)) {
            ItemBom itemBom = itemBomService.getById(itemBomId);
            if (itemBom == null || !itemId.equals(itemBom.getItemId())) {
                throw new ILSBootException("P-OW-0003");
            }
            // 校验各个工序下的物料总和不能超过物料清单中的数量
            this.checkItemBomQty(productVO, itemBomId);

        }

        //检查循环依赖
        List<String> itemCodeList = new ArrayList<>();
        List<String> exitItemCodeList = new ArrayList<>();
        exitItemCodeList.add(productVO.getItemCode());
        productVO.getProductLineList().forEach(productLineVO -> {
            List<ProductBomVO> productBomList = productLineVO.getProductBomList();
            if (!CommonUtil.isEmptyOrNull(productBomList)) {
                productBomList.forEach(productBomVO -> {
                    QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
                    productQueryWrapper.eq("item_code", productBomVO.getItemCode());
                    List<Product> productList = productMapper.selectList(productQueryWrapper);
                    if (!CommonUtil.isEmptyOrNull(productList)) {
                        itemCodeList.add(productBomVO.getItemCode());
                    }
                });
            }
        });
        this.checkCircularDependency(exitItemCodeList, itemCodeList);

        return true;
    }

    /**
     * 检查循环依赖
     *
     * @param exitItemCodeList
     * @param itemCodeList
     */
    private void checkCircularDependency(List<String> exitItemCodeList, List<String> itemCodeList) {
        itemCodeList.forEach(itemCode -> {
            if (exitItemCodeList.contains(itemCode)) {
                throw new ILSBootException("P-OW-0053");
            } else {
                QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
                productQueryWrapper.eq("item_code", itemCode);
                List<Product> productList = productMapper.selectList(productQueryWrapper);
                if (!CommonUtil.isEmptyOrNull(productList)) {
                    exitItemCodeList.add(itemCode);
                    List<String> subItemCodeList = new ArrayList<>();
                    productList.forEach(product -> {
                        QueryWrapper<ProductBom> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("product_id", product.getId());
                        List<ProductBom> productBomList = productBomMapper.selectList(queryWrapper);
                        if (!CommonUtil.isEmptyOrNull(productBomList)) {
                            productBomList.forEach(detail -> subItemCodeList.add(detail.getItemCode()));
                        }
                    });
                    if (!CommonUtil.isEmptyOrNull(subItemCodeList)) {
                        this.checkCircularDependency(exitItemCodeList, subItemCodeList);
                    }
                }
            }
        });
    }


    /**
     * 检查BOM物料时总量超过物料清单中物料的总量
     *
     * @param productVO
     * @date 2020年12月25日
     */
    private void checkItemBomQty(ProductVO productVO, String itemBomId) {
        List<ProductBomVO> lstProductBomVO = new ArrayList<ProductBomVO>(16);
        List<ProductLineVO> lstProductLineVO = productVO.getProductLineList();
        for (ProductLineVO productLineVO : lstProductLineVO) {
            if (!CommonUtil.isEmptyOrNull(productLineVO.getProductBomList())) {
                lstProductBomVO.addAll(productLineVO.getProductBomList());
            }
        }
        if (!CommonUtil.isEmptyOrNull(lstProductBomVO)) {
            // 根据物料汇总所有的数量
            Map<String, BigDecimal> productBomMap =
                    lstProductBomVO.stream().collect(Collectors.groupingBy(ProductBomVO::getItemId,
                            Collectors.reducing(BigDecimal.ZERO, ProductBomVO::getQty, BigDecimal::add)));

            // 查询物料清单中数量
            QueryWrapper<ItemBomDetail> queryWrapper = new QueryWrapper<ItemBomDetail>();
            queryWrapper.eq("bom_id", itemBomId);
            List<ItemBomDetail> lstItemBomDetail = itemBomDetailMapper.selectList(queryWrapper);
            Map<String, ItemBomDetail> itemBomMap = lstItemBomDetail.stream()
                    .collect(Collectors.toMap(ItemBomDetail::getItemId, itemBomDetail -> itemBomDetail));

            // 判断每一种物料是否超出
            for (Entry<String, BigDecimal> entry : productBomMap.entrySet()) {
                ItemBomDetail itemBomDetail = itemBomMap.get(entry.getKey());
                if (itemBomDetail != null) {
                    BigDecimal itemTotalQty = entry.getValue();
                    boolean isGreater = BigDecimalUtils.greaterThan(itemTotalQty, itemBomDetail.getQty());
                    if (isGreater) {
                        throw new ILSBootException("P-OW-0045", itemBomDetail.getItemCode());
                    }
                }
            }
        }

    }

    @Override
    public ProductVO queryById(String id) {

        ProductVO productVO = new ProductVO();
        Product product = this.getById(id);
        String itemBomId = product.getItemBomId();
        if (StringUtils.isNoneBlank(itemBomId)) {
            productVO.setItemBomVersion(itemBomService.getById(itemBomId).getVersion());
        }
        BeanUtils.copyProperties(product, productVO);
        if (StringUtils.isNotEmpty(product.getRouteId())){
            Route route = routeMapper.selectById(product.getRouteId());
            productVO.setRouteName(route.getRouteName());
            productVO.setRouteCode(route.getRouteCode());
        }
        List<ProductLineVO> productLineList = productLineMapper.selectByProductId(id);
        productVO.setProductLineList(productLineList);

        // 按产品路线分组获取工位
        List<ProductRouteStationVO> productRouteStationList = productRouteStationMapper.selectByProductId(id);
        Map<String, List<ProductRouteStationVO>> productRouteStationMap =
                productRouteStationList.stream().collect(Collectors.groupingBy(ProductRouteStationVO::getProductLineId));

        // 按产品路线分组获取质检方案
        List<ProductRouteMethodVO> productRouteMethodList = productRouteMethodMapper.selectByProductId(id);
        Map<String, List<ProductRouteMethodVO>> productRouteMethodMap =
                productRouteMethodList.stream().collect(Collectors.groupingBy(ProductRouteMethodVO::getProductLineId));

        // 按产品路线分组获取参数
        List<ProductRouteParaVO> productRouteParaVOList = productRouteParaHeadService.queryByProductId(id);
        Map<String, List<ProductRouteParaVO>> productRouteParaMap =
                productRouteParaVOList.stream().collect(Collectors.groupingBy(ProductRouteParaVO::getProductLineId));

        // 按物料 BOM ID分组替代物
        List<ProductBomSubstituteVO> productBomSubstituteList = productBomSubstituteMapper.selectByProductId(id);
        Map<String, List<ProductBomSubstituteVO>> productBomSubstituteMap =
                productBomSubstituteList.stream().collect(Collectors.groupingBy(ProductBomSubstituteVO::getProductBomId));

        List<ProductBomVO> productBomList = productBomMapper.selectByProductId(id);
        for (ProductBomVO productBomVO : productBomList) {
            if (productBomSubstituteMap.containsKey(productBomVO.getId())) {
                productBomVO.setItemBomSubstituteList(productBomSubstituteMap.get(productBomVO.getId()));
            }
        }
        // 按产品路线分组获取物料BOM
        Map<String, List<ProductBomVO>> productBomListMap =
                productBomList.stream().collect(Collectors.groupingBy(ProductBomVO::getProductLineId));

        // 把产品路线下的工位，质检方案对应起来
        for (ProductLineVO productLineVO : productLineList) {
            if (productRouteStationMap.containsKey(productLineVO.getId())) {
                productLineVO.setProductRouteStationList(productRouteStationMap.get(productLineVO.getId()));
            }

            if (productRouteMethodMap.containsKey(productLineVO.getId())) {
                productLineVO.setProductRouteMethodList(productRouteMethodMap.get(productLineVO.getId()));
            }

            if (productBomListMap.containsKey(productLineVO.getId())) {
                productLineVO.setProductBomList(productBomListMap.get(productLineVO.getId()));
            }

            if (productRouteParaMap.containsKey(productLineVO.getId())) {
                productLineVO.setProductRouteParaVOList(productRouteParaMap.get(productLineVO.getId()));
            }
        }

        return productVO;
    }

    @Override
    public IPage<ProductListVO> listPage(Page<ProductListVO> page, QueryWrapper<ProductListVO> queryWrapper) {
        return productMapper.listPage(page, queryWrapper);
    }

    @Override
    public List<ProductLineBomVO> queryProductLineBomById(String id) {
        List<ProductLineBomVO> lstBom = productLineMapper.queryProductLineBomById(id);
        for (ProductLineBomVO itemUnit : lstBom) {
            if (!itemUnit.getConvertUnit().equals(itemUnit.getMainUnit())) {
                BigDecimal mainTotalQty =
                        BigDecimalUtils.divide(BigDecimalUtils.multiply(itemUnit.getQty(), itemUnit.getMainUnitQty()),
                                itemUnit.getConvertQty(), 6);
                itemUnit.setQty(mainTotalQty);
                itemUnit.setUnit(itemUnit.getMainUnit());
            }
        }
        return lstBom;
    }

    @Override
    public List<Product> selectByItemIdAndOrderByUpdateTime(String itemId) {
        return productMapper.selectByItemIdAndOrderByUpdateTime(itemId);
    }
}
