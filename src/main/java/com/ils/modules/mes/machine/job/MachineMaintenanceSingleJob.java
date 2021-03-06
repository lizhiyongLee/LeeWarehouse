package com.ils.modules.mes.machine.job;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.system.util.TenantContext;
import com.ils.common.util.SpringContextUtils;
import com.ils.modules.mes.base.factory.entity.Shift;
import com.ils.modules.mes.base.factory.service.ShiftService;
import com.ils.modules.mes.base.factory.service.WorkCalendarService;
import com.ils.modules.mes.base.factory.vo.WorkCalendarParamsVO;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.CalendarTypeEnum;
import com.ils.modules.mes.enums.MachineMaintenanceStatusEnum;
import com.ils.modules.mes.enums.MachinePolicyRuleEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.machine.entity.*;
import com.ils.modules.mes.machine.service.MachineDataService;
import com.ils.modules.mes.machine.service.MachineMaintenanceTaskService;
import com.ils.modules.mes.machine.service.MachinePolicyService;
import com.ils.modules.mes.machine.service.MachineService;
import com.ils.modules.mes.machine.service.impl.MachineServiceImpl;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.quartz.entity.QuartzJob;
import com.ils.modules.quartz.job.BaseJob;
import com.ils.modules.quartz.service.IQuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author lishaojie
 * @description
 * @date 2021/8/23 15:15
 */
@Slf4j
public class MachineMaintenanceSingleJob extends BaseJob {
    @Autowired
    private MachinePolicyService machinePolicyService;
    @Autowired
    private MachineService machineService;
    @Autowired
    private IQuartzJobService quartzJobService;
    @Autowired
    private WorkCalendarService workCalendarService;
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private MachineDataService machineDataService;
    @Autowired
    private MachineMaintenanceTaskService machineMaintenanceTaskService;

    public static final String JOB_ID = "MachineMaintenanceSingleJob";

    /**
     * id??????????????????????????????????????????
     * ?????????????????????????????????????????????
     * ????????????????????????????????????????????????????????????????????????????????????????????????
     */
    private Map<String, Date> taskTimeMap;


