package com.ils.modules.mes.base.product.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.product.entity.ItemBomSubstitute;
import com.ils.modules.mes.base.product.mapper.ItemBomSubstituteMapper;
import com.ils.modules.mes.base.product.service.ItemBomSubstituteService;
import com.ils.modules.mes.base.product.vo.ItemBomSubstituteVO;

/**
 * @Description: 物料BOM替代料
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
@Service
public class ItemBomSubstituteServiceImpl extends ServiceImpl<ItemBomSubstituteMapper, ItemBomSubstitute> implements ItemBomSubstituteService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveItemBomSubstitute(ItemBomSubstitute itemBomSubstitute) {
         baseMapper.insert(itemBomSubstitute);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateItemBomSubstitute(ItemBomSubstitute itemBomSubstitute) {
        baseMapper.updateById(itemBomSubstitute);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delItemBomSubstitute(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchItemBomSubstitute(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public List<ItemBomSubstituteVO> queryBomSubstituteMaterialInfoList(QueryWrapper queryWrapper) {
        return baseMapper.queryBomSubstituteMaterialInfoList(queryWrapper);
    }
}
