package com.ils.modules.mes.base.material.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.material.entity.ItemContainerType;
import com.ils.modules.mes.base.material.mapper.ItemContainerTypeMapper;
import com.ils.modules.mes.base.material.service.ItemContainerTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 物料类型
 * @Author: fengyi
 * @Date: 2020-10-22
 * @Version: V1.0
 */
@Service
public class ItemContainerTypeServiceImpl extends ServiceImpl<ItemContainerTypeMapper, ItemContainerType> implements ItemContainerTypeService {

    @Override
    public void saveItemContainerType(ItemContainerType itemContainerType) {
        this.checkCode(itemContainerType);
         baseMapper.insert(itemContainerType);
    }

    @Override
    public void updateItemContainerType(ItemContainerType itemContainerType) {
        this.checkCode(itemContainerType);
        baseMapper.updateById(itemContainerType);
    }

    private void checkCode(ItemContainerType itemContainerType) {
        // 编码不重复
        QueryWrapper<ItemContainerType> queryWrapper = new QueryWrapper();
        queryWrapper.eq("type_code", itemContainerType.getTypeCode());
        if (StringUtils.isNoneBlank(itemContainerType.getId())) {
            queryWrapper.ne("id", itemContainerType.getId());
        }
        ItemContainerType obj = baseMapper.selectOne(queryWrapper);
        if (obj != null) {
            throw new ILSBootException("B-FCT-0010");
        }
    }

    @Override
    public void delItemContainerType(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void delBatchItemContainerType(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
