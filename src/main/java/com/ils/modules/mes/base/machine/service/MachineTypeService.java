package com.ils.modules.mes.base.machine.service;

import java.util.List;

import com.ils.modules.mes.base.machine.entity.MachineType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.machine.entity.MachineTypeData;
import com.ils.modules.mes.base.machine.vo.MachineTypeParaVO;
import com.ils.modules.mes.base.machine.vo.MachineTypeVO;

/**
 * @Description: 设备类型
 * @Author: Tian
 * @Date: 2020-10-29
 * @Version: V1.0
 */
public interface MachineTypeService extends IService<MachineType> {

    /**
     * 添加
     *
     * @param machineType
     */
    public void saveMachineType(MachineType machineType);

    /**
     * 修改
     *
     * @param machineType
     */
    public void updateMachineType(MachineType machineType);

    /**
     * 更新
     *
     * @param machineTypeVO
     */
    public void updateMachineTypeMain(MachineTypeVO machineTypeVO);

    /**
     * 添加MachineType的vo类
     *
     * @param machineTypeVO
     */
    public void saveMain(MachineTypeVO machineTypeVO);

    /**
     * 通过id查询主表和子表
     *
     * @param id
     * @return 返回
     */
    public MachineTypeVO selectByMainId(String id);


    /**
     * 通过设备类型id查询相关参数数据
     *
     * @param id
     * @return List<MachineTypeParaVO>
     */
    public List<MachineTypeParaVO> queryMachineTypeParaByMainId(String id);

    /**
     * 删除参数模板
     *
     * @param ids
     */
    void deleteMachinePara(String ids);

}