    @Override
    public void executeBussion(String parameter) {
        //???????????????id
        List<String> policyIds = JSONArray.parseArray(parameter, String.class);

        if (!CommonUtil.isEmptyOrNull(policyIds)) {
            //??????
            taskTimeMap = new HashMap<>(8);
            //?????????????????????????????????????????????taskTimeMap
            policyIds.forEach(id -> {
                        MachinePolicy machinePolicy = machinePolicyService.getById(id);
                        TenantContext.setTenant(machinePolicy.getTenantId());
                        //???????????????????????????
                        if (MachinePolicyRuleEnum.FIXED_CYCLE.getValue().equals(machinePolicy.getPolicyRule()) || MachinePolicyRuleEnum.FLOATING_CYCLE.getValue().equals(machinePolicy.getPolicyRule())) {
                            List<Shift> shiftList = new ArrayList<>();
                            if (MachinePolicyRuleEnum.FLOATING_CYCLE.getValue().equals(machinePolicy.getPolicyRule())) {
                                //???????????????????????????????????????????????????
                                shiftList = this.getShiftList(machinePolicy.getTenantId());
                            }
                            this.countTime(id, machinePolicy, shiftList);
                        }
                        //???????????????????????????
                        if (MachinePolicyRuleEnum.THE_ACCUMULATE.getValue().equals(machinePolicy.getPolicyRule()) || MachinePolicyRuleEnum.THE_FIXED.getValue().equals(machinePolicy.getPolicyRule())) {
                            QueryWrapper<MachineData> machineDataQueryWrapper = new QueryWrapper<>();
                            machineDataQueryWrapper.eq("machine_id", machinePolicy.getMachineId());
                            machineDataQueryWrapper.eq("data_name", machinePolicy.getRuleName());
                            MachineData machineData = machineDataService.getOne(machineDataQueryWrapper);
                            String dataAddress = machineData.getDataAdress();
                            //???????????????????????????????????????????????????????????????
                            if (new BigDecimal(dataAddress).compareTo(BigDecimal.valueOf(machinePolicy.getQty())) > 0) {
                                List<String> statusList = new ArrayList<>();
                                statusList.add(MachineMaintenanceStatusEnum.NOT_YET_START.getValue());
                                statusList.add(MachineMaintenanceStatusEnum.RUNNING.getValue());
                                statusList.add(MachineMaintenanceStatusEnum.STOP.getValue());
                                //?????????????????????????????????????????????????????????
                                QueryWrapper<MachineMaintenanceTask> taskQueryWrapper = new QueryWrapper<>();
                                taskQueryWrapper.eq("policy_id", id);
                                taskQueryWrapper.in("status", statusList);
                                List<MachineMaintenanceTask> taskList = machineMaintenanceTaskService.list(taskQueryWrapper);
                                if (CommonUtil.isEmptyOrNull(taskList)) {
                                    taskTimeMap.put(id, new Date());
                                    this.createTask(id);
                                }
                            }
                        }
                    }
            );
        }
        //????????????
        String updatePolicyIds = JSONObject.toJSONString(taskTimeMap.keySet());
        QuartzJob quartzJob = quartzJobService.getById(JOB_ID);
        quartzJob.setParameter(updatePolicyIds);
        quartzJobService.updateById(quartzJob);
        //???????????????????????????
        Set<String> keySet = taskTimeMap.keySet();
        if (!CommonUtil.isEmptyOrNull(keySet)) {
            keySet.forEach(this::createTask);
        }

    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @return List<Shift>
     */
    private List<Shift> getShiftList(String tenantId) {
        QueryWrapper<WorkCalendarParamsVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        queryWrapper.eq("type", CalendarTypeEnum.MACHINE.getValue());
        queryWrapper.eq("workdate", DateUtil.beginOfDay(new Date()));
        List<WorkCalendarParamsVO> lstWorkCalendarParamsVO = workCalendarService.querySingleInitData(queryWrapper, tenantId);
        if (CommonUtil.isEmptyOrNull(lstWorkCalendarParamsVO)) {
            return new ArrayList<>();
        }
        WorkCalendarParamsVO workCalendarParamsVO = lstWorkCalendarParamsVO.get(0);
        String shiftIds = workCalendarParamsVO.getShiftIds();
        QueryWrapper<Shift> shiftQueryWrapper = new QueryWrapper<>();
        shiftQueryWrapper.in("id", Arrays.asList(shiftIds.split(",")));
        return shiftService.list(shiftQueryWrapper);
    }

    /**
     * ??????????????????
     *
     * @param id
     * @param machinePolicy
     * @param shiftList
     */
    private void countTime(String id, MachinePolicy machinePolicy, List<Shift> shiftList) {
        Date now = new Date();
        now = new Date(now.getTime() - 59 * 1000);
        Machine machine = machineService.getById(machinePolicy.getMachineId());
        //???????????????????????????????????????????????????????????????????????????
        if (ZeroOrOneEnum.ZERO.getStrCode().equals(machine.getStatus()) || ZeroOrOneEnum.ZERO.getStrCode().equals(machinePolicy.getStatus()) || machinePolicy.getEndTime().before(now)) {
            taskTimeMap.remove(id);
            return;
        }
        Date startTime = machinePolicy.getExcuteBaseTime();
        Integer qty = machinePolicy.getQty();
        Date endTime = machinePolicy.getEndTime();
        //1????????????2????????????3?????????4?????????5??????
        //????????????????????????
        switch (machinePolicy.getUnit()) {
            case "1":
                while (startTime.before(endTime)) {
                    startTime.setTime(startTime.getTime() + qty * 60 * 1000);
                    if (startTime.after(now) || startTime.compareTo(now) == 0) {
                        taskTimeMap.put(id, new Date(startTime.getTime()));
                        break;
                    }
                }
                break;
            case "2":
                while (startTime.before(endTime)) {
                    startTime.setTime(startTime.getTime() + qty * 60 * 60 * 1000);
                    if (startTime.after(now) || startTime.compareTo(now) == 0) {
                        taskTimeMap.put(id, new Date(startTime.getTime()));
                        break;
                    }
                }
                break;
            case "3":
                while (startTime.before(endTime)) {
                    startTime.setTime(startTime.getTime() + qty * 24 * 60 * 60 * 1000);
                    if (startTime.after(now) || startTime.compareTo(now) == 0) {
                        taskTimeMap.put(id, new Date(startTime.getTime()));
                        break;
                    }
                }
                break;
            case "4":
                while (startTime.before(endTime)) {
                    startTime.setTime(startTime.getTime() + qty * 7 * 24 * 60 * 60 * 1000);
                    if (startTime.after(now) || startTime.compareTo(now) == 0) {
                        taskTimeMap.put(id, new Date(startTime.getTime()));
                        break;
                    }
                }
                break;
            case "5":
                while (startTime.before(endTime)) {
                    startTime.setTime(startTime.getTime() + qty * 30 * 60 * 60 * 1000);
                    if (startTime.after(now) || startTime.compareTo(now) == 0) {
                        taskTimeMap.put(id, new Date(startTime.getTime()));
                        break;
                    }
                }
                break;
            default:
                break;
        }

        //????????????????????????????????????,???????????????????????????
        if (MachinePolicyRuleEnum.FLOATING_CYCLE.getValue().equals(machinePolicy.getPolicyRule())) {
            if (CommonUtil.isEmptyOrNull(shiftList)) {
                taskTimeMap.remove(id);
            } else {
                boolean notInTime = true;
                for (Shift shift : shiftList) {
                    Date date = taskTimeMap.get(id);
                    if (date.after(DateUtil.parseTimeToday(shift.getShiftStartTime())) && date.before(DateUtil.parseTimeToday(shift.getShiftEndTime()))) {
                        notInTime = false;
                    }
                }
                if (notInTime) {
                    taskTimeMap.remove(id);
                }
            }
        }
    }


    /**
     * ????????????/????????????
     */
    private void createTask(String id) {
        Date date = taskTimeMap.get(id);
        Date now = new Date();
        long nowTime = now.getTime();
        if (nowTime >= date.getTime()) {
            MachineService machineService = SpringContextUtils.getBean(MachineServiceImpl.class);
            machineService.createMaintenanceTaskJob(id);
            //????????????????????????
            MachinePolicy machinePolicy = machinePolicyService.getById(id);
            machinePolicy.setLastExcuteTime(date);
            machinePolicyService.updateById(machinePolicy);
            //??????????????????
            MachineLog machineLog = new MachineLog();
            machineLog.setMachineId(machinePolicy.getMachineId());
            machineLog.setOprateType(MesCommonConstant.MACHINE_MAINTENANCE_TASK_ADD);
            machineLog.setDescription("????????????:" + machinePolicy.getPolicyName() + "?????????????????????");
        }
    }
}
