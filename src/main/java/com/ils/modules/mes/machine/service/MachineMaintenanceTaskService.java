package com.ils.modules.mes.machine.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.machine.entity.MachineMaintenanceTask;
import com.ils.modules.mes.machine.vo.MachineMaintenanceTaskVO;
import com.ils.modules.mes.machine.vo.MaintenancePageListWithPhoneVO;
import com.ils.modules.mes.machine.vo.MaintenanceTaskDetailVO;
import com.ils.modules.mes.machine.vo.MaintenanceTaskVO;

import java.util.List;


/**
 * @Description: 维保任务
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
public interface MachineMaintenanceTaskService extends IService<MachineMaintenanceTask> {

    /**
     * 添加
     * @param machineMaintenanceTask
     */
    public void saveMachineMaintenanceTask(MachineMaintenanceTask machineMaintenanceTask) ;
    
    /**
     * 修改
     * @param machineMaintenanceTaskVO
     */
    public void updateMachineMaintenanceTask(MachineMaintenanceTaskVO machineMaintenanceTaskVO);
    
    /**
     * 删除
     * @param id
     */
    public void delMachineMaintenanceTask (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchMachineMaintenanceTask (List<String> idList);


    /**
     * 分页查询维护任务
     * @param userId
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<MaintenanceTaskVO> listPage(String userId,Page<MaintenanceTaskVO> page, QueryWrapper<MaintenanceTaskVO> queryWrapper);

    /**
     * 通过id查询点检任务详情页面
     * @param id
     * @param type
     * @return
     */
    public MaintenanceTaskDetailVO queryMaintenanceTaskDetailById(String id,String type);

    /**
     * 根据id查询信息
     * @param id
     * @return MaintenancePageListWithPhoneVO
     */
    public MaintenancePageListWithPhoneVO queryMaintenanceTaskById(String id);

    /**
     * 改变保养任务执行的状态
     * @param id
     * @param status
     * @param userId
     */
    public void changeStatus(String id, String status, String userId);

    /**
     * 分页查询维护任务
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<MaintenancePageListWithPhoneVO> listPageWithPhone(Page<MaintenancePageListWithPhoneVO> page,QueryWrapper<MaintenancePageListWithPhoneVO> queryWrapper);

    /**
     * 查询待维保设备
     * @param taskType
     * @return
     */
    public List<String> queryWaitMaintenanMachine(String taskType);
}
