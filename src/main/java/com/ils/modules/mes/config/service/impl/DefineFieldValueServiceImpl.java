package com.ils.modules.mes.config.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.config.entity.DefineFieldValue;
import com.ils.modules.mes.config.mapper.DefineFieldValueMapper;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.util.CommonUtil;

/**
 * @Description: 自定义字段值存储
 * @Author: fengyi
 * @Date: 2020-10-13
 * @Version: V1.0
 */
@Service
public class DefineFieldValueServiceImpl extends ServiceImpl<DefineFieldValueMapper, DefineFieldValue> implements DefineFieldValueService {

    @Override
    public void saveDefineFieldValue(DefineFieldValue defineFieldValue) {
         baseMapper.insert(defineFieldValue);
    }

    @Override
    public void updateDefineFieldValue(DefineFieldValue defineFieldValue) {
        baseMapper.updateById(defineFieldValue);
    }

    @Override
    public void delDefineFieldValue(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void delBatchDefineFieldValue(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public List<DefineFieldValueVO> queryDefineFieldValue(String tableCode, String mainId) {
        return baseMapper.queryDefineFieldValue(tableCode, mainId, CommonUtil.getTenantId());
    }

    @Override
    public void saveDefineFieldValue(List<DefineFieldValueVO> lstDefineFieldValueVO, String tableCode, String mainId) {
        // 先删除原来的自定义值
        baseMapper.deleteByMainId(tableCode, mainId, CommonUtil.getTenantId());

        // 在保存本次的自定义值
        if (lstDefineFieldValueVO == null || lstDefineFieldValueVO.size() == 0) {
            return;
        }
        for (DefineFieldValueVO entity : lstDefineFieldValueVO) {
            entity.setMainId(mainId);
            this.save(entity);
        }
    }
}
