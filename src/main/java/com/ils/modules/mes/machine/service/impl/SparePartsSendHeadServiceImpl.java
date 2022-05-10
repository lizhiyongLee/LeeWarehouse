package com.ils.modules.mes.machine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.machine.entity.SparePartsStorage;
import com.ils.modules.mes.base.machine.mapper.SparePartsStorageMapper;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.machine.entity.SpareParts;
import com.ils.modules.mes.machine.entity.SparePartsSendHead;
import com.ils.modules.mes.machine.entity.SparePartsSendLine;
import com.ils.modules.mes.machine.mapper.SparePartsMapper;
import com.ils.modules.mes.machine.mapper.SparePartsSendHeadMapper;
import com.ils.modules.mes.machine.mapper.SparePartsSendLineMapper;
import com.ils.modules.mes.machine.service.SparePartsSendHeadService;
import com.ils.modules.mes.machine.vo.SparePartsSendHeadVO;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.service.CodeGeneratorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 备件入库表头
 * @Author: Conner
 * @Date: 2021-02-24
 * @Version: V1.0
 */
@Service
public class SparePartsSendHeadServiceImpl extends ServiceImpl<SparePartsSendHeadMapper, SparePartsSendHead> implements SparePartsSendHeadService {

    @Autowired
    private SparePartsSendLineMapper sparePartsSendLineMapper;
    @Autowired
    private CodeGeneratorService codeGeneratorService;
    @Autowired
    private SparePartsStorageMapper sparePartsStorageMapper;
    @Autowired
    private SparePartsMapper sparePartsMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveSparePartsSendHead(SparePartsSendHeadVO sparePartsSendHeadVO) {
        List<SparePartsSendLine> sparePartsSendLineList = sparePartsSendHeadVO.getSparePartsSendLineList();
        SparePartsSendHead sparePartsSendHead = new SparePartsSendHead();
        BeanUtils.copyProperties(sparePartsSendHeadVO, sparePartsSendHead);
        //备件物料明细行chu库
        String sendCode = sparePartsSendHead.getSendCode();
        //通过仓位编码查出备件仓位
        QueryWrapper<SparePartsStorage> sparePartsStorageQueryWrapper = new QueryWrapper<>();
        sparePartsStorageQueryWrapper.eq("storage_code", sparePartsSendHead.getOutStorageCode());
        SparePartsStorage sparePartsStorage = sparePartsStorageMapper.selectOne(sparePartsStorageQueryWrapper);
        //设置关于仓位的属性
        sparePartsSendHead.setOutStorageName(sparePartsStorage.getStorageName());
        //插入表头
        if (sendCode.isEmpty() || sendCode == null) {
            String saleOrderNo = codeGeneratorService.getNextCode(CommonUtil.getTenantId(),
                    MesCommonConstant.SPARE_PARTS_RECEIPT_NO, sparePartsSendHead);
            sparePartsSendHead.setSendCode(saleOrderNo);
        }
        baseMapper.insert(sparePartsSendHead);
        for (SparePartsSendLine sparePartsSendLine : sparePartsSendLineList) {
            //判断出库数量是否大于该仓库下的库存数量
            BigDecimal qty = sparePartsMapper.queryQtyByStorageIdAndSparePartId(sparePartsStorage.getId(), sparePartsSendLine.getSparePartsId());
            if (qty == null || qty.compareTo(sparePartsSendLine.getQty()) == -1) {
                throw new ILSBootException("P-OW-0065");
            }
            sparePartsSendLine.setOutStorageId(sparePartsStorage.getId())
                    .setSendHeadId(sparePartsSendHead.getId());
            sparePartsSendLineMapper.insert(sparePartsSendLine);
            //更新备件数量
            SpareParts spareParts = sparePartsMapper.selectById(sparePartsSendLine.getSparePartsId());
            spareParts.setQty(spareParts.getQty().subtract(sparePartsSendLine.getQty()));
            sparePartsMapper.updateById(spareParts);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateSparePartsSendHead(SparePartsSendHead sparePartsSendHead) {
        baseMapper.updateById(sparePartsSendHead);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delSparePartsSendHead(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchSparePartsSendHead(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
