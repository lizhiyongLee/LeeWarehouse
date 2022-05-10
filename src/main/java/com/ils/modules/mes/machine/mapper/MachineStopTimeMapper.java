package com.ils.modules.mes.machine.mapper;

import java.util.List;

import com.ils.modules.mes.base.material.vo.ItemQualityVO;
import org.apache.ibatis.annotations.Param;
import com.ils.modules.mes.machine.entity.MachineStopTime;
import com.ils.common.system.base.mapper.ILSMapper;

/**
 * @Description: 设备关联停机时间
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
public interface MachineStopTimeMapper extends ILSMapper<MachineStopTime> {

    /**
     * 通过主表 Id 查询
     * @param mainId
     * @return
     */
    public List<MachineStopTime> selectByMainId(String mainId);
}
