package com.ils.modules.mes.label.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.enums.LabelUseStatusEnum;
import com.ils.modules.mes.enums.ReceiptStatusEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.label.entity.LabelManage;
import com.ils.modules.mes.label.entity.LabelManageLine;
import com.ils.modules.mes.label.mapper.LabelManageLineMapper;
import com.ils.modules.mes.label.mapper.LabelManageMapper;
import com.ils.modules.mes.label.service.LabelManageLineService;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.mapper.ItemCellMapper;
import com.ils.modules.mes.material.service.ItemCellService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/13 10:54
 */
@Slf4j
@Service
public class LabelManageLineServiceImpl extends ServiceImpl<LabelManageLineMapper, LabelManageLine> implements LabelManageLineService {

    @Autowired
    private LabelManageLineMapper labelManageLineMapper;

    @Autowired
    private LabelManageMapper labelManageMapper;
    @Autowired
    private ItemCellMapper itemCellMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void printingBatch(List<String> ids) {
        for (String id : ids) {
            LabelManageLine labelManageLine = labelManageLineMapper.selectById(id);
            labelManageLine.setPrintStatus(ZeroOrOneEnum.ONE.getStrCode());
            labelManageLine.setPrintTimes(labelManageLine.getPrintTimes() + 1);
            labelManageLineMapper.updateById(labelManageLine);

            LabelManage labelManage = labelManageMapper.selectById(labelManageLine.getLabelManageId());
            labelManage.setReceiptStatus(ReceiptStatusEnum.PRINTED.getValue());
            labelManageMapper.updateById(labelManage);
        }
    }

    @Override
    public void cancelBatch(List<String> ids) {
        for (String id : ids) {
            LabelManageLine labelManageLine = labelManageLineMapper.selectById(id);
            labelManageLine.setUseStatus(LabelUseStatusEnum.CANCEL.getValue());
            labelManageLineMapper.updateById(labelManageLine);
        }
    }
    @Override
    public LabelManageLine queryByCode(String code){
        QueryWrapper<LabelManageLine> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("qrcode",code);
        return labelManageLineMapper.selectOne(queryWrapper);
    }
}
