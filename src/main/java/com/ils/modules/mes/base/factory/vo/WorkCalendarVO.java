package com.ils.modules.mes.base.factory.vo;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.ils.modules.mes.base.factory.entity.WorkCalendar;
import com.ils.modules.mes.constants.MesCommonConstant;
import lombok.*;

/**
 * @Description: 返回结果的工作日历VO
 * @author: fengyi
 * @date: 2020年10月21日 下午1:32:41
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class WorkCalendarVO extends WorkCalendar {

    /**
     * 班次名称
     */
    private String shiftName;
    /**
     * 班次开始时间
     */
    private String shiftStartTime;
    /**
     * 班次结束时间
     */
    private String shiftEndTime;
    /**
     * 班次备注
     */
    private String shiftNote;

    private DateTime shiftStartDateTime;
    private DateTime shiftEndDateTime;

    /**
     * 当前工作日历绑定班组的完整日期
     *
     * @author niushuai
     * @date: 2021/6/16 9:55:43
     * @return: void
     */
    public void setShiftFullDateTime() {
        if (StrUtil.isNotEmpty(shiftStartTime) && StrUtil.isNotEmpty(shiftEndTime)) {
            String formatWorkDate = DateUtil.formatDate(super.getWorkdate());
            shiftStartDateTime = DateUtil.parseDateTime(
                    StrUtil.concat(true, formatWorkDate, MesCommonConstant.SPACE_SIGN, shiftStartTime));
            shiftEndDateTime = DateUtil.parseDateTime(
                    StrUtil.concat(true, formatWorkDate, MesCommonConstant.SPACE_SIGN, shiftEndTime));

            if (shiftStartDateTime.isAfter(shiftEndDateTime)) {
                shiftEndDateTime = DateUtil.offsetDay(shiftEndDateTime, 1);
            }
        }
    }

}
