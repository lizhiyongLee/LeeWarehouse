package com.ils.modules.mes.machine.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.enums.MachineMaintenanceStatusEnum;
import com.ils.modules.mes.enums.MachineTaskOprateTypeEnum;
import com.ils.modules.mes.machine.entity.MachineTaskLog;
import com.ils.modules.mes.machine.mapper.MachineTaskLogMapper;
import com.ils.modules.mes.machine.service.MachineTaskLogService;
import com.ils.modules.mes.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 设备维修日志
 * @Author: Hhuangtao
 * @Date: 2020-11-25
 * @Version: V1.0
 */
@Service
public class MachineTaskLogServiceImpl extends ServiceImpl<MachineTaskLogMapper, MachineTaskLog> implements MachineTaskLogService {


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addMachineTaskLog(MachineTaskLog machineTaskLog) {
        baseMapper.insert(machineTaskLog);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineTaskLog(MachineTaskLog machineTaskLog) {
        baseMapper.updateById(machineTaskLog);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMachineTaskLog(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMachineTaskLog(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineTaskLog(String oldStatus, String nowStatus, String taskId, String taskCode ,String taskType) {
        MachineTaskLog machineTaskLog = new MachineTaskLog();
        String description = "";
        String oprateType = "";
        //领取任务
        if (MachineMaintenanceStatusEnum.NOT_YET_START.getValue().equals(oldStatus)&& MachineMaintenanceStatusEnum.RUNNING.getValue().equals(nowStatus)) {
            description = CommonUtil.getLoginUser().getUsername()+"||"+ MachineTaskOprateTypeEnum.GET_TASK.getDesc()+"||" +taskCode;
            oprateType = MachineTaskOprateTypeEnum.GET_TASK.getValue();
        }
        //暂停任务
        if (MachineMaintenanceStatusEnum.STOP.getValue().equals(nowStatus)) {
            description = CommonUtil.getLoginUser().getUsername()+"||"+ MachineTaskOprateTypeEnum.SUSPENDED_TASK.getDesc()+"||"+taskCode;
            oprateType = MachineTaskOprateTypeEnum.SUSPENDED_TASK.getValue();
        }
        //恢复任务
        if (MachineMaintenanceStatusEnum.RUNNING.getValue().equals(nowStatus)&& MachineMaintenanceStatusEnum.STOP.getValue().equals(oldStatus)) {
            description = CommonUtil.getLoginUser().getUsername()+"||"+ MachineTaskOprateTypeEnum.RESTORE_TASK.getDesc()+"||"+taskCode;
            oprateType = MachineTaskOprateTypeEnum.RESTORE_TASK.getValue();
        }
        //结束任务
        if (MachineMaintenanceStatusEnum.ENDED.getValue().equals(nowStatus)){
            description = CommonUtil.getLoginUser().getUsername()+"||"+ MachineTaskOprateTypeEnum.END_TASK.getDesc()+"||"+taskCode;
            oprateType = MachineTaskOprateTypeEnum.END_TASK.getValue();
        }
        //取消任务
        if (MachineMaintenanceStatusEnum.CANCEL.getValue().equals(nowStatus)) {
            description = CommonUtil.getLoginUser().getUsername()+"||"+ MachineTaskOprateTypeEnum.CANCEL_TASK.getDesc()+"||"+taskCode;
            oprateType = MachineTaskOprateTypeEnum.CANCEL_TASK.getValue();
        }
        machineTaskLog.setTaskId(taskId);
        machineTaskLog.setDescription(description);
        machineTaskLog.setTaskType(taskType);
        machineTaskLog.setOprateType(oprateType);
        baseMapper.insert(machineTaskLog);
    }
}
