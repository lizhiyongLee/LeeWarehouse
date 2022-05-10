package com.ils.modules.mes.base.schedule.vo;

import com.ils.modules.mes.base.schedule.entity.ScheduleStandardProductionVolume;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 批量新增产能
 * @author: fengyi
 * @date: 2021年2月2日 上午9:26:54
 */
@Setter
@Getter
@ToString(callSuper = true)
public class StdProductionVolumeVO extends ScheduleStandardProductionVolume {

    /** 多个工位“，”隔开 */
    public String stationIds;
}
