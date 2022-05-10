package com.ils.modules.mes.produce.vo;

import com.ils.modules.mes.base.factory.entity.Shift;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 自动排程班次VO
 * @author: fengyi
 */
@Setter
@Getter
@ToString(callSuper = true)
public class AutoScheShiftVO extends Shift {
    /**
     * 班次时长按毫秒标识
     */
    Long shiftDuration;

}
