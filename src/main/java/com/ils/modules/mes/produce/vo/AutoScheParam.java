package com.ils.modules.mes.produce.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.modules.mes.base.schedule.entity.ScheduleAutoRuleConfigure;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 自动排程参数
 * @author: fengyi
 * @date: 2021年2月24日 下午4:34:03
 */
@Setter
@Getter
@ToString(callSuper = true)
public class AutoScheParam implements Serializable {

    /**
     * @Fields serialVersionUID : TODO
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "基准时间", hidden = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date baseTime;

    /**
     * 班组排程时未找到工作日历是否完全终止
     */
    private String overWorkCalendarFlag;
    /**
     * 自动配置规则
     */
    private ScheduleAutoRuleConfigure scheAutoRuleConfigure;

    /**
     * 待排程工序任务
     */
    List<AutoScheProcessVO> lstAutoScheProcessVO;
}
