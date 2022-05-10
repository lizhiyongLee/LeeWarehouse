package com.ils.modules.mes.machine.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.machine.entity.MachineTypeData;
import com.ils.modules.mes.base.machine.entity.MachineTypePolicy;
import com.ils.modules.mes.base.product.vo.ResultDataVO;
import com.ils.modules.mes.machine.entity.Machine;
import com.ils.modules.mes.machine.vo.MachineDetailsVO;
import com.ils.modules.mes.machine.vo.MachineOeeSettingVO;
import com.ils.modules.mes.machine.vo.MachineVO;

import java.util.List;

/**
 * @Description: 设备管理
 * @Author: Tian
 * @Date: 2020-11-10
 * @Version: V1.0
 */
public interface MachineService extends IService<Machine> {

    /**
     * 添加
     *
     * @param machine
     */
    public void saveMachine(Machine machine);

    /**
     * 修改
     *
     * @param machineVO
     */
    public void updateMachine(MachineVO machineVO);

    /**
     * 修改设备参数
     *
     * @param machineVO
     */
    void updateMachinePara(MachineVO machineVO);

    /**
     * 删除设备参数
     *
     * @param ids
     */
    void deleteMachinePara(String ids);


    /**
     * 修改设备读数
     *
     * @param machineVO
     */
    void updateMachineData(MachineVO machineVO);

    /**
     * 根据设备类型查询策略组
     *
     * @param machineTypeId
     * @param page
     * @return
     */
    Page<MachineTypePolicy> getPolicyByMachineTypeId(String machineTypeId, Page<MachineTypePolicy> page);

    /**
     * 根据设备类型查询设备读数组
     *
     * @param machineTypeId
     * @param page
     * @return
     */
    Page<MachineTypeData> getDataByMachineTypeId(String machineTypeId, Page<MachineTypeData> page);

    /**
     * 根据id查询设备关联详情
     *
     * @param id
     * @return
     */
    public MachineVO selectDetailById(String id);

    /**
     * 定时任务，生成维保和点检任务
     *
     * @param id
     */
    public void createMaintenanceTaskJob(String id);

    /**
     * 设备维修和维保执行端查看设备详情
     *
     * @param id
     * @return
     */
    public MachineDetailsVO getMachineDetails(String id);

    /**
     * 改变设备状态
     *
     * @param machineId
     * @param status
     */
    public void changeMachineStatus(String machineId, String status);

    /**
     * 改变设备策略状态
     *
     * @param policyId
     * @param status
     */
    public void changeMachinePolicyStatus(String policyId, String status);

    /**
     * 添加基准执行时间
     *
     * @param policyId
     * @param baseTime
     */
    public void addExcuteBaseTime(String policyId, String baseTime);

    /**
     * 手动执行策略
     *
     * @param policyId
     */
    public void performedManuallyPolicy(String policyId);

    /**
     * 添日志信息
     *
     * @param machineId
     * @param oprateType
     * @param description
     */
    public void addMachineLog(String machineId, String oprateType, String description);

    /**
     * 查询当前工位可选设备
     *
     * @param stationId
     * @return
     */
    JSONArray getMachineByStationId(String stationId);

    /**
     * OEE设备看板
     *
     * @param machineOeeSettingVO
     * @return
     */
    ResultDataVO getOeeData(MachineOeeSettingVO machineOeeSettingVO);
}
