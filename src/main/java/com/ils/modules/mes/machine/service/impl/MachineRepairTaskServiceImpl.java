package com.ils.modules.mes.machine.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.machine.entity.MachineType;
import com.ils.modules.mes.base.machine.service.MachineTypeService;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.MachineTaskOprateTypeEnum;
import com.ils.modules.mes.enums.MachineTaskTypeEnum;
import com.ils.modules.mes.enums.RepairTaskAheadType;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.machine.entity.Machine;
import com.ils.modules.mes.machine.entity.MachineRepairTask;
import com.ils.modules.mes.machine.entity.MachineRepairTaskEmployee;
import com.ils.modules.mes.machine.entity.MachineTaskLog;
import com.ils.modules.mes.machine.job.RepairTaskSingleJob;
import com.ils.modules.mes.machine.mapper.MachineMapper;
import com.ils.modules.mes.machine.mapper.MachineRepairTaskEmployeeMapper;
import com.ils.modules.mes.machine.mapper.MachineRepairTaskMapper;
import com.ils.modules.mes.machine.mapper.MachineTaskLogMapper;
import com.ils.modules.mes.machine.service.MachineRepairTaskService;
import com.ils.modules.mes.machine.service.MachineService;
import com.ils.modules.mes.machine.service.MachineTaskLogService;
import com.ils.modules.mes.machine.vo.MachineRepairVO;
import com.ils.modules.mes.machine.vo.RepairTaskDetailVO;
import com.ils.modules.mes.machine.vo.RepairTaskWithEmployeeVO;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.message.service.MsgHandleServer;
import com.ils.modules.message.vo.MsgParamsVO;
import com.ils.modules.quartz.entity.QuartzJob;
import com.ils.modules.quartz.service.IQuartzJobService;
import com.ils.modules.system.entity.User;
import com.ils.modules.system.service.CodeGeneratorService;
import com.ils.modules.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.ils.modules.mes.constants.MesCommonConstant.*;

/**
 * @Description: 维修任务
 * @Author: Hhuangtao
 * @Date: 2020-11-17
 * @Version: V1.0
 */
@Service
public class MachineRepairTaskServiceImpl extends ServiceImpl<MachineRepairTaskMapper, MachineRepairTask> implements MachineRepairTaskService {

    /**
     * 设备维修状态
     */
    private static final String MACHINE_REPAIR_STATUS = "1";
    /**
     * 状态为未开始 1
     */
    private static final String TASK_STATUS_ONE = "1";
    /**
     * 状态为开始 2
     */
    private static final String TASK_STATUS_TWO = "2";
    /**
     * 状态为暂停 3
     */
    private static final String TASK_STATS_THREE = "3";
    /**
     * 状态为结束4
     */
    private static final String TASK_STATUS_FOUR = "4";

    public static final String REPAIR_TASK_JOB = "com.ils.modules.mes.machine.job.RepairTaskSingleJob";
    public static final String ID = "RepairTaskSingleJob";

