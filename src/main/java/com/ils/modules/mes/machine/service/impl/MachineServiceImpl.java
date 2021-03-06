package com.ils.modules.mes.machine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.util.TenantContext;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.mapper.WorkstationMapper;
import com.ils.modules.mes.base.factory.service.WorkstationService;
import com.ils.modules.mes.base.machine.entity.MachineTypeData;
import com.ils.modules.mes.base.machine.entity.MachineTypePolicy;
import com.ils.modules.mes.base.machine.entity.MachineTypeTaskEmployee;
import com.ils.modules.mes.base.machine.mapper.MachineTypeDataMapper;
import com.ils.modules.mes.base.machine.mapper.MachineTypePolicyMapper;
import com.ils.modules.mes.base.machine.mapper.MachineTypeTaskEmployeeMapper;
import com.ils.modules.mes.base.machine.service.MachineTypeService;
import com.ils.modules.mes.base.machine.vo.MachineTypeParaVO;
import com.ils.modules.mes.base.product.vo.ResultDataVO;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.*;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.execution.mapper.WorkProduceRecordMapper;
import com.ils.modules.mes.machine.entity.*;
import com.ils.modules.mes.machine.mapper.*;
import com.ils.modules.mes.machine.service.MachineParaHeadService;
import com.ils.modules.mes.machine.service.MachineService;
import com.ils.modules.mes.machine.service.MachineTaskLogService;
import com.ils.modules.mes.machine.vo.*;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.message.service.MsgHandleServer;
import com.ils.modules.message.vo.MsgParamsVO;
import com.ils.modules.quartz.entity.QuartzJob;
import com.ils.modules.quartz.service.IQuartzJobService;
import com.ils.modules.system.entity.SysDocument;
import com.ils.modules.system.service.CodeGeneratorService;
import com.ils.modules.system.service.SysDocumentService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: ????????????
 * @Author: Conner
 * @Date: 2020-11-10
 * @Version: V1.0
 */
@Service
public class MachineServiceImpl extends ServiceImpl<MachineMapper, Machine> implements MachineService {

    /**
     * ????????????
     */
    public static final String MACHINE_MAINTENANCE_JOB = "com.ils.modules.mes.machine.job.MachineMaintenanceSingleJob";
    public static final String JOB_ID = "MachineMaintenanceSingleJob";

    @Autowired
    private MachineTypePolicyMapper machineTypePolicyMapper;
    @Autowired
    private MachinePolicyMapper machinePolicyMapper;
    @Autowired
    private MachineTypeDataMapper machineTypeDataMapper;
    @Autowired
    private MachineDataMapper machineDataMapper;
    @Autowired
    private MachineStopTimeMapper machineStopTimeMapper;
    @Autowired
    private MachineLogMapper machineLogMapper;
    @Autowired
    private MachineMaintenanceTaskMapper machineMaintenanceTaskMapper;
    @Autowired
    private MachineMaintenanceTaskEmployeeMapper machineMaintenanceTaskEmployeeMapper;
    @Autowired
    private WorkstationMapper workstationMapper;
    @Autowired
    private MachineTypeTaskEmployeeMapper machineTypeTaskEmployeeMapper;
    @Autowired
    private IQuartzJobService quartzJobService;
    @Autowired
    private CodeGeneratorService codeGeneratorService;
    @Autowired
    private MachineTaskLogService machineTaskLogService;
    @Autowired
    private WorkstationService workstationService;
    @Autowired
    private MsgHandleServer msgHandleServer;
    @Autowired
    private MachineTypeService machineTypeService;
    @Autowired
    private MachineParaHeadService machineParaHeadService;
    @Autowired
    private WorkProduceRecordMapper workProduceRecordMapper;
    @Autowired
    private SysDocumentService sysDocumentService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachine(Machine machine) {
        machine.setStatus(ZeroOrOneEnum.ZERO.getStrCode());
        baseMapper.insert(machine);

        //??????????????????????????????
        QueryWrapper<MachineTypeTaskEmployee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
        employeeQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());

        //????????????id???????????????id???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        QueryWrapper<MachineTypePolicy> policyQueryWrapper = new QueryWrapper<>();
        policyQueryWrapper.eq("machine_type_id", machine.getMachineTypeId());
        policyQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachineTypePolicy> policyList = machineTypePolicyMapper.selectList(policyQueryWrapper);
        if (CollectionUtil.isNotEmpty(policyList)) {
            for (MachineTypePolicy machineTypePolicy : policyList) {
                MachinePolicy machinePolicy = new MachinePolicy();
                BeanUtils.copyProperties(machineTypePolicy, machinePolicy, "id", "createTime", "updateTime", "updateBy");
                machinePolicy.setMachineId(machine.getId());
                machinePolicy.setStatus(ZeroOrOneEnum.ZERO.getStrCode());
                machinePolicyMapper.insert(machinePolicy);
                //?????????????????????????????????
                if (MesCommonConstant.POLICY_STATUS_ONE.equals(machinePolicy.getStatus())) {
                    this.createMaintenanceQuartzJob(machinePolicy.getId());
                }
            }
        }

        //?????????????????????id??????????????????????????????????????????????????????????????????????????????
        QueryWrapper<MachineTypeData> dataConfigQueryWrapper = new QueryWrapper<>();
        dataConfigQueryWrapper.eq("machine_type_id", machine.getMachineTypeId());
        List<MachineTypeData> machineTypeDataList = machineTypeDataMapper.selectList(dataConfigQueryWrapper);
        if (CollectionUtil.isNotEmpty(machineTypeDataList)) {
            for (MachineTypeData machineTypeData : machineTypeDataList) {
                MachineData machineData = new MachineData();
                machineData.setDataId(machineTypeData.getDataId());
                machineData.setDataAdress(machineTypeData.getDataAdress());
                machineData.setUnit(machineTypeData.getUnit());
                machineData.setDataName(machineTypeData.getDataName());
                machineData.setMachineId(machine.getId());
                machineDataMapper.insert(machineData);
            }
        }
        //????????????????????????????????????????????????????????????????????????????????????
        List<MachineTypeParaVO> machineTypeParaVOList = machineTypeService.queryMachineTypeParaByMainId(machine.getMachineTypeId());
        machineParaHeadService.saveMachineParaHeadVO(machineTypeParaVOList, machine.getId());

