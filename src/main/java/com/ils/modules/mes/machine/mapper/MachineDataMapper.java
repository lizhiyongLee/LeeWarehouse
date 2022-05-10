package com.ils.modules.mes.machine.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.ils.modules.mes.machine.entity.MachineData;
import com.ils.common.system.base.mapper.ILSMapper;

/**
 * @Description: 设备类型关联读数
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
public interface MachineDataMapper extends ILSMapper<MachineData> {
    /**
     * 通过主表 Id 删除
     * @param mainId
     * @return
     */
    boolean deleteByMainId(String mainId);
}