    @Autowired
    private MachineRepairTaskMapper machineRepairTaskMapper;
    @Autowired
    private MachineRepairTaskEmployeeMapper machineRepairTaskEmployeeMapper;
    @Autowired
    private UserService sysUserService;
    @Autowired
    private MachineMapper machineMapper;
    @Autowired
    private MachineTaskLogMapper machineTaskLogMapper;
    @Autowired
    private CodeGeneratorService codeGeneratorService;
    @Autowired
    private MachineTaskLogService machineTaskLogService;
    @Autowired
    private MachineService machineService;
    @Autowired
    private MachineTypeService machineTypeService;
    @Autowired
    private MsgHandleServer msgHandleServer;
    @Autowired
    private IQuartzJobService quartzJobService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public MachineRepairTask saveMachineRepairTask(RepairTaskWithEmployeeVO repairTaskWithEmployeeVO) {
        MachineRepairTask machineRepairTask = new MachineRepairTask();
        BeanUtils.copyProperties(repairTaskWithEmployeeVO, machineRepairTask);
        String taskCode = codeGeneratorService.getNextCode(CommonUtil.getTenantId(), MesCommonConstant.MACHINE_REPAIR_TASK_CODE, machineRepairTask);
        machineRepairTask.setTaskCode(taskCode);
        machineRepairTask.setStatus(MACHINE_REPAIR_STATUS);
        Machine machine = machineService.getById(machineRepairTask.getRepairMachineId());
        MachineType machineType = machineTypeService.getById(machine.getMachineTypeId());
        machineRepairTask.setScan(machineType.getScan());
        baseMapper.insert(machineRepairTask);
        String employeeIds = repairTaskWithEmployeeVO.getRapairTaskEmployeeId();
        if (StringUtils.isNotEmpty(employeeIds)) {
            String[] employees = employeeIds.split(",");
            for (String employee : employees) {
                MachineRepairTaskEmployee repairTaskEmployee = new MachineRepairTaskEmployee();
                repairTaskEmployee.setEmployeeId(employee);
                User user = sysUserService.getById(employee);
                repairTaskEmployee.setEmployeeName(user.getRealname());
                repairTaskEmployee.setEmployeeCode(user.getOrgCode());
                repairTaskEmployee.setRepairTaskId(machineRepairTask.getId());
                machineRepairTaskEmployeeMapper.insert(repairTaskEmployee);
            }
        }
        //新增任务日志
        MachineTaskLog machineTaskLog = new MachineTaskLog();
        machineTaskLog.setTaskId(machineRepairTask.getId());
        machineTaskLog.setOprateType(MachineTaskTypeEnum.REPAIR_TASKS.getValue());
        machineTaskLog.setTaskType(MachineTaskTypeEnum.REPAIR_TASKS.getValue());
        machineTaskLog.setDescription(CommonUtil.getLoginUser().getUsername() + "||" + MachineTaskOprateTypeEnum.CREATE_TASK.getDesc() + "||" + machineRepairTask.getTaskCode());
        machineTaskLogService.addMachineTaskLog(machineTaskLog);

        //给计划执行人发消息通知他们
        if (StringUtils.isNotEmpty(employeeIds)) {
            JSONObject task = new JSONObject();
            //设置消息参数
            task.put("taskName", machineRepairTask.getTaskName());
            task.put("machineName", machine.getMachineName());
            MsgParamsVO msgParamsVO = new MsgParamsVO(Arrays.asList(employeeIds.split(",")), null, null, machineRepairTask.getCreateBy(), task);
            msgHandleServer.sendMsg(MesCommonConstant.REPAIR_ADD_MODULE, MesCommonConstant.REPAIR_ADD_OPERACTION, msgParamsVO);
        }

        String description = "创建维修任务,任务编号为：" + machineRepairTask.getTaskCode();
        machineService.addMachineLog(machineRepairTask.getRepairMachineId(), MACHINE_REPAIR_TASK_ADD, description);

        //添加定时任务
        if (null != machineRepairTask.getAheadType() && !RepairTaskAheadType.MAINTENANCE.getValue().equals(machineRepairTask.getAheadType())) {
            Date date = RepairTaskSingleJob.formatDate(machineRepairTask.getAheadType(), machineRepairTask.getPlanStartTime());
            if (null != date && date.compareTo(new Date()) > 0) {
                this.createMaintenanceQuartzJob(machineRepairTask.getId(), machineRepairTask.getTenantId());
            }
        }
        return machineRepairTask;
    }

    /**
     * 创建定时任务
     *
     * @param taskId
     */
    public void createMaintenanceQuartzJob(String taskId, String tenantId) {
        QuartzJob quartzJob = quartzJobService.getById(ID);
        if (null == quartzJob) {
            String baseCron = "0 */1 * * * ? *";
            quartzJob = new QuartzJob();
            quartzJob.setId(ID);
            quartzJob.setCronExpression(baseCron);
            quartzJob.setJobNameKey(REPAIR_TASK_JOB);
            quartzJob.setJobClassName(REPAIR_TASK_JOB);
            quartzJob.setStatus(MesCommonConstant.STATUS_NORMAL);
            Map<String, String> taskIdMap = new HashMap<>(1);
            taskIdMap.put(taskId, tenantId);
            quartzJob.setParameter(JSONObject.toJSONString(taskIdMap));
            quartzJobService.saveAndScheduleJob(quartzJob);
        } else {
            String parameter = quartzJob.getParameter();
            Map<String, String> taskIdMap = JSONObject.parseObject(parameter, new TypeReference<Map<String, String>>() {
            });
            if (taskIdMap == null || taskIdMap.keySet().isEmpty()) {
                taskIdMap = new HashMap<>(1);
            }
            taskIdMap.put(taskId, tenantId);
            quartzJob.setParameter(JSONObject.toJSONString(taskIdMap));
            try {
                quartzJobService.editAndScheduleJob(quartzJob);
            } catch (SchedulerException e) {
                throw new ILSBootException("执行任务失败");
            }
        }
    }

