package com.ils.modules.mes.base.material.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.material.entity.ItemType;
import com.ils.modules.mes.base.material.mapper.ItemTypeMapper;
import com.ils.modules.mes.base.material.service.ItemTypeService;

/**
 * @Description: 物料类型
 * @Author: fengyi
 * @Date: 2020-10-22
 * @Version: V1.0
 */
@Service
public class ItemTypeServiceImpl extends ServiceImpl<ItemTypeMapper, ItemType> implements ItemTypeService {

    @Override
    public void saveItemType(ItemType itemType) {
        this.checkConditin(itemType);
         baseMapper.insert(itemType);
    }

    @Override
    public void updateItemType(ItemType itemType) {
        this.checkConditin(itemType);
        baseMapper.updateById(itemType);
    }

    private void checkConditin(ItemType itemType) {
        // 编码不重复
        QueryWrapper<ItemType> queryWrapper = new QueryWrapper();
        queryWrapper.eq("type_code", itemType.getTypeCode());
        if (StringUtils.isNoneBlank(itemType.getId())) {
            queryWrapper.ne("id", itemType.getId());
        }
        ItemType obj = baseMapper.selectOne(queryWrapper);
        if (obj != null) {
            throw new ILSBootException("B-FCT-0010");
        }
    }

    @Override
    public void delItemType(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void delBatchItemType(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
