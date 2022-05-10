package com.ils.modules.mes.machine.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.machine.entity.MachineMaintenanceTask;
import com.ils.modules.mes.machine.vo.MaintenancePageListWithPhoneVO;
import com.ils.modules.mes.machine.vo.MaintenanceTaskDetailVO;
import com.ils.modules.mes.machine.vo.MaintenanceTaskVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 维保任务
 * @Author: Tian
 * @Date: 2020-11-17
 * @Version: V1.0
 */
public interface MachineMaintenanceTaskMapper extends ILSMapper<MachineMaintenanceTask> {

    /**
     * 分页查询维保任务
     *
     * @param userId
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<MaintenanceTaskVO> listPage(String userId, Page<MaintenanceTaskVO> page,
                                             @Param("ew") QueryWrapper<MaintenanceTaskVO> queryWrapper);

    /**
     * 查询维护任务详情
     * @param taskId
     * @return
     */
    public MaintenancePageListWithPhoneVO queryMaintenanceTaskById(String taskId);
    /**
     * 查询维护任务的详情，包括基本信息和维护策略等等
     *
     * @param maintenanceTaskId
     * @param type
     * @return
     */
    public MaintenanceTaskDetailVO selectMaintenanceDetailById(String maintenanceTaskId, String type);

    /**
     * 分页查询维护任务
     * @param page
     * @param queryWrapper
     * @param userId
     * @return
     */
    public IPage<MaintenancePageListWithPhoneVO> listPageWithPhone(Page<MaintenancePageListWithPhoneVO> page,String userId,
                                                            @Param("ew") QueryWrapper<MaintenancePageListWithPhoneVO> queryWrapper);

    /**
     * 查询待维保的设备id
     * @param taskType
     * @return
     */
    public List<String> queryWaitMaintenanceTask(String taskType);
}