    @Override
    public List<DictModel> queryDictMachineName() {
        return machineMapper.queryMachineNameAndMachineId();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addFaultReason(String faultReasonId, String taskId) {
        MachineRepairTask machineRepairTask = baseMapper.selectById(taskId);
        machineRepairTask.setFaultReason(faultReasonId);
        baseMapper.updateById(machineRepairTask);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void changeRealExcuter(String id, String realExcuters) {
        MachineRepairTask machineRepairTask = baseMapper.selectById(id);
        machineRepairTask.setRealExcuter(realExcuters);
        baseMapper.updateById(machineRepairTask);
        String description = "执行维修任务,任务编号为：" + machineRepairTask.getTaskCode();
        machineService.addMachineLog(machineRepairTask.getRepairMachineId(), MACHINE_REPAIR_TASK_EXECUTE, description);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineRepairTask(RepairTaskWithEmployeeVO repairTaskWithEmployeeVO) {
        MachineRepairTask machineRepairTask = new MachineRepairTask();
        BeanUtils.copyProperties(repairTaskWithEmployeeVO, machineRepairTask);
        //更新计划执行人
        QueryWrapper<MachineRepairTaskEmployee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("repair_task_id", repairTaskWithEmployeeVO.getId());
        machineRepairTaskEmployeeMapper.delete(employeeQueryWrapper);
        String employeeIds = repairTaskWithEmployeeVO.getRapairTaskEmployeeId();
        if (!employeeIds.isEmpty()) {
            String[] employees = employeeIds.split(",");
            for (String employee : employees) {
                MachineRepairTaskEmployee repairTaskEmployee = new MachineRepairTaskEmployee();
                repairTaskEmployee.setEmployeeId(employee);
                User user = sysUserService.getById(employee);
                repairTaskEmployee.setEmployeeName(user.getRealname());
                repairTaskEmployee.setEmployeeCode(user.getOrgCode());
                repairTaskEmployee.setRepairTaskId(machineRepairTask.getId());
                machineRepairTaskEmployeeMapper.insert(repairTaskEmployee);
            }
        }
        baseMapper.updateById(machineRepairTask);
        //添加定时任务
        if (null != machineRepairTask.getAheadType() && !RepairTaskAheadType.MAINTENANCE.getValue().equals(machineRepairTask.getAheadType())) {
            Date date = RepairTaskSingleJob.formatDate(machineRepairTask.getAheadType(), machineRepairTask.getPlanStartTime());
            if (null != date && date.compareTo(new Date()) > 0) {
                this.createMaintenanceQuartzJob(machineRepairTask.getId(), machineRepairTask.getTenantId());
            }
        }
    }

    @Override
    public IPage<MachineRepairVO> listPage(String userId, Page<MachineRepairTask> page, QueryWrapper<MachineRepairTask> queryWrapper) {
        return machineRepairTaskMapper.listPage(userId, page, queryWrapper);
    }

    @Override
    public RepairTaskDetailVO queryDetailById(String id) {
        RepairTaskDetailVO repairTaskDetailVO = baseMapper.queryRepairTaskDetailsByTaskId(id);
        QueryWrapper<MachineTaskLog> machineTaskLogQueryWrapper = new QueryWrapper<>();
        machineTaskLogQueryWrapper.eq("task_id", id);
        machineTaskLogQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachineTaskLog> machineTaskLogList = machineTaskLogMapper.selectList(machineTaskLogQueryWrapper);
        repairTaskDetailVO.setMachineTaskLogList(machineTaskLogList);
        return repairTaskDetailVO;
    }

    @Override
    public RepairTaskWithEmployeeVO queryTaskWithEmployeesById(String id) {
        MachineRepairTask machineRepairTask = baseMapper.selectById(id);
        RepairTaskWithEmployeeVO repairTaskWithEmployeeVO = new RepairTaskWithEmployeeVO();
        BeanUtils.copyProperties(machineRepairTask, repairTaskWithEmployeeVO);
        //查询维修人员
        QueryWrapper<MachineRepairTaskEmployee> repairTaskEmployeeQueryWrapper = new QueryWrapper<>();
        repairTaskEmployeeQueryWrapper.eq("repair_task_id", machineRepairTask.getId());
        repairTaskEmployeeQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachineRepairTaskEmployee> machineRepairTaskEmployees = machineRepairTaskEmployeeMapper.selectList(repairTaskEmployeeQueryWrapper);
        StringBuilder employeesIds = new StringBuilder();
        for (MachineRepairTaskEmployee repairTaskEmployee : machineRepairTaskEmployees) {
            String employeeId = repairTaskEmployee.getEmployeeId();
            employeesIds.append(employeeId).append(",");
        }
        if (!machineRepairTaskEmployees.isEmpty()) {
            String employeesId = employeesIds.substring(0, employeesIds.length() - 1);
            repairTaskWithEmployeeVO.setRapairTaskEmployeeId(employeesId);
        }
        return repairTaskWithEmployeeVO;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void changeStatus(String id, String status, String userId, String qrcode) {
        MachineRepairTask machineRepairTask = baseMapper.selectById(id);
        String oldStatus = machineRepairTask.getStatus();
        if (TASK_STATUS_TWO.equals(status)) {
            if (TASK_STATUS_ONE.equals(oldStatus)) {
                //状态为未开始到开始时，校验二维码
                if (StringUtils.isNoneBlank(qrcode)) {
                    checkMachineByQrcode(qrcode, id);
                }
                machineRepairTask.setRealExcuter(userId);
                machineRepairTask.setExcuteStartTime(new Date());
            } else if (TASK_STATS_THREE.equals(oldStatus)) {

            } else {
                throw new ILSBootException("维修任务状态为未开始才能进行执行操作");
            }

        }
        if (TASK_STATUS_FOUR.equals(status)) {
            if (TASK_STATS_THREE.equals(oldStatus) || TASK_STATUS_TWO.equals(oldStatus)) {
                machineRepairTask.setExcuteEndTime(new Date());
            } else {
                throw new ILSBootException("维修任务状态为执行中和暂停中才能进行结束操作");
            }

        }
        machineRepairTask.setStatus(status);
        baseMapper.updateById(machineRepairTask);
        //新增一条日志到machinetasklog中去
        machineTaskLogService.saveMachineTaskLog(oldStatus, status, id, machineRepairTask.getTaskCode(), MachineTaskTypeEnum.REPAIR_TASKS.getValue());
    }

    @Override
    public String queryTemplateIdByMachineId(String machineId) {
        return baseMapper.queryTemplateIdByMachineId(machineId);
    }

    @Override
    public List<DictModel> queryUserInfoWithNameAndId() {
        return baseMapper.queryUserInfoWithNameAndId();
    }

    @Override
    public List<String> queryRepairMachineTask() {
        return baseMapper.queryRepairMachineTask();
    }

    /**
     * 判断设备是否需要扫描确认，如果需要判断二维码是否正确
     *
     * @param qrcode
     * @param taskId
     * @return
     */
    private void checkMachineByQrcode(String qrcode, String taskId) {
        QueryWrapper<Machine> machineQueryWrapper = new QueryWrapper<>();
        machineQueryWrapper.eq("qr_code", qrcode);
        Machine machine = machineService.getOne(machineQueryWrapper);
        RepairTaskDetailVO repairTaskDetailVO = this.queryDetailById(taskId);
        if (ZeroOrOneEnum.ONE.getStrCode().equals(repairTaskDetailVO.getScan())) {
            if (null == machine || !machine.getId().equals(repairTaskDetailVO.getRepairMachineId())) {
                throw new ILSBootException("二维码查询异常，请检查二维码是否与该设备对应。");
            }
        }
    }
}
