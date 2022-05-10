package com.ils.modules.mes.machine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.factory.mapper.WorkstationMapper;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.MachineTaskTypeEnum;
import com.ils.modules.mes.enums.TaskStatusEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.machine.entity.MachineMaintenanceTask;
import com.ils.modules.mes.machine.entity.MachineMaintenanceTaskEmployee;
import com.ils.modules.mes.machine.entity.MachineTaskLog;
import com.ils.modules.mes.machine.mapper.*;
import com.ils.modules.mes.machine.service.MachineMaintenanceTaskService;
import com.ils.modules.mes.machine.service.MachineService;
import com.ils.modules.mes.machine.service.MachineTaskLogService;
import com.ils.modules.mes.machine.vo.MachineMaintenanceTaskVO;
import com.ils.modules.mes.machine.vo.MaintenancePageListWithPhoneVO;
import com.ils.modules.mes.machine.vo.MaintenanceTaskDetailVO;
import com.ils.modules.mes.machine.vo.MaintenanceTaskVO;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.entity.User;
import com.ils.modules.system.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.ils.modules.mes.constants.MesCommonConstant.MACHINE_MAINTENANCE_TASK_ADD;
import static com.ils.modules.mes.constants.MesCommonConstant.MACHINE_MAINTENANCE_TASK_EXECUTE;


/**
 * @Description: 维保任务
 * @Author: Hhuangtao
 * @Date: 2020-11-17
 * @Version: V1.0
 */
@Service
public class MachineMaintenanceTaskServiceImpl extends ServiceImpl<MachineMaintenanceTaskMapper, MachineMaintenanceTask> implements MachineMaintenanceTaskService {

