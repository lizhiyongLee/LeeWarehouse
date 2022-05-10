package com.ils.modules.mes.machine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.machine.vo.MachineTypeParaVO;
import com.ils.modules.mes.machine.entity.MachineParaHead;
import com.ils.modules.mes.machine.vo.MachineParaVO;

import java.util.List;

/**
 * @Description: 设备关联参数主表
 * @Author: Tian
 * @Date: 2021-10-19
 * @Version: V1.0
 */
public interface MachineParaHeadService extends IService<MachineParaHead> {


    /**
     * 将设备“类型”关联参数的数据，赋值并保存一份到设备管理参数相关表中
     * @param machineTypeParaVOList
     * @param machineId
     */
    public void saveMachineParaHeadVO(List<MachineTypeParaVO> machineTypeParaVOList, String machineId);

    /**
     * 通过设备id查询相关数据
     * @param machineId
     * @return
     */
    public List<MachineParaVO> queryParaDataByMachineId(String machineId);

    /**
     * 通过设备id删除
     * @param machineId
     * @return
     */
   void deleteByMachineId(String machineId);

    /**
     * 通过id删除
     * @param id
     * @return
     */
   void deleteById(String id);

    /**
     * 批量新增
     * @param machineParaVOList
     * @param machineId
     * @return
     */
   void saveBatch(List<MachineParaVO> machineParaVOList, String machineId);
}
