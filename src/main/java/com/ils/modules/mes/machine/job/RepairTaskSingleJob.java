package com.ils.modules.mes.machine.job;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.system.vo.DictModel;
import com.ils.common.system.util.TenantContext;
import com.ils.modules.mes.base.machine.mapper.MachineFaultAppearanceMapper;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.machine.entity.MachineRepairTask;
import com.ils.modules.mes.machine.entity.MachineRepairTaskEmployee;
import com.ils.modules.mes.machine.mapper.MachineMapper;
import com.ils.modules.mes.machine.mapper.MachineRepairTaskEmployeeMapper;
import com.ils.modules.mes.machine.mapper.MachineRepairTaskMapper;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.message.service.MsgHandleServer;
import com.ils.modules.message.vo.MsgParamsVO;
import com.ils.modules.quartz.entity.QuartzJob;
import com.ils.modules.quartz.job.BaseJob;
import com.ils.modules.quartz.service.IQuartzJobService;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author lishaojie
 * @description
 * @date 2021/9/9 16:50
 */
public class RepairTaskSingleJob extends BaseJob {
    @Autowired
    MachineRepairTaskMapper machineRepairTaskMapper;
    @Autowired
    MachineRepairTaskEmployeeMapper machineRepairTaskEmployeeMapper;
    @Autowired
    MachineMapper machineMapper;
    @Autowired
    MachineFaultAppearanceMapper machineFaultAppearanceMapper;
    @Autowired
    MsgHandleServer msgHandleServer;
    @Autowired
    IQuartzJobService quartzJobService;

    public static final String ID = "RepairTaskSingleJob";
    /**
     * 提前一小时
     */
    public static final String ONE_HOUR = "1";
    /**
     * 提前8小时
     */
    public static final String EIGHT_HOURS = "2";
    /**
     * 提前1天
     */
    public static final String ONE_DAY = "3";
    /**
     * 提前1周
     */
    public static final String ONE_WEEK = "4";

    @Override
    public void executeBussion(String parameter) {
        //参数为需要提醒的任务id列表
        List<String> taskIdList = new ArrayList<>();
        Map<String, String> taskIdMap = JSONObject.parseObject(parameter, new TypeReference<Map<String, String>>() {
        });
        if (null != taskIdMap && !taskIdMap.keySet().isEmpty()) {
            for (String taskId : taskIdMap.keySet()) {
                TenantContext.setTenant(taskIdMap.get(taskId));
                MachineRepairTask machineRepairTask = machineRepairTaskMapper.selectById(taskId);
                Date now = new Date();
                //分钟级别的时间判断
                DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.minuteOfHour());
                Date date = formatDate(machineRepairTask.getAheadType(), machineRepairTask.getPlanStartTime());
                if (null != date && comparator.compare(date, now) == 0) {
                    this.sendMsg(machineRepairTask, getTimeString(machineRepairTask.getAheadType()));
                    taskIdList.add(taskId);
                }
            }
            //去除已经执行过的任务
            if (!CommonUtil.isEmptyOrNull(taskIdList)) {
                taskIdList.forEach(taskIdMap::remove);
                this.deleteTaskId(JSONObject.toJSONString(taskIdMap));
            }
        }
    }


    private void deleteTaskId(String parameter) {
        QuartzJob quartzJob = quartzJobService.getById(ID);
        quartzJob.setParameter(parameter);
        quartzJobService.updateById(quartzJob);
    }

    private void sendMsg(MachineRepairTask machineRepairTask, String timeString) {
        JSONObject msg = new JSONObject();
        msg.put("taskCode", machineRepairTask.getTaskCode());
        msg.put("time", timeString);
        msg.put("machineName", machineMapper.selectById(machineRepairTask.getRepairMachineId()).getMachineName());
        String faultAppearance = machineRepairTask.getFaultAppearance();
        List<DictModel> dictModels = machineFaultAppearanceMapper.queryDictFaultAppearance();
        for (DictModel dictModel : dictModels) {
            if (dictModel.getValue().equals(faultAppearance)) {
                faultAppearance = dictModel.getText();
            }
        }
        msg.put("faultReason", faultAppearance);

        QueryWrapper<MachineRepairTaskEmployee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("repair_task_id", machineRepairTask.getId());

        List<MachineRepairTaskEmployee> machineRepairTaskEmployeeList = machineRepairTaskEmployeeMapper.selectList(employeeQueryWrapper);
        List<String> receiverIds = new ArrayList<>();
        machineRepairTaskEmployeeList.forEach(employee -> receiverIds.add(employee.getEmployeeId()));
        MsgParamsVO msgParamsVO = new MsgParamsVO(receiverIds, null, null, "系统消息", msg);
        msgParamsVO.setTenantId(TenantContext.getTenant());
        msgHandleServer.sendMsg(MesCommonConstant.MSG_REPAIR_TASK_NOTICE, MesCommonConstant.INFORM, msgParamsVO);
    }

    public static Date formatDate(String aheadType, Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        switch (aheadType) {
            case ONE_HOUR:
                instance.add(Calendar.HOUR, -1);
                return instance.getTime();
            case EIGHT_HOURS:
                instance.add(Calendar.HOUR, -8);
                return instance.getTime();
            case ONE_DAY:
                instance.add(Calendar.DATE, -1);
                return instance.getTime();
            case ONE_WEEK:
                instance.add(Calendar.DATE, -7);
                return instance.getTime();
            default:
                return null;
        }
    }

    public String getTimeString(String aheadType) {
        switch (aheadType) {
            case ONE_HOUR:
                return "1个小时";
            case EIGHT_HOURS:
                return "8个小时";
            case ONE_DAY:
                return "1天";
            case ONE_WEEK:
                return "1个星期";
            default:
                return null;
        }
    }
}
