package com.ils.modules.mes.produce.vo;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ils.modules.mes.produce.util.AutoTimeUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 工序任务时间
 * @author: fengyi
 */
@Setter
@Getter
@ToString(callSuper = true)
public class AutoScheProcessTaskTime {

    /** 工序任务最小时间 */
    private Long minTime;
    /** 工序任务最大时间 */
    private Long maxTime;

    private String minTimeStr;
    private String maxTimeStr;

    public void parseDate() {
        minTimeStr = DateTime.of(minTime).toString(AutoTimeUtils.DATE_TIME_PATTERN);
        maxTimeStr = DateTime.of(maxTime).toString(AutoTimeUtils.DATE_TIME_PATTERN);
    }
}
