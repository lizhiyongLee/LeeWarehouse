package com.ils.modules.mes.machine.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.machine.entity.Machine;
import com.ils.modules.mes.machine.vo.MachineRepairAndMaintenanceVO;

import java.util.List;

/**
 * @Description: 设备管理
 * @Author: Tian
 * @Date: 2020-11-10
 * @Version: V1.0
 */
public interface MachineMapper extends ILSMapper<Machine> {

    /**
     * 查询维修和维护的所有数据
     * @param machineId
     * @return
     */
    public List<MachineRepairAndMaintenanceVO> queryRepairAndMaintenanceTaskByMachineId(String machineId);

    /**
     * 看板查询设备任务信息
     * @param machineId
     * @return
     */
    public List<MachineRepairAndMaintenanceVO> queryMachineDashBoardInfoById(String machineId);

    /**
     * 查询设备名称和对应的Id
     * @return
     */
    public List<DictModel> queryMachineNameAndMachineId();

    /**
     * 待点检设备数
     * @return
     */
    public Integer queryMachineRepairCount();

    /**
     * 待维保设备数
     * @param type
     * @return
     */
    public Integer queryMachinMaintenanceCount(String type);



}
