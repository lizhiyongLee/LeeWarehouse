package com.ils.modules.mes.base.qc.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.mes.base.factory.vo.WorkstationVO;
import com.ils.modules.mes.base.qc.entity.QcItem;
import com.ils.modules.mes.base.qc.entity.QcItemType;
import com.ils.modules.mes.base.qc.entity.QcMethod;
import com.ils.modules.mes.base.qc.entity.QcMethodDetail;
import com.ils.modules.mes.base.qc.service.QcItemService;
import com.ils.modules.mes.base.qc.service.QcItemTypeService;
import com.ils.modules.mes.base.qc.service.QcMethodService;
import com.ils.modules.mes.base.qc.vo.QcMethodVO;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.machine.entity.Machine;
import com.ils.modules.system.service.AbstractExcelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 质检方案的导入类
 *
 * @author Anna.
 * @date 2021/6/21 19:06
 */
@Component
public class QcMethodImportHandler extends AbstractExcelHandler {

    @Autowired
    private QcMethodService qcMethodService;
    @Autowired
    private QcItemService qcItemService;
    @Autowired
    private UnitService unitService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {

        Map<String, String> qcItemCodeIdMap = new HashMap<>();
        Map<String, String> unitNameIdMap = new HashMap<>();
        Map<String, QcMethodVO> mapQcMethodVO = new HashMap<>(16);

        //查询已存在的质检项集合
        QueryWrapper<QcItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<QcItem> existQcItemTypeList = qcItemService.list(queryWrapper);

        //查询已存在的质检项集合
        QueryWrapper<Unit> unitQueryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<Unit> existUnitTypeList = unitService.list(unitQueryWrapper);

        if (CollectionUtil.isNotEmpty(existQcItemTypeList)) {
            existQcItemTypeList.forEach(qcItem -> qcItemCodeIdMap.put(qcItem.getQcItemCode(), qcItem.getId()));
        }

        if (CollectionUtil.isNotEmpty(existUnitTypeList)) {
            existUnitTypeList.forEach(unit -> unitNameIdMap.put(unit.getUnitName(), unit.getId()));
        }
        //根据唯一属性建立获取主表
        Set<String> qcMethodNameList = new HashSet<>();
        for (Map<String, Object> data : dataList) {
            qcMethodNameList.add((String) data.get("methodName"));
        }

        qcMethodNameList.forEach(name -> {
            QcMethodVO qcMethodVO = new QcMethodVO();
            qcMethodVO.setQcMethodDetailList(new ArrayList<>());
            mapQcMethodVO.put(name, qcMethodVO);
        });
        //赋值
        for (Map<String, Object> data : dataList) {
            QcMethodVO qcMethodVO = mapQcMethodVO.get((String) data.get("methodName"));
            QcMethod qcMethod = BeanUtil.toBeanIgnoreError(data, QcMethod.class);
            BeanUtil.copyProperties(qcMethod, qcMethodVO);
            QcMethodDetail qcMethodDetail = BeanUtil.toBeanIgnoreError(data, QcMethodDetail.class);
            if (!qcItemCodeIdMap.containsKey((String) data.get("itemCode"))) {
                throw new ILSBootException("未查询到该质检项");
            } else {
                qcMethodDetail.setItemId(qcItemCodeIdMap.get((String) data.get("itemCode")));
            }
            if (!unitNameIdMap.containsKey((String) data.get("valueUnit"))) {
                throw new ILSBootException("未查询到该单位");
            } else {
                qcMethodDetail.setValueUnit(unitNameIdMap.get((String) data.get("valueUnit")));
            }
            qcMethodVO.getQcMethodDetailList().add(qcMethodDetail);
        }
        qcMethodService.saveMainList(new ArrayList<>(mapQcMethodVO.values()));
    }

}
