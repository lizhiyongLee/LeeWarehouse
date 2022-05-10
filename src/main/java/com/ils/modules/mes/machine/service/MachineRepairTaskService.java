package com.ils.modules.mes.machine.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.machine.entity.MachineRepairTask;
import com.ils.modules.mes.machine.vo.MachineRepairVO;
import com.ils.modules.mes.machine.vo.RepairTaskDetailVO;
import com.ils.modules.mes.machine.vo.RepairTaskWithEmployeeVO;

import java.util.List;

/**
 * @Description: 维修任务
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
public interface MachineRepairTaskService extends IService<MachineRepairTask> {

    /**
     * 添加
     * @param repairTaskWithEmployeeVO
     * @return
     */
    public MachineRepairTask saveMachineRepairTask(RepairTaskWithEmployeeVO repairTaskWithEmployeeVO) ;
    
    /**
     * 修改
     * @param repairTaskWithEmployeeVO
     */
    public void updateMachineRepairTask(RepairTaskWithEmployeeVO repairTaskWithEmployeeVO);

    /**
     * 分页查询维修任务
     * @param page
     * @param queryWrapper
     * @param userId
     * @return
     */
    public IPage<MachineRepairVO> listPage(String userId,Page<MachineRepairTask> page, QueryWrapper<MachineRepairTask> queryWrapper);

    /**
     *  查询维修任务详情
     * @param id
     * @return
     */
    public RepairTaskDetailVO queryDetailById(String id);

    /**
     * 查询维修任务，包含计划维修人员h和实际维修人员
     * @param id
     * @return
     */
    public RepairTaskWithEmployeeVO queryTaskWithEmployeesById(String id);

    /**
     * 扭转改变执行状态
     * @param id
     * @param status
     * @param userId
     * @param qrcode
     */
    public void changeStatus(String id, String status, String userId,String qrcode);

    /**
     * 改变实际执行人
     * @param id
     * @param realExcuters
     */
    public void changeRealExcuter(String id,String realExcuters);

    /**
     * 添加故障原因
     * @param faultReasonId
     * @param taskId
     */
    public void addFaultReason(String faultReasonId,String taskId);

    /**
     * 服务化接口，查询设备名称，和对应的id
     * @return
     */
    public List<DictModel> queryDictMachineName();

    /**
     * 查询用户Id和名字
     * @return
     */
    public List<DictModel> queryUserInfoWithNameAndId();

    /**
     * 通过设备id查询该设备的报告模板
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