    @Autowired
    private MachineTaskLogMapper machineTaskLogMapper;
    @Autowired
    private MachineMaintenanceTaskEmployeeMapper machineMaintenanceTaskEmployeeMapper;
    @Autowired
    private UserService sysUserService;
    @Autowired
    private MachineTaskLogService machineTaskLogService;
    @Autowired
    private MachineService machineService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineMaintenanceTask(MachineMaintenanceTask machineMaintenanceTask) {
        baseMapper.insert(machineMaintenanceTask);
        String description = "创建维保任务,任务编号为：" + machineMaintenanceTask.getTaskCode();
        machineService.addMachineLog(machineMaintenanceTask.getMaintainMachineId(), MACHINE_MAINTENANCE_TASK_ADD, description);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineMaintenanceTask(MachineMaintenanceTaskVO machineMaintenanceTaskVO) {
        MachineMaintenanceTask machineMaintenanceTask = new MachineMaintenanceTask();
        BeanUtils.copyProperties(machineMaintenanceTaskVO, machineMaintenanceTask);
        //跟新计划执行人
        QueryWrapper<MachineMaintenanceTaskEmployee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("repair_task_id", machineMaintenanceTask.getId());
        machineMaintenanceTaskEmployeeMapper.delete(employeeQueryWrapper);
        String planExcuter = machineMaintenanceTaskVO.getPlanExcuter();
        if (planExcuter != null) {
            String[] employeeIds = planExcuter.split(",");
            for (String employeeId : employeeIds) {
                MachineMaintenanceTaskEmployee employee = new MachineMaintenanceTaskEmployee();
                employee.setEmployeeId(employeeId);
                User user = sysUserService.getById(employeeId);
                employee.setEmployeeName(user.getRealname());
                employee.setEmployeeCode(user.getOrgCode());
                employee.setRepairTaskId(machineMaintenanceTask.getId());
                machineMaintenanceTaskEmployeeMapper.insert(employee);
            }
        }
        baseMapper.updateById(machineMaintenanceTask);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMachineMaintenanceTask(String id) {
        baseMapper.deleteById(id);
        QueryWrapper<MachineMaintenanceTaskEmployee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("repair_task_id", id);
        machineMaintenanceTaskEmployeeMapper.delete(employeeQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMachineMaintenanceTask(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public IPage<MaintenanceTaskVO> listPage(String userId, Page<MaintenanceTaskVO> page, QueryWrapper<MaintenanceTaskVO> queryWrapper) {
        IPage<MaintenanceTaskVO> pageMaintenanceTaskVO = baseMapper.listPage(userId, page, queryWrapper);
        return pageMaintenanceTaskVO;
    }

    @Override
    public MaintenanceTaskDetailVO queryMaintenanceTaskDetailById(String id, String type) {
        MaintenanceTaskDetailVO maintenanceTaskDetailVO = baseMapper.selectMaintenanceDetailById(id, type);
        QueryWrapper<MachineTaskLog> machineTaskLogQueryWrapper = new QueryWrapper<>();
        machineTaskLogQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        machineTaskLogQueryWrapper.eq("task_id", id);
        List<MachineTaskLog> machineTaskLogList = machineTaskLogMapper.selectList(machineTaskLogQueryWrapper);
        maintenanceTaskDetailVO.setMachineTaskLogList(machineTaskLogList);
        maintenanceTaskDetailVO.setCycle(jointPlanTime(maintenanceTaskDetailVO.getQty(), maintenanceTaskDetailVO.getUnit()));
        maintenanceTaskDetailVO.setPlanWorkTime(jointPlanTime(maintenanceTaskDetailVO.getPlanTime(), maintenanceTaskDetailVO.getTimeUnit()));
        return maintenanceTaskDetailVO;
    }

    @Override
    public MaintenancePageListWithPhoneVO queryMaintenanceTaskById(String id) {
        MaintenancePageListWithPhoneVO maintenancePageListWithPhoneVO = baseMapper.queryMaintenanceTaskById(id);
        return maintenancePageListWithPhoneVO;
    }


    @Override
    public IPage<MaintenancePageListWithPhoneVO> listPageWithPhone(Page<MaintenancePageListWithPhoneVO> page, QueryWrapper<MaintenancePageListWithPhoneVO> queryWrapper) {
        IPage<MaintenancePageListWithPhoneVO> maintenancePageListWithPhoneVO = baseMapper.listPageWithPhone(page, CommonUtil.getLoginUser().getId(), queryWrapper);
        return maintenancePageListWithPhoneVO;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void changeStatus(String id, String status, String userId) {
        MachineMaintenanceTask machineMaintenanceTask = baseMapper.selectById(id);
        if (TaskStatusEnum.NOT_STARTED.getValue().equals(machineMaintenanceTask.getStatus()) && TaskStatusEnum.EXECUTING.getValue().equals(status)) {
            machineMaintenanceTask.setRealExcuter(userId);
            machineMaintenanceTask.setRealStartTime(new Date());
            String description = "执行维保任务,任务编号为：" + machineMaintenanceTask.getTaskCode();
            machineService.addMachineLog(machineMaintenanceTask.getMaintainMachineId(), MACHINE_MAINTENANCE_TASK_EXECUTE, description);
        }
        if (TaskStatusEnum.COMPLETE.getValue().equals(status)) {
            machineMaintenanceTask.setRealEndTime(new Date());
        }
        String taskType = "";
        if (MesCommonConstant.MAINTENANECE_TASK_TYPE_SPOT_CHECK.equals(machineMaintenanceTask.getMaintenanceType())) {
            taskType = MachineTaskTypeEnum.MAINTENANCE_TASKS.getValue();
        } else {
            taskType = MachineTaskTypeEnum.CHECK_TASKS.getValue();
        }
        machineTaskLogService.saveMachineTaskLog(machineMaintenanceTask.getStatus(), status, id, machineMaintenanceTask.getTaskCode(), taskType);
        machineMaintenanceTask.setStatus(status);
        baseMapper.updateById(machineMaintenanceTask);
    }

    private String jointPlanTime(Integer qty, String unit) {
        StringBuilder planTime = new StringBuilder();
        switch (unit) {
            case MesCommonConstant.POLICY_TIME_UNIT_ONE:
                planTime.append(MesCommonConstant.MAINTENANECE_TASK_WORD_MEI).append(qty).append(MesCommonConstant.MAINTENANECE_TASK_PLAN_TIME_ONE);
                break;
            case MesCommonConstant.POLICY_TIME_UNIT_TWO:
                planTime.append(MesCommonConstant.MAINTENANECE_TASK_WORD_MEI).append(qty).append(MesCommonConstant.MAINTENANECE_TASK_PLAN_TIME_TWO);
                break;
            case MesCommonConstant.POLICY_TIME_UNIT_THREE:
                planTime.append(MesCommonConstant.MAINTENANECE_TASK_WORD_MEI).append(qty).append(MesCommonConstant.MAINTENANECE_TASK_PLAN_TIME_THREE);
                break;
            case MesCommonConstant.POLICY_TIME_UNIT_FOUR:
                planTime.append(MesCommonConstant.MAINTENANECE_TASK_WORD_MEI).append(qty).append(MesCommonConstant.MAINTENANECE_TASK_PLAN_TIME_FOUR);
                break;
            case MesCommonConstant.POLICY_TIME_UNIT_FIVE:
                planTime.append(MesCommonConstant.MAINTENANECE_TASK_WORD_MEI).append(qty).append(MesCommonConstant.MAINTENANECE_TASK_PLAN_TIME_FIVE);
                break;
            default:
                break;
        }
        return planTime.toString();
    }

    @Override
    public List<String> queryWaitMaintenanMachine(String taskType) {
        return baseMapper.queryWaitMaintenanceTask(taskType);
    }
}