        String description = "??????????????????" + machine.getMachineName();
        addMachineLog(machine.getId(), MesCommonConstant.MACHINE_TYPE_ADD, description);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachine(MachineVO machineVO) {
        Machine machine = new Machine();
        BeanUtil.copyProperties(machineVO, machine);
        baseMapper.updateById(machine);
        //??????????????????
        List<MachinePolicy> newMachinePolicyList = machineVO.getMachinePolicyList();
        this.updatePolicy(newMachinePolicyList, machine);
    }

    private void updatePolicy(List<MachinePolicy> newMachinePolicyList, Machine machine) {
        QueryWrapper<MachinePolicy> machinePolicyQueryWrapper = new QueryWrapper<>();
        machinePolicyQueryWrapper.eq("machine_id", machine.getId());
        machinePolicyQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
        machinePolicyQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachinePolicy> oldMachinePolicyList = machinePolicyMapper.selectList(machinePolicyQueryWrapper);
        List<MachinePolicy> updatePolicyList = new ArrayList<>();

        //??????????????????
        if (!CommonUtil.isEmptyOrNull(newMachinePolicyList)) {
            newMachinePolicyList.forEach(policy -> oldMachinePolicyList.forEach(oldPolicy -> {
                if (oldPolicy.getPolicyName().equals(policy.getPolicyName())) {
                    //????????????
                    if (!oldPolicy.getStatus().equals(policy.getStatus())) {
                        this.changeMachinePolicyStatus(policy.getId(), policy.getStatus());
                    }
                    //??????????????????
                    boolean newCheck = policy.getExcuteBaseTime() != null && oldPolicy.getExcuteBaseTime() == null;
                    boolean updateCheck = policy.getExcuteBaseTime() != null && oldPolicy.getExcuteBaseTime() != null && (!oldPolicy.getExcuteBaseTime().equals(policy.getExcuteBaseTime()));
                    if (newCheck || updateCheck) {
                        this.addExcuteBaseTime(policy.getId(), DateUtil.formatDateTime(policy.getExcuteBaseTime()));
                    }
                    //????????????????????????
                    policy.setId(oldPolicy.getId());
                    machinePolicyMapper.updateById(policy);
                    updatePolicyList.add(policy);
                }
            }));
            newMachinePolicyList.removeAll(updatePolicyList);
        }
        //????????????
        if (!CommonUtil.isEmptyOrNull(newMachinePolicyList)) {
            //????????????id???????????????id???????????????????????????????????????????????????????????????????????????????????????????????????????????????
            QueryWrapper<MachineTypePolicy> policyQueryWrapper = new QueryWrapper<>();
            policyQueryWrapper.eq("machine_type_id", machine.getMachineTypeId());
            policyQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
            List<MachineTypePolicy> policyList = machineTypePolicyMapper.selectList(policyQueryWrapper);
            newMachinePolicyList.forEach(machinePolicy -> {
                for (MachineTypePolicy machineTypePolicy : policyList) {
                    if (machineTypePolicy.getPolicyName().equals(machinePolicy.getPolicyName())) {
                        BeanUtils.copyProperties(machineTypePolicy, machinePolicy, "id", "createTime", "updateTime", "updateBy");
                        machinePolicy.setId(null);
                        machinePolicy.setCreateTime(null);
                        machinePolicy.setUpdateTime(null);
                        machinePolicy.setUpdateBy(null);
                        machinePolicy.setStatus(ZeroOrOneEnum.ZERO.getStrCode());
                        machinePolicy.setMachineId(machine.getId());
                        machinePolicyMapper.insert(machinePolicy);
                        //?????????????????????????????????
                        if (MesCommonConstant.POLICY_STATUS_ONE.equals(machinePolicy.getStatus())) {
                            this.createMaintenanceQuartzJob(machinePolicy.getId());
                        }
                    }
                }
            });
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachinePara(MachineVO machineVO) {
        List<MachineParaVO> machineParaVOList = machineVO.getMachineParaVOList();
        machineParaHeadService.deleteByMachineId(machineVO.getId());
        machineParaHeadService.saveBatch(machineParaVOList, machineVO.getId());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteMachinePara(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        idList.forEach(id -> {
            MachineParaHead machineParaHead = machineParaHeadService.getById(id);
            if (machineParaHead.getStatus().equals(ZeroOrOneEnum.ONE.getStrCode())) {
                throw new ILSBootException("?????????????????????????????????");
            } else {
                machineParaHeadService.deleteById(id);
            }
        });

    }

    @Override
    public void updateMachineData(MachineVO machineVO) {
        machineDataMapper.deleteByMainId(machineVO.getId());
        List<MachineData> machineDataList = machineVO.getMachineDataList();
        if (!CommonUtil.isEmptyOrNull(machineDataList)) {
            machineDataList.forEach(machineData -> machineDataMapper.insert(machineData));
        }
    }

    @Override
    public Page<MachineTypePolicy> getPolicyByMachineTypeId(String machineTypeId, Page<MachineTypePolicy> page) {
        QueryWrapper<MachineTypePolicy> policyQueryWrapper = new QueryWrapper<>();
        policyQueryWrapper.eq("machine_type_id", machineTypeId);
        policyQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        policyQueryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
        return machineTypePolicyMapper.selectPage(page, policyQueryWrapper);
    }

    @Override
    public Page<MachineTypeData> getDataByMachineTypeId(String machineTypeId, Page<MachineTypeData> page) {
        QueryWrapper<MachineTypeData> dataQueryWrapper = new QueryWrapper<>();
        dataQueryWrapper.eq("machine_type_id", machineTypeId);
        dataQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        return machineTypeDataMapper.selectPage(page, dataQueryWrapper);
    }


    @Override
    public MachineVO selectDetailById(String id) {
        //????????????????????????
        Machine machine = baseMapper.selectById(id);
        String machineId = machine.getId();
        MachineVO machineVO = new MachineVO();
        BeanUtils.copyProperties(machine, machineVO);
        //????????????????????????,?????????????????????????????????machineVO
        QueryWrapper<MachineData> dataQueryWrapper = new QueryWrapper<>();
        dataQueryWrapper.eq("machine_id", machineId);
        dataQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
        dataQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachineData> machineDataList = machineDataMapper.selectList(dataQueryWrapper);
        machineVO.setMachineDataList(machineDataList);
        //????????????????????????,????????????machineVO
        QueryWrapper<MachinePolicy> machinePolicyQueryWrapper = new QueryWrapper<>();
        machinePolicyQueryWrapper.eq("machine_id", machineId);
        machinePolicyQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
        machinePolicyQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachinePolicy> machinePolicyList = machinePolicyMapper.selectList(machinePolicyQueryWrapper);
        machineVO.setMachinePolicyList(machinePolicyList);
        //?????????????????????????????????machineVO
        QueryWrapper<MachineStopTime> machineStopTimeQueryWrapper = new QueryWrapper<>();
        machineStopTimeQueryWrapper.eq("machine_id", machineId);
        machineStopTimeQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
        machineStopTimeQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        machineStopTimeQueryWrapper.orderByDesc("start_time");
        List<MachineStopTime> machineStopTimeList = machineStopTimeMapper.selectList(machineStopTimeQueryWrapper);
        machineVO.setMachineStopTimeList(machineStopTimeList);
        //?????????????????????????????????
        List<MachineRepairAndMaintenanceVO> machineRepairAndMaintenanceVOList = baseMapper.queryRepairAndMaintenanceTaskByMachineId(machine.getId());
        machineVO.setMachineRepairAndMaintenanceVOList(machineRepairAndMaintenanceVOList);
        //?????????????????????
        QueryWrapper<Workstation> workstationQueryWrapper = new QueryWrapper<>();
        workstationQueryWrapper.eq("equipment", machineId);
        workstationQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
        workstationQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        workstationQueryWrapper.orderByDesc("create_time");
        List<Workstation> workstationList = workstationService.list(workstationQueryWrapper);
        machineVO.setWorkstationList(workstationList);
        //??????????????????????????????
        List<MachineParaVO> machineParaVOList = machineParaHeadService.queryParaDataByMachineId(id);
        machineVO.setMachineParaVOList(machineParaVOList);

        return machineVO;
    }

    /**
     * ??????????????????
     * ????????????????????????????????????????????????id??????
     *
     * @param policyId
     */
    public void createMaintenanceQuartzJob(String policyId) {
        QuartzJob quartzJob = quartzJobService.getById(JOB_ID);
        //??????????????????????????????????????????????????????id??????
        if (null == quartzJob) {
            String baseCron = "0 */1 * * * ? *";
            quartzJob = new QuartzJob();
            quartzJob.setId(JOB_ID);
            quartzJob.setCronExpression(baseCron);
            quartzJob.setJobNameKey(MACHINE_MAINTENANCE_JOB);
            quartzJob.setJobClassName(MACHINE_MAINTENANCE_JOB);
            quartzJob.setStatus(MesCommonConstant.STATUS_NORMAL);
            Set<String> ids = new HashSet<>();
            ids.add(policyId);
            quartzJob.setParameter(JSONArray.toJSONString(ids));
            quartzJobService.saveAndScheduleJob(quartzJob);
        } else {
            String parameter = quartzJob.getParameter();
            Set<String> ids = new HashSet<>(JSONArray.parseArray(parameter, String.class));
            if (CommonUtil.isEmptyOrNull(ids)) {
                ids = new HashSet<>();
            }
            ids.add(policyId);
            quartzJob.setParameter(JSONArray.toJSONString(ids));
            try {
                quartzJobService.editAndScheduleJob(quartzJob);
            } catch (SchedulerException e) {
                throw new ILSBootException("??????????????????");
            }
        }
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public MachineDetailsVO getMachineDetails(String id) {
        //??????????????????
        Machine machine = baseMapper.selectById(id);
        MachineDetailsVO machineDetailsVO = new MachineDetailsVO();
        BeanUtils.copyProperties(machine, machineDetailsVO);
        //?????????????????????????????????
        List<MachineRepairAndMaintenanceVO> machineRepairAndMaintenanceVOList = baseMapper.queryRepairAndMaintenanceTaskByMachineId(machine.getId());
        machineDetailsVO.setMachineRepairAndMaintenanceVOList(machineRepairAndMaintenanceVOList);
        //?????????????????????
        QueryWrapper<MachinePolicy> machinePolicyQueryWrapper = new QueryWrapper<>();
        machinePolicyQueryWrapper.eq("machine_id", machine.getId());
        machinePolicyQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
        machinePolicyQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachinePolicy> machinePolicyList = machinePolicyMapper.selectList(machinePolicyQueryWrapper);
        machineDetailsVO.setMachinePolicyList(machinePolicyList);
        //??????????????????????????????
        QueryWrapper<MachineStopTime> machineStopTimeQueryWrapper = new QueryWrapper<>();
        machineStopTimeQueryWrapper.eq("machine_id", machine.getId());
        machineStopTimeQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
        machineStopTimeQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachineStopTime> machineStopTimeList = machineStopTimeMapper.selectList(machineStopTimeQueryWrapper);
        machineDetailsVO.setMachineStopTimeList(machineStopTimeList);
        //????????????
        QueryWrapper<MachineData> machineDataQueryWrapper = new QueryWrapper<>();
        machineDataQueryWrapper.eq("machine_id", machine.getId());
        machineDataQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
        machineDataQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachineData> machineDataList = machineDataMapper.selectList(machineDataQueryWrapper);
        machineDetailsVO.setMachineDataList(machineDataList);
        return machineDetailsVO;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void changeMachineStatus(String machineId, String status) {
        Machine machine = baseMapper.selectById(machineId);
        String oprateType = "";
        String description = "";
        if (MesCommonConstant.MACHINE_STATUS_ONE.equals(status)) {
            oprateType = MesCommonConstant.MACHINE_TYPE_ENABLE;
            description = "????????????" + machine.getMachineName();
        } else {
            oprateType = MesCommonConstant.MACHINE_TYPE_DISABLE;
            description = "????????????" + machine.getMachineName();
        }
        addMachineLog(machineId, oprateType, description);
        machine.setStatus(status);
        baseMapper.updateById(machine);
        //??????????????????
        QueryWrapper<MachinePolicy> machinePolicyQueryWrapper = new QueryWrapper<>();
        machinePolicyQueryWrapper.eq("machine_id", machine.getId());
        machinePolicyQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
        machinePolicyQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachinePolicy> machinePolicyList = machinePolicyMapper.selectList(machinePolicyQueryWrapper);
        machinePolicyList.forEach(machinePolicy -> {
            //?????????????????????????????????
            if (MesCommonConstant.POLICY_STATUS_ONE.equals(machinePolicy.getStatus())) {
                this.createMaintenanceQuartzJob(machinePolicy.getId());
            }
        });
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void performedManuallyPolicy(String policyId) {
        MachinePolicy machinePolicy = machinePolicyMapper.selectById(policyId);
        MachineMaintenanceTask machineMaintenanceTask = new MachineMaintenanceTask();
        //????????????????????????
        Date date = new Date();
        machineMaintenanceTask.setPlanStartTime(date);
        Integer planTime = machinePolicy.getPlanTime();
        String timeUnit = machinePolicy.getTimeUnit();
        //????????????????????????
        if (MesCommonConstant.POLICY_TIME_UNIT_ONE.equals(timeUnit)) {
            machineMaintenanceTask.setPlanEndTime(new Date(1000 * 60 * planTime + date.getTime()));
        } else if (MesCommonConstant.POLICY_TIME_UNIT_TWO.equals(timeUnit)) {
            machineMaintenanceTask.setPlanEndTime(new Date(1000 * 60 * 60 * planTime + date.getTime()));
        }
        machineMaintenanceTask.setCreateBy(CommonUtil.getLoginUser().getRealname());
        machineMaintenanceTask.setMaintainMachineId(machinePolicy.getMachineId());
        machineMaintenanceTask.setMaintenanceType(machinePolicy.getPolicyType());
        machineMaintenanceTask.setPolicyCode("CLCODE" + machinePolicy.getId());
        machineMaintenanceTask.setStatus(machinePolicy.getStatus());
        machineMaintenanceTask.setTaskTitle(machinePolicy.getTaskName());
        machineMaintenanceTask.setTaskCode("RWCODE" + machinePolicy.getId());
        machineMaintenanceTask.setPolicyName(machinePolicy.getPolicyName());
        machineMaintenanceTask.setAttach(machinePolicy.getAttach());
        machineMaintenanceTask.setPolicyId(policyId);
        machineMaintenanceTaskMapper.insert(machineMaintenanceTask);
        //???????????????????????????????????????????????????
        List<MachineTypeTaskEmployee> typeTaskEmployees = machineTypeTaskEmployeeMapper.selectByMaintenanceTaskId(policyId);
        if (!typeTaskEmployees.isEmpty()) {
            for (MachineTypeTaskEmployee machineTypeTaskEmployee : typeTaskEmployees) {
                MachineMaintenanceTaskEmployee employee = new MachineMaintenanceTaskEmployee();
                employee.setEmployeeName(machineTypeTaskEmployee.getEmployeeName());
                employee.setRepairTaskId(machineMaintenanceTask.getId());
                employee.setEmployeeCode(machineTypeTaskEmployee.getEmployeeCode());
                employee.setEmployeeId(machineTypeTaskEmployee.getEmployeeId());
                machineMaintenanceTaskEmployeeMapper.insert(employee);
            }
        }
        machinePolicy.setLastExcuteTime(date);
        machinePolicyMapper.updateById(machinePolicy);
        String description = "??????????????????" + machinePolicy.getPolicyName();
        addMachineLog(machinePolicy.getMachineId(), MesCommonConstant.MACHINE_TYPE_ADD, description);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addExcuteBaseTime(String policyId, String baseTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = null;
        try {
            dateTime = simpleDateFormat.parse(baseTime);
        } catch (ParseException e) {
            throw new ILSBootException("M-MC-0002");
        }
        Date now = new Date();
        if (now.getTime() - dateTime.getTime() > 0) {
            throw new ILSBootException("M-MC-0001");
        }
        //?????????????????????????????????
        if (baseTime == null) {
            throw new ILSBootException("M-MC-0003");
        }
        MachinePolicy machinePolicyLast = machinePolicyMapper.selectById(policyId);
        boolean sameDate = this.isSameDate(machinePolicyLast.getExcuteBaseTime(), dateTime);
        if (!sameDate) {
            if (MachinePolicyRuleEnum.FIXED_CYCLE.getValue().equals(machinePolicyLast.getPolicyRule())) {
                this.createMaintenanceQuartzJob(machinePolicyLast.getId());
            } else if (MesCommonConstant.POLICY_RULE_THREE.equals(machinePolicyLast.getPolicyRule())) {

            }
        }
        //??????????????????
        MachinePolicy machinePolicy = new MachinePolicy();
        machinePolicy.setId(policyId);
        machinePolicy.setExcuteBaseTime(dateTime);
        machinePolicyMapper.updateById(machinePolicy);
        //??????????????????
        addMachineLog(machinePolicyLast.getMachineId(), MesCommonConstant.MACHINE_TYPE_ENABLE, "????????????" + machinePolicyLast.getPolicyName() + "????????????");
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void changeMachinePolicyStatus(String policyId, String status) {
        String description = "";
        String oprateType = "";
        //???????????????????????????
        MachinePolicy machinePolicy = machinePolicyMapper.selectById(policyId);
        if (MesCommonConstant.POLICY_STATUS_ZERO.equals(machinePolicy.getStatus()) &&
                MesCommonConstant.POLICY_STATUS_ONE.equals(status)) {
            description = "????????????" + machinePolicy.getPolicyName();
            oprateType = MesCommonConstant.MACHINE_TYPE_ENABLE;
            //????????????
            this.createMaintenanceQuartzJob(machinePolicy.getId());
        } else if (MesCommonConstant.POLICY_STATUS_ZERO.equals(status) && MesCommonConstant.POLICY_STATUS_ONE.equals(machinePolicy.getStatus())) {
            description = "????????????" + machinePolicy.getPolicyName();
            oprateType = MesCommonConstant.MACHINE_TYPE_DISABLE;
        }
        addMachineLog(machinePolicy.getMachineId(), oprateType, description);
        machinePolicy.setStatus(status);
        machinePolicyMapper.updateById(machinePolicy);
    }

    /**
     * ????????????????????????????????????????????????
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void createMaintenanceTaskJob(String policyId) {
        Date date = new Date();
        MachinePolicy machinePolicy = machinePolicyMapper.selectById(policyId);
        MachineMaintenanceTask machineMaintenanceTask = new MachineMaintenanceTask();
        //????????????????????????
        machineMaintenanceTask.setPlanStartTime(date);
        Integer planTime = machinePolicy.getPlanTime();
        String timeUnit = machinePolicy.getTimeUnit();
        //????????????????????????
        if (MesCommonConstant.POLICY_TIME_UNIT_ONE.equals(timeUnit)) {
            machineMaintenanceTask.setPlanEndTime(new Date(1000 * 60 * planTime + date.getTime()));
        } else if (MesCommonConstant.POLICY_TIME_UNIT_TWO.equals(timeUnit)) {
            machineMaintenanceTask.setPlanEndTime(new Date(1000 * 60 * 60 * planTime + date.getTime()));
        }
        //?????????????????????????????????????????????????????????
        machineMaintenanceTask.setCreateBy(MesCommonConstant.MAINTENANECE_TASK_MAN);
        machineMaintenanceTask.setMaintainMachineId(machinePolicy.getMachineId());
        machineMaintenanceTask.setMaintenanceType(machinePolicy.getPolicyType());
        machineMaintenanceTask.setTaskTitle(machinePolicy.getTaskName());
        machineMaintenanceTask.setPolicyName(machinePolicy.getPolicyName());
        machineMaintenanceTask.setTemplateId(machinePolicy.getTemplateId());
        machineMaintenanceTask.setAttach(machinePolicy.getAttach());
        machineMaintenanceTask.setPolicyId(policyId);
        machineMaintenanceTask.setStatus(MachineMaintenanceStatusEnum.NOT_YET_START.getValue());
        //???????????????0
        String tenantId = machinePolicy.getTenantId();
        if (StringUtils.isBlank(tenantId)) {
            tenantId = "0";
        }
        TenantContext.setTenant(tenantId);
        machineMaintenanceTask.setTenantId(tenantId);
        String maintenanceCode = codeGeneratorService.getNextCode(tenantId, MesCommonConstant.MACHINE_MAINTENANCE_CODE, machineMaintenanceTask);
        machineMaintenanceTask.setTaskCode(maintenanceCode);
        machineMaintenanceTaskMapper.insert(machineMaintenanceTask);
        //???????????????????????????????????????????????????
        List<MachineTypeTaskEmployee> typeTaskEmployees = machineTypeTaskEmployeeMapper.selectByMaintenanceTaskId(policyId);
        if (!typeTaskEmployees.isEmpty()) {
            for (MachineTypeTaskEmployee machineTypeTaskEmployee : typeTaskEmployees) {
                MachineMaintenanceTaskEmployee employee = new MachineMaintenanceTaskEmployee();
                employee.setEmployeeName(machineTypeTaskEmployee.getEmployeeName());
                employee.setRepairTaskId(machineMaintenanceTask.getId());
                employee.setEmployeeCode(machineTypeTaskEmployee.getEmployeeCode());
                employee.setEmployeeId(machineTypeTaskEmployee.getEmployeeId());
                machineMaintenanceTaskEmployeeMapper.insert(employee);
            }
        }

        //??????????????????????????????
        if (CollectionUtil.isNotEmpty(typeTaskEmployees)) {
            //??????????????????
            JSONObject task = new JSONObject();
            task.put("taskName", machineMaintenanceTask.getPolicyName());
            task.put("machineName", baseMapper.selectById(machineMaintenanceTask.getMaintainMachineId()).getMachineName());

            MsgParamsVO msgParamsVO = new MsgParamsVO(typeTaskEmployees.stream().map(MachineTypeTaskEmployee::getEmployeeId).collect(Collectors.toList())
                    , null, null, "????????????", task);
            msgParamsVO.setTenantId(TenantContext.getTenant());
            //????????????????????????????????????????????????????????????????????????????????????????????????
            if (MesCommonConstant.MAINTENANECE_TASK_TYPE_SPOT_CHECK.equals(machinePolicy.getPolicyType())) {
                msgHandleServer.sendMsg(MesCommonConstant.CHECK_ADD_MODULE, MesCommonConstant.CHECK_ADD_OPERACTION, msgParamsVO);
            } else if (MesCommonConstant.MAINTENANECE_TASK_TYPE_MAINTENANECE.equals(machinePolicy.getPolicyType())) {
                msgHandleServer.sendMsg(MesCommonConstant.MAINTENACE_ADD_MODULE, MesCommonConstant.MAINTENACE_ADD_OPERACTION, msgParamsVO);
            }
        }

        machinePolicy.setLastExcuteTime(date);
        machinePolicyMapper.updateById(machinePolicy);
        //??????????????????
        MachineTaskLog machineTaskLog = new MachineTaskLog();
        machineTaskLog.setTaskId(machineMaintenanceTask.getId());
        String taskType = "";
        if (MesCommonConstant.MAINTENANECE_TASK_TYPE_SPOT_CHECK.equals(machineMaintenanceTask.getMaintenanceType())) {
            taskType = MachineTaskTypeEnum.CHECK_TASKS.getValue();
        } else {
            taskType = MachineTaskTypeEnum.MAINTENANCE_TASKS.getValue();
        }
        machineTaskLog.setTaskType(taskType);
        machineTaskLog.setOprateType(MachineTaskOprateTypeEnum.CREATE_TASK.getValue());
        String username = CommonUtil.getLoginUser().getUsername();
        if (StringUtils.isBlank(username)) {
            username = MesCommonConstant.MAINTENANECE_TASK_MAN;
        }
        machineTaskLog.setDescription(username + "||" + MachineTaskOprateTypeEnum.CREATE_TASK + "||" + machineMaintenanceTask.getTaskCode());
        machineTaskLogService.addMachineTaskLog(machineTaskLog);
        //????????????????????????
        String description = "??????????????????????????????,??????????????????" + machineMaintenanceTask.getTaskCode();
        addMachineLog(machineMaintenanceTask.getMaintainMachineId(), MesCommonConstant.MACHINE_MAINTENANCE_TASK_ADD, description);
    }

    /**
     * ??????date1???date2?????????????????????????????????????????????
     * ??????true??????????????????false???????????????
     *
     * @param date1
     * @param date2
     * @return
     */
    public boolean isSameDate(Date date1, Date date2) {
        if (date1 == null) {
            return false;
        }
        Calendar time1 = Calendar.getInstance();
        Calendar time2 = Calendar.getInstance();
        time1.setTime(date1);
        time2.setTime(date2);
        return time1.equals(time2);
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param unit
     * @param plantime
     * @param date
     * @return
     */
    public String formatDate(String unit, Integer plantime, Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int day = instance.get(Calendar.DATE);
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        int minute = instance.get(Calendar.MINUTE);
        int second = instance.get(Calendar.SECOND);
        StringBuilder cron = new StringBuilder();
        switch (unit) {
            case MesCommonConstant.POLICY_TIME_UNIT_ONE:
                cron.append(second + " ").append("* * * * ? *");
                break;
            case MesCommonConstant.POLICY_TIME_UNIT_TWO:
                //0 0 * * * ?
                cron.append(second + " ").append(minute + " ").append("* * * ? *");
                break;
            case MesCommonConstant.POLICY_TIME_UNIT_THREE:
                cron.append(second + " ").append(minute + " ").append(hour + " ").append("* * ? *");
                break;
            case MesCommonConstant.POLICY_TIME_UNIT_FOUR:
                cron.append(second + " ").append(minute + " ").append(hour + " ").append("? * ").append(getDayOfWeek(date));
                break;
            case MesCommonConstant.POLICY_TIME_UNIT_FIVE:
                cron.append(second + " ").append(minute + " ").append(hour + " ").append(day + " ").append("* ?");
                break;
            default:
                break;
        }
        return cron.toString();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addMachineLog(String machineId, String oprateType, String description) {
        MachineLog machineLog = new MachineLog();
        machineLog.setMachineId(machineId);
        machineLog.setOprateType(oprateType);
        machineLog.setDescription(description);
        machineLogMapper.insert(machineLog);
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param date
     * @return
     */
    private String getDayOfWeek(Date date) {
        final String[] dayNames = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0) {
            dayOfWeek = 0;
        }
        return dayNames[dayOfWeek];
    }

    @Override
    public JSONArray getMachineByStationId(String stationId) {
        JSONArray data = new JSONArray();
        //???????????????????????????
        String thisStationMachineId = null;
        if (StringUtils.isNotBlank(stationId)) {
            Workstation byId = workstationMapper.selectById(stationId);
            thisStationMachineId = byId.getEquipment();
        }
        //????????????????????????
        List<String> usingMachineIdList = new ArrayList<>();
        QueryWrapper<Workstation> workstationQueryWrapper = new QueryWrapper<>();
        workstationQueryWrapper.ne("equipment", "");
        List<Workstation> workstationList = workstationMapper.selectList(workstationQueryWrapper);
        for (Workstation workstation : workstationList) {
            if (!workstation.getEquipment().equals(thisStationMachineId)) {
                usingMachineIdList.add(workstation.getEquipment());
            }
        }
        //???????????????
        List<Machine> list = this.list();
        list.forEach(machine -> {
            if (!usingMachineIdList.contains(machine.getId())) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", machine.getMachineName() + "(" + machine.getMachineCode() + ")");
                jsonObject.put("value", machine.getId());
                data.add(jsonObject);
            }
        });
        return data;
    }

    @Override
    public ResultDataVO getOeeData(MachineOeeSettingVO machineOeeSettingVO) {
        String[] sort = machineOeeSettingVO.getSortFactor().split(",");
        QueryWrapper<Machine> machineQueryWrapper = new QueryWrapper<>();
        machineQueryWrapper.orderByAsc(sort);
        List<Machine> machineList = baseMapper.selectList(machineQueryWrapper);
        List<MachineOeeDataVO> machineOeeDataVOList = new ArrayList<>();
        machineList.forEach(machine -> {
            MachineOeeDataVO machineOeeDataVO = new MachineOeeDataVO();
            this.calculationOeeData(machine, machineOeeDataVO);
            machineOeeDataVOList.add(machineOeeDataVO);
        });
        ResultDataVO resultDataVO = new ResultDataVO();
        resultDataVO.setData(machineOeeDataVOList);
        return resultDataVO;
    }

    private static final String RUNNING_STATE_NAME = "????????????";
    private static final String THEORETICAL_VELOCITY_NAME = "????????????(min)";

    /**
     * ??????OEE??????
     *
     * @param machine
     * @param machineOeeDataVO
     */
    private void calculationOeeData(Machine machine, MachineOeeDataVO machineOeeDataVO) {

        machineOeeDataVO.setMachineId(machine.getId());
        machineOeeDataVO.setMachineCode(machine.getMachineCode());
        machineOeeDataVO.setMachineName(machine.getMachineName());

        //?????????????????????????????????
        String runningState = ZeroOrOneEnum.ZERO.getStrCode();
        BigDecimal theoreticalVelocity = BigDecimal.ZERO;
        List<MachineParaVO> machineParaVOList = machineParaHeadService.queryParaDataByMachineId(machine.getId());
        for (MachineParaVO paraVO : machineParaVOList) {
            if (paraVO.getStatus().equals(ZeroOrOneEnum.ONE.getStrCode())) {
                for (MachineParaDetail detail : paraVO.getMachineParaDetailList()) {
                    if (RUNNING_STATE_NAME.equals(detail.getParaName())) {
                        runningState = detail.getOptionValue();
                    }
                    if (THEORETICAL_VELOCITY_NAME.equals(detail.getParaName())) {
                        theoreticalVelocity = detail.getEqualValue();
                    }
                }
            }
        }
        machineOeeDataVO.setRunningState(runningState);

        //??????????????????
        Date today = DateUtil.parse(DateUtil.today()).toJdkDate();
        DateTime tomorrow = DateUtil.tomorrow();
        QueryWrapper<MachineStopTime> machineStopTimeQueryWrapper = new QueryWrapper<>();
        machineStopTimeQueryWrapper.ge("start_time", today);
        machineStopTimeQueryWrapper.le("start_time", tomorrow);
        machineStopTimeQueryWrapper.eq("machine_id", machine.getId());
        machineStopTimeQueryWrapper.orderByAsc("start_time");
        List<MachineStopTime> machineStopTimeList = machineStopTimeMapper.selectList(machineStopTimeQueryWrapper);

        //?????????????????????????????????????????????
        long stopTime = addStopTime(machineOeeDataVO, today, tomorrow, machineStopTimeList);

        //?????????
        long totalTime = System.currentTimeMillis() - today.getTime();
        machineOeeDataVO.setTotalTime(totalTime);
        //????????????
        if (runningState.equals(ZeroOrOneEnum.ZERO.getStrCode())) {
            machineOeeDataVO.setRunningTime(0L);
        } else {
            machineOeeDataVO.setRunningTime(totalTime - stopTime);
        }

        //???????????????
        BigDecimal timeRate = new BigDecimal(totalTime - stopTime).divide(new BigDecimal(totalTime), 6, BigDecimal.ROUND_HALF_UP);
        machineOeeDataVO.setTimeRate(timeRate);

        //????????????:????????????????????????????????????
        this.addRealYieldAndUnqualifiedYield(machine, machineOeeDataVO, today, tomorrow);

        //????????????,?????????????????????????????????
        machineOeeDataVO.setTheoreticalYield(theoreticalVelocity.multiply(new BigDecimal(totalTime - stopTime)).divide(new BigDecimal(1000 * 60), 6, BigDecimal.ROUND_HALF_UP));

        //???????????????
        if (machineOeeDataVO.getRealYield().compareTo(BigDecimal.ZERO) == 0) {
            machineOeeDataVO.setPerformanceRate(BigDecimal.ZERO);
        } else if (machineOeeDataVO.getTheoreticalYield().compareTo(BigDecimal.ZERO) == 0) {
            machineOeeDataVO.setPerformanceRate(BigDecimal.ONE);
        } else {
            machineOeeDataVO.setPerformanceRate(machineOeeDataVO.getRealYield().divide(machineOeeDataVO.getTheoreticalYield(), 6, BigDecimal.ROUND_HALF_UP));
        }

        //?????????
        if (machineOeeDataVO.getRealYield().compareTo(BigDecimal.ZERO) == 0) {
            machineOeeDataVO.setQualifiedRate(BigDecimal.ZERO);
        } else {
            machineOeeDataVO.setQualifiedRate((machineOeeDataVO.getRealYield().subtract(machineOeeDataVO.getUnqualifiedYield())).divide(machineOeeDataVO.getRealYield(), 6, BigDecimal.ROUND_HALF_UP));
        }

        //????????????
        if (machineOeeDataVO.getRealYield().compareTo(BigDecimal.ZERO) == 0) {
            machineOeeDataVO.setUnqualifiedRate(BigDecimal.ZERO);
        } else {
            machineOeeDataVO.setUnqualifiedRate(machineOeeDataVO.getUnqualifiedYield().divide(machineOeeDataVO.getRealYield(), 6, BigDecimal.ROUND_HALF_UP));
        }

        //oee
        BigDecimal oee = machineOeeDataVO.getTimeRate().multiply(machineOeeDataVO.getPerformanceRate()
                .multiply(machineOeeDataVO.getQualifiedRate()))
                .multiply(new BigDecimal(100))
                .setScale(1, BigDecimal.ROUND_HALF_UP);
        machineOeeDataVO.setOee(oee);

        //???????????????????????????????????????
        this.addPicture(machine, machineOeeDataVO);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param machine
     * @param machineOeeDataVO
     */
    private void addPicture(Machine machine, MachineOeeDataVO machineOeeDataVO) {
        //????????????????????????
        QueryWrapper<SysDocument> runningQueryWrapper = new QueryWrapper<>();
        runningQueryWrapper.eq("related_id", machine.getId());
        runningQueryWrapper.eq("related_type", "machine");
        runningQueryWrapper.like("document_name", "?????????????????????");
        List<SysDocument> runningFileList = sysDocumentService.list(runningQueryWrapper);
        if (!CommonUtil.isEmptyOrNull(runningFileList)) {
            machineOeeDataVO.setRunningPicture(runningFileList.get(0).getDocumentLocation());
        }

        //????????????????????????
        QueryWrapper<SysDocument> stopQueryWrapper = new QueryWrapper<>();
        stopQueryWrapper.eq("related_id", machine.getId());
        stopQueryWrapper.eq("related_type", "machine");
        stopQueryWrapper.like("document_name", "?????????????????????");
        List<SysDocument> stopFileList = sysDocumentService.list(stopQueryWrapper);
        if (!CommonUtil.isEmptyOrNull(stopFileList)) {
            machineOeeDataVO.setStopPicture(stopFileList.get(0).getDocumentLocation());
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param machineOeeDataVO
     * @param today
     * @param tomorrow
     * @param machineStopTimeList
     * @return
     */
    private long addStopTime(MachineOeeDataVO machineOeeDataVO, Date today, DateTime tomorrow, List<MachineStopTime> machineStopTimeList) {
        long stopTime = 0L;
        List<MachineStopTime> runningTimeList = new ArrayList<>();
        //???????????????
        MachineStopTime firstRunningTime = new MachineStopTime();
        firstRunningTime.setStopType(ZeroOrOneEnum.ONE.getStrCode());
        firstRunningTime.setStartTime(today);
        if (CommonUtil.isEmptyOrNull(machineStopTimeList)) {
            firstRunningTime.setEndTime(new Date());
        } else {
            if (machineStopTimeList.get(0).getStartTime() != today) {
                firstRunningTime.setEndTime(machineStopTimeList.get(0).getStartTime());
            }
        }
        runningTimeList.add(firstRunningTime);

        for (MachineStopTime machineStopTime : machineStopTimeList) {
            //??????????????????
            Date startTime = machineStopTime.getStartTime();
            Date endTime = machineStopTime.getEndTime();
            machineStopTime.setStopType(ZeroOrOneEnum.ZERO.getStrCode());
            if (endTime.compareTo(tomorrow) >= 0) {
                endTime = tomorrow;
                machineStopTime.setEndTime(tomorrow);
            }
            stopTime = stopTime + endTime.getTime() - startTime.getTime();

            //???????????????????????????????????????????????????????????????????????????
            MachineStopTime runningTime = new MachineStopTime();
            runningTime.setStopType(ZeroOrOneEnum.ONE.getStrCode());
            runningTime.setStartTime(endTime);
            runningTimeList.add(runningTime);
        }
        //???????????????????????????
        runningTimeList.forEach(runningTime -> {
            if (runningTime.getEndTime() == null) {
                machineStopTimeList.forEach(machineStopTime -> {
                    if (machineStopTime.getStartTime().compareTo(runningTime.getStartTime()) > 0) {
                        runningTime.setEndTime(machineStopTime.getStartTime());
                    }
                });
                if (runningTime.getEndTime() == null) {
                    runningTime.setEndTime(new Date());
                }
            }
        });

        machineStopTimeList.addAll(runningTimeList);
        Collections.sort(machineStopTimeList);
        machineOeeDataVO.setMachineStopTimeList(machineStopTimeList);
        machineOeeDataVO.setStopTime(stopTime);
        return stopTime;
    }

    /**
     * ????????????
     *
     * @param machine
     * @param machineOeeDataVO
     * @param today
     * @param tomorrow
     */
    private void addRealYieldAndUnqualifiedYield(Machine machine, MachineOeeDataVO machineOeeDataVO, Date today, DateTime tomorrow) {
        BigDecimal realYield = BigDecimal.ZERO;
        BigDecimal unqualifiedYield = BigDecimal.ZERO;
        //?????????????????????
        List<String> stationIds = new ArrayList<>();
        QueryWrapper<Workstation> workstationQueryWrapper = new QueryWrapper<>();
        workstationQueryWrapper.eq("equipment", machine.getId());
        workstationQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<Workstation> workstationList = workstationService.list(workstationQueryWrapper);
        if (!CommonUtil.isEmptyOrNull(workstationList)) {
            workstationList.forEach(workstation -> stationIds.add(workstation.getId()));
            //???????????????????????????
            QueryWrapper<WorkProduceRecord> workProduceRecordQueryWrapper = new QueryWrapper<>();
            workProduceRecordQueryWrapper.in("station_id", stationIds);
            workProduceRecordQueryWrapper.ge("produce_date", today);
            workProduceRecordQueryWrapper.le("produce_date", tomorrow);
            workProduceRecordQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
            workProduceRecordQueryWrapper.orderByDesc("produce_date");
            List<WorkProduceRecord> workProduceRecordList = workProduceRecordMapper.selectList(workProduceRecordQueryWrapper);
            if (!CommonUtil.isEmptyOrNull(workProduceRecordList)) {
                WorkProduceRecord latestWorkProduceRecord = workProduceRecordList.get(0);
                for (WorkProduceRecord workProduceRecord : workProduceRecordList) {
                    if (latestWorkProduceRecord.getItemId().equals(workProduceRecord.getItemId())) {
                        realYield = realYield.add(workProduceRecord.getSubmitQty());
                        if (workProduceRecord.getQcStatus().equals(ItemQcStatusEnum.UNQUALIFIED.getValue())) {
                            unqualifiedYield = unqualifiedYield.add(workProduceRecord.getSubmitQty());
                        }
                    }
                }
            }
        }
        machineOeeDataVO.setRealYield(realYield);
        machineOeeDataVO.setUnqualifiedYield(unqualifiedYield);
    }

}
