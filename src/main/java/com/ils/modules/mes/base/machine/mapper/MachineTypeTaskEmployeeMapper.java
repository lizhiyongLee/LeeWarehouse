package com.ils.modules.mes.base.machine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.ils.modules.mes.base.machine.entity.MachineTypeTaskEmployee;
import com.ils.common.system.base.mapper.ILSMapper;

/**
 * @Description: 设备类型关联策略组
 * @Author: Tian
 * @Date:   2020-10-30
 * @Version: V1.0
 */
public interface MachineTypeTaskEmployeeMapper extends ILSMapper<MachineTypeTaskEmployee> {

    /**
     *根据策略id查询
     * @param id
     * @return
     */
    public List<MachineTypeTaskEmployee> selectByPolicyId(String id);

    /**根据策略id删除
     *
     * @param policyId
     *
     */
    public void deleteByPolicyId(String policyId);

    /**
     * 根据维保任务id查询计划人员
     * @param id
     * @return
     */
    public List<MachineTypeTaskEmployee> selectByMaintenanceTaskId(String id);
 }
