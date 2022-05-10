package com.ils.modules.mes.config.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.config.entity.DefineField;
import com.ils.modules.mes.config.mapper.DefineFieldMapper;
import com.ils.modules.mes.config.service.DefineFieldService;

/**
 * @Description: 自定义字段
 * @Author: fengyi
 * @Date: 2020-10-13
 * @Version: V1.0
 */
@Service
public class DefineFieldServiceImpl extends ServiceImpl<DefineFieldMapper, DefineField> implements DefineFieldService {

    @Override
    public void saveDefineField(DefineField defineField) {
         baseMapper.insert(defineField);
    }

    @Override
    public void updateDefineField(DefineField defineField) {
        baseMapper.updateById(defineField);
    }

    @Override
    public void delDefineField(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void delBatchDefineField(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
