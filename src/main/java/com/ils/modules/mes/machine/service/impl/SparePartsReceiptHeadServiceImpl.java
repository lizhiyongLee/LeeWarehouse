package com.ils.modules.mes.machine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.machine.entity.SparePartsStorage;
import com.ils.modules.mes.base.machine.mapper.SparePartsStorageMapper;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.machine.entity.SpareParts;
import com.ils.modules.mes.machine.entity.SparePartsReceiptHead;
import com.ils.modules.mes.machine.entity.SparePartsReceiptLine;
import com.ils.modules.mes.machine.mapper.SparePartsMapper;
import com.ils.modules.mes.machine.mapper.SparePartsReceiptHeadMapper;
import com.ils.modules.mes.machine.mapper.SparePartsReceiptLineMapper;
import com.ils.modules.mes.machine.service.SparePartsReceiptHeadService;
import com.ils.modules.mes.machine.vo.SparePartsReceiptHeadVO;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.service.CodeGeneratorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 备件入库表头
 * @Author: Conner
 * @Date: 2021-02-24
 * @Version: V1.0
 */
@Service
public class SparePartsReceiptHeadServiceImpl extends ServiceImpl<SparePartsReceiptHeadMapper, SparePartsReceiptHead> implements SparePartsReceiptHeadService {

    @Autowired
    private SparePartsReceiptLineMapper sparePartsReceiptLineMapper;
    @Autowired
    private CodeGeneratorService codeGeneratorService;
    @Autowired
    private SparePartsStorageMapper sparePartsStorageMapper;
    @Autowired
    private SparePartsMapper sparePartsMapper;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveSparePartsReceiptHead(SparePartsReceiptHeadVO sparePartsReceiptHeadVO) {
        SparePartsReceiptHead sparePartsReceiptHead = new SparePartsReceiptHead();
        BeanUtils.copyProperties(sparePartsReceiptHeadVO, sparePartsReceiptHead);
        List<SparePartsReceiptLine> sparePartsReceiptLineList = sparePartsReceiptHeadVO.getSparePartsReceiptLineList();
        //如果入库编码为空，则由系统自动生成
        String receiptCode = sparePartsReceiptHead.getReceiptCode();
        if (receiptCode.isEmpty() || receiptCode == null) {
            String code = codeGeneratorService.getNextCode(CommonUtil.getTenantId(),
                    MesCommonConstant.SPARE_PARTS_RECEIPT_NO, sparePartsReceiptHead);
            sparePartsReceiptHead.setReceiptCode(code);
        }
        //查询备件库
        QueryWrapper<SparePartsStorage> sparePartsStorageQueryWrapper = new QueryWrapper<>();
        sparePartsStorageQueryWrapper.eq("storage_code", sparePartsReceiptHead.getInStorageCode());
        SparePartsStorage sparePartsStorage = sparePartsStorageMapper.selectOne(sparePartsStorageQueryWrapper);
        //设置仓位名称
        sparePartsReceiptHead.setInStorageName(sparePartsStorage.getStorageName());
        baseMapper.insert(sparePartsReceiptHead);
        //保存明细行
        for (SparePartsReceiptLine sparePartsReceiptLine : sparePartsReceiptLineList) {
            //设置主表id和入库库位id
            sparePartsReceiptLine.setInStorageId(sparePartsStorage.getId())
                    .setReceiptHeadId(sparePartsReceiptHead.getId());
            sparePartsReceiptLineMapper.insert(sparePartsReceiptLine);
            //更新备件数量
            SpareParts spareParts = sparePartsMapper.selectById(sparePartsReceiptLine.getSparePartsId());
            spareParts.setQty(spareParts.getQty().add(sparePartsReceiptLine.getQty()));
            sparePartsMapper.updateById(spareParts);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateSparePartsReceiptHead(SparePartsReceiptHead sparePartsReceiptHead) {
        baseMapper.updateById(sparePartsReceiptHead);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delSparePartsReceiptHead(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchSparePartsReceiptHead(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public SparePartsReceiptHeadVO queryReceiptDetail(String headId) {
        //查询入库信息头
        SparePartsReceiptHead sparePartsReceiptHead = baseMapper.selectById(headId);
        SparePartsReceiptHeadVO sparePartsReceiptHeadVO = new SparePartsReceiptHeadVO();
        BeanUtils.copyProperties(sparePartsReceiptHead, sparePartsReceiptHeadVO);
        //查询入库信息行
        QueryWrapper<SparePartsReceiptLine> sparePartsReceiptLineQueryWrapper = new QueryWrapper<>();
        sparePartsReceiptLineQueryWrapper.eq("receipt_head_id", sparePartsReceiptHeadVO.getId());
        List<SparePartsReceiptLine> sparePartsReceiptLineList = sparePartsReceiptLineMapper.selectList(sparePartsReceiptLineQueryWrapper);
        sparePartsReceiptHeadVO.setSparePartsReceiptLineList(sparePartsReceiptLineList);
        return sparePartsReceiptHeadVO;
    }
}
