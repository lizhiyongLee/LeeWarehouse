package com.ils.modules.mes.base.machine.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.factory.entity.ReportTemplate;
import com.ils.modules.mes.base.factory.service.ReportTemplateService;
import com.ils.modules.mes.base.machine.entity.MachineLabel;
import com.ils.modules.mes.base.machine.entity.MachineType;
import com.ils.modules.mes.base.machine.service.MachineLabelService;
import com.ils.modules.mes.base.machine.service.MachineTypeService;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.service.AbstractExcelHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备类型导入处理类
 *
 * @author Anna.
 * @date 2021/7/5 13:55
 */
@Service
@Slf4j
public class MachineTypeImportHandler extends AbstractExcelHandler {
    @Autowired
    private MachineTypeService machineTypeService;
    @Autowired
    private ReportTemplateService reportTemplateService;
    @Autowired
    private MachineLabelService machineLabelService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {
        int importLine = 0;
        QueryWrapper<MachineType> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("type_name");
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachineType> list = machineTypeService.list(queryWrapper);
        List<String> typeNameList = list.stream().map(MachineType::getTypeName).collect(Collectors.toList());

        QueryWrapper<ReportTemplate> reportTemplateQueryWrapper = new QueryWrapper<>();
        reportTemplateQueryWrapper.select("template_name,id");
        reportTemplateQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<ReportTemplate> reportTemplateList = reportTemplateService.list(reportTemplateQueryWrapper);
        List<String> templateNameList = reportTemplateList.stream().map(ReportTemplate::getTemplateName).collect(Collectors.toList());

        QueryWrapper<MachineLabel> machineLabelQueryWrapper = new QueryWrapper<>();
        machineLabelQueryWrapper.select("machine_label_name,id");
        machineLabelQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        machineLabelQueryWrapper.eq("label_type", "1");
        List<MachineLabel> machineLabelList = machineLabelService.list(machineLabelQueryWrapper);
        Map<String, List<MachineLabel>> machineLabelMap = machineLabelList.stream().collect(Collectors.groupingBy(MachineLabel::getMachineLabelName));

        for (Map<String, Object> data : dataList) {
            MachineType machineType = BeanUtil.toBeanIgnoreError(data, MachineType.class);

            //判断系统中是否存在该设备类型
            if (typeNameList.contains(machineType.getTypeName())) {
                throw new ILSBootException("第" + importLine + "行导入失败，系统中已存在该设备类型名称");
            }

            //判断系统中是否存在该模板
            if (!templateNameList.contains(machineType.getTemplateId())) {
                throw new ILSBootException("第" + importLine + "行导入失败，系统中不存在存在该模板");
            }

            reportTemplateList.forEach(item -> {
                if (item.getTemplateName().equals(machineType.getTypeName())) {
                    machineType.setTemplateId(item.getId());
                }
            });
            //判断是否存在设备分类
            if (CollectionUtil.isEmpty(machineLabelMap.get(machineType.getLabelId()))) {
                throw new ILSBootException("第" + importLine + "行导入失败，系统中不存在该类型");
            }
            machineType.setLabelId(machineLabelMap.get(machineType.getLabelId()).get(0).getId());
            machineTypeService.saveMachineType(machineType);
            importLine++;
        }
    }
}
