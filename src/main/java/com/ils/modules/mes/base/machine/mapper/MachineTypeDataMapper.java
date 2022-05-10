package com.ils.modules.mes.base.machine.mapper;

import java.util.List;

import net.bytebuddy.asm.Advice;
import org.apache.ibatis.annotations.Param;
import com.ils.modules.mes.base.machine.entity.MachineTypeData;
import com.ils.common.system.base.mapper.ILSMapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 设备关联读数
 * @Author: Tian
 * @Date:   2020-10-30
 * @Version: V1.0
 */

public interface MachineTypeDataMapper extends ILSMapper<MachineTypeData> {

    /**
     * 通过主表id区删除
     * @param mainId
     * @return
     */
    public boolean deleteByMainId(String mainId);

    /**
     * 通过主表id查询
     * @param id
     * @return MachineTypeData集合
     */
    public List<MachineTypeData> selectAllByMachineTypeId(String id);

}
