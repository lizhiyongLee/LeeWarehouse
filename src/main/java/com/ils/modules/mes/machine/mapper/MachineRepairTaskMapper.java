package com.ils.modules.mes.machine.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.machine.entity.MachineRepairTask;
import com.ils.modules.mes.machine.vo.MachineRepairVO;
import com.ils.modules.mes.machine.vo.RepairTaskDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 维修任务
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
public interface MachineRepairTaskMapper extends ILSMapper<MachineRepairTask> {
    /**
     * 分页查询维保任务
     * @param userId
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<MachineRepairVO> listPage(String userId,Page<MachineRepairTask> page,
         @Param("ew") QueryWrapper<MachineRepairTask> queryWrapper);

    /**
     * 查询设备维修详情
     * @param taskId
     * @return
     */
    public RepairTaskDetailVO queryRepairTaskDetailsByTaskId(String taskId);

    /**
     * 查询系统用户Id和名字
     * @return
     */
    public List<DictModel> queryUserInfoWithNameAndId();

    /**
     * 通过设备id查询模板类型
     * @param machineId
     * @return
     */
    public String queryTemplateIdByMachineId(String machineId);

    /**
     * 查询待维修设备
     * @return
     */
    public List<String> queryRepairMachineTask();
}
