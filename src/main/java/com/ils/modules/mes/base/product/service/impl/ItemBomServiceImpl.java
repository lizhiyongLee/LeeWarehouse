package com.ils.modules.mes.base.product.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ils.modules.mes.base.product.entity.*;
import com.ils.modules.mes.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.product.mapper.ItemBomDetailMapper;
import com.ils.modules.mes.base.product.mapper.ItemBomMapper;
import com.ils.modules.mes.base.product.mapper.ItemBomSubstituteMapper;
import com.ils.modules.mes.base.product.service.ItemBomService;
import com.ils.modules.mes.base.product.vo.ItemBomDetailVO;
import com.ils.modules.mes.base.product.vo.ItemBomSubstituteVO;
import com.ils.modules.mes.base.product.vo.ItemBomVO;

/**
 * @Description: 物料BOM
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
@Service
public class ItemBomServiceImpl extends ServiceImpl<ItemBomMapper, ItemBom> implements ItemBomService {

    @Autowired
    private ItemBomMapper itemBomMapper;
    @Autowired
    private ItemBomDetailMapper itemBomDetailMapper;

    @Autowired
    private ItemBomSubstituteMapper itemBomSubstituteMapper;

    @Autowired
    private ItemService itemService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ItemBom saveMain(ItemBomVO itemBomVO) {

        this.checkCondition(itemBomVO);

        ItemBom itemBom = new ItemBom();
        BeanUtils.copyProperties(itemBomVO, itemBom);
        itemBomMapper.insert(itemBom);
        List<ItemBomDetailVO> itemBomDetailList = itemBomVO.getItemBomDetailList();
        for (ItemBomDetailVO entity : itemBomDetailList) {
            //外键设置
            entity.setBomId(itemBom.getId());
            itemBomDetailMapper.insert(entity);
            List<ItemBomSubstituteVO> itemBomSubstituteList = entity.getItemBomSubstituteList();
            if (null != itemBomSubstituteList) {
                for (ItemBomSubstitute substitute : itemBomSubstituteList) {
                    substitute.setBomId(itemBom.getId());
                    itemBomSubstituteMapper.insert(substitute);
                }
            }
        }

        return itemBom;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMain(ItemBomVO itemBomVO) {

        this.checkCondition(itemBomVO);

        ItemBom itemBom = new ItemBom();
        BeanUtils.copyProperties(itemBomVO, itemBom);
        itemBomMapper.updateById(itemBom);


        //1.先删除子表数据
        itemBomDetailMapper.deleteByMainId(itemBom.getId());
        itemBomSubstituteMapper.deleteByMainId(itemBom.getId());

        //2.子表数据重新插入
        List<ItemBomDetailVO> itemBomDetailList = itemBomVO.getItemBomDetailList();
        for (ItemBomDetailVO entity : itemBomDetailList) {
            // 外键设置
            entity.setBomId(itemBom.getId());
            itemBomDetailMapper.insert(entity);
            List<ItemBomSubstituteVO> itemBomSubstituteList = entity.getItemBomSubstituteList();
            if (null != itemBomSubstituteList) {
                for (ItemBomSubstitute substitute : itemBomSubstituteList) {
                    substitute.setBomId(itemBom.getId());
                    itemBomSubstituteMapper.insert(substitute);
                }
            }

        }
    }


    /**
     * 条件校验
     *
     * @return
     * @date 2020年11月11日
     */
    private boolean checkCondition(ItemBomVO itemBomVO) {

        // 检验编码版本
        QueryWrapper<ItemBom> queryWrapper = new QueryWrapper<ItemBom>();
        queryWrapper.eq("version", itemBomVO.getVersion());
        queryWrapper.eq("item_id", itemBomVO.getItemId());
        if (StringUtils.isNoneBlank(itemBomVO.getId())) {
            queryWrapper.ne("id", itemBomVO.getId());
        }
        ItemBom obj = baseMapper.selectOne(queryWrapper);
        if (obj != null) {
            throw new ILSBootException("P-OW-0006");
        }
        //明细行必须至少添加一种物料
        List<ItemBomDetailVO> itemBomDetailList = itemBomVO.getItemBomDetailList();
        if (itemBomDetailList.isEmpty()) {
            throw new ILSBootException("P-AU-0072");
        }
        // 加入选择的主单位，不在物料定义的转换单位中，抛出异常
        String mianUnit = itemBomVO.getUnit();
        List<Unit> listUnit = itemService.queryItemUnitListByMainId(itemBomVO.getItemId());
        long iCount = listUnit.stream().filter(unit -> unit.getId().equals(mianUnit)).count();
        if (iCount == 0) {
            throw new ILSBootException("B-FCT-0007");
        }

        iCount = itemBomVO.getItemBomDetailList().stream().filter(bom -> itemBomVO.getItemId().equals(bom.getItemId()))
                .count();

        if (iCount > 0) {
            throw new ILSBootException("B-FCT-0008");
        }

        //检查循环依赖
        List<String> itemCodeList = new ArrayList<>();
        List<String> exitItemCodeList = new ArrayList<>();
        exitItemCodeList.add(itemBomVO.getItemCode());
        itemBomVO.getItemBomDetailList().forEach(itemBomDetailVO -> {
            QueryWrapper<ItemBomDetail> itemBomDetailQueryWrapper = new QueryWrapper<>();
            queryWrapper.eq("bom_id", itemBomDetailVO.getId());
            List<ItemBomDetail> lstItemBomDetail = itemBomDetailMapper.selectList(itemBomDetailQueryWrapper);
            if (!CommonUtil.isEmptyOrNull(lstItemBomDetail)) {
                itemCodeList.add(itemBomDetailVO.getItemCode());
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
                QueryWrapper<ItemBom> itemBomQueryWrapper = new QueryWrapper<>();
                itemBomQueryWrapper.eq("item_code", itemCode);
                List<ItemBom> itemBomList = itemBomMapper.selectList(itemBomQueryWrapper);
                if (!CommonUtil.isEmptyOrNull(itemBomList)) {
                    exitItemCodeList.add(itemCode);
                    List<String> subItemCodeList = new ArrayList<>();
                    itemBomList.forEach(itemBom -> {
                        QueryWrapper<ItemBomDetail> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("bom_id", itemBom.getId());
                        List<ItemBomDetail> lstItemBomDetail = itemBomDetailMapper.selectList(queryWrapper);
                        if (!CommonUtil.isEmptyOrNull(lstItemBomDetail)) {
                            lstItemBomDetail.forEach(detail -> subItemCodeList.add(detail.getItemCode()));
                        }
                    });
                    if (!CommonUtil.isEmptyOrNull(subItemCodeList)) {
                        this.checkCircularDependency(exitItemCodeList, subItemCodeList);
                    }
                }
            }
        });
    }


    @Override
    public List<ItemBom> selectByItemIdAndOrderByUpdateTime(String itemId) {
        return itemBomMapper.selectByItemIdAndOrderByUpdateTime(itemId);
    }

}
