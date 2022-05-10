package com.ils.modules.mes.base.machine.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.factory.entity.ReportTemplate;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.service.ReportTemplateService;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.mes.base.machine.entity.MachineMaintainPolicy;
import com.ils.modules.mes.base.machine.entity.MachineType;
import com.ils.modules.mes.base.machine.entity.MachineTypePolicy;
import com.ils.modules.mes.base.machine.entity.MachineTypeTaskEmployee;
import com.ils.modules.mes.base.machine.mapper.MachineTypeTaskEmployeeMapper;
import com.ils.modules.mes.base.machine.service.MachineMaintainPolicyService;
import com.ils.modules.mes.base.machine.service.MachineTypePolicyService;
import com.ils.modules.mes.base.machine.service.MachineTypeService;
import com.ils.modules.mes.base.machine.vo.MachineTypePolicyVO;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.system.entity.User;
import com.ils.modules.system.service.AbstractExcelHandler;
import com.ils.modules.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备类型策略导入类
 *
 * @author Anna.
 * @date 2021/7/5 15:57
 */
@Service
@Slf4j
public class MachineTypePolicyImportHandler extends AbstractExcelHandler {

    @Autowired
    private UnitService unitService;
    @Autowired
    private MachineTypeService machineTypeService;
    @Autowired
    private MachineMaintainPolicyService machineMaintainPolicyService;
    @Autowired
    private UserService sysUserService;
    @Autowired
    private MachineTypePolicyService machineTypePolicyService;
    @Autowired
    private MachineTypeTaskEmployeeMapper machineTypeTaskEmployeeMapper;
    @Autowired
    private ReportTemplateService reportTemplateService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {

        int importLine = 0;

        Map<String, List<MachineType>> typeNameMap = getMachineTyple();

        Map<String, List<MachineMaintainPolicy>> policyNameMap = getMachineMaintainPolicy();

        Map<String, List<ReportTemplate>> reportTemplateMap = getReportTemplateMap();


        for (Map<String, Object> data : dataList) {
            MachineTypePolicy machineTypePolicy = new MachineTypePolicy();

            MachineTypePolicyVO machineTypePolicyVO = BeanUtil.toBeanIgnoreError(data, MachineTypePolicyVO.class);

            BeanUtil.copyProperties(machineTypePolicyVO, machineTypePolicy);

            //判断是否存在该设备类型
            if (CollectionUtil.isEmpty(typeNameMap.get(machineTypePolicy.getMachineTypeId()))) {
                throw new ILSBootException("第" + importLine + "行导入失败，系统中不存在该设备类型");
            }
            machineTypePolicy.setMachineTypeId(typeNameMap.get(machineTypePolicy.getMachineTypeId()).get(0).getId());

            //判断是否存在该策略组
            if (CollectionUtil.isEmpty(policyNameMap.get(machineTypePolicy.getPolicyGroupId()))) {
                throw new ILSBootException("第" + importLine + "行导入失败，系统中不存在该策略组");
            }
            machineTypePolicy.setPolicyGroupId(policyNameMap.get(machineTypePolicy.getPolicyGroupId()).get(0).getId());

            //判断是否存在该模板 reportTemplateMap
            if (CollectionUtil.isEmpty(reportTemplateMap.get(machineTypePolicy.getTemplateId()))) {
                throw new ILSBootException("第" + importLine + "行导入失败，系统中不存在该模板");
            }
            machineTypePolicy.setTemplateId(reportTemplateMap.get(machineTypePolicy.getTemplateId()).get(0).getId());

            //保存设备策略
            machineTypePolicyService.saveMachineTypePolicy(machineTypePolicy);

            //判断是否存在该计划执行人
            String machineTypeTaskEmployees = machineTypePolicyVO.getMachineTypeTaskEmployees();
            String[] userNameList = machineTypeTaskEmployees.split("，");
            for (String userName : userNameList) {
                User user = sysUserService.getUserByName(userName);
                if (user == null) {
                    throw new ILSBootException("第" + importLine + "行导入失败，系统中不存在该用户");
                }
                MachineTypeTaskEmployee employee = new MachineTypeTaskEmployee();
                employee.setEmployeeId(user.getId());
                employee.setEmployeeName(user.getRealname());
                employee.setEmployeeCode(user.getOrgCode());
                employee.setPolicyId(machineTypePolicy.getId());
                machineTypeTaskEmployeeMapper.insert(employee);
            }
            importLine++;
        }

    }

    private Map<String, List<MachineType>> getMachineTyple() {
        QueryWrapper<MachineType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        queryWrapper.select("id,type_name");
        List<MachineType> list = machineTypeService.list(queryWrapper);
        Map<String, List<MachineType>> typeNameMap = list.stream().collect(Collectors.groupingBy(MachineType::getTypeName));

        return typeNameMap;
    }

    private Map<String, List<MachineMaintainPolicy>> getMachineMaintainPolicy() {
        QueryWrapper<MachineMaintainPolicy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        queryWrapper.select("id,data_name");
        List<MachineMaintainPolicy> list = machineMaintainPolicyService.list(queryWrapper);
        Map<String, List<MachineMaintainPolicy>> policyNameMap = list.stream().collect(Collectors.groupingBy(MachineMaintainPolicy::getDataName));
        return policyNameMap;
    }

    private Map<String, List<Unit>> getUnitMap() {
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id,unit_name");
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<Unit> list = unitService.list(queryWrapper);
        Map<String, List<Unit>> unitNameMap = list.stream().collect(Collectors.groupingBy(Unit::getUnitName));
        return unitNameMap;
    }

    private Map<String, List<ReportTemplate>> getReportTemplateMap() {

        QueryWrapper<ReportTemplate> reportTemplateQueryWrapper = new QueryWrapper<>();
        reportTemplateQueryWrapper.select("template_name,id");
        reportTemplateQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<ReportTemplate> list = reportTemplateService.list(reportTemplateQueryWrapper);
        Map<String, List<ReportTemplate>> templateMap = list.stream().collect(Collectors.groupingBy(ReportTemplate::getTemplateName));

        return templateMap;
    }
}
