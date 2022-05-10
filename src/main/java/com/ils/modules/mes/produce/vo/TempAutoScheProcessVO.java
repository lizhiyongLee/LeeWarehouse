package com.ils.modules.mes.produce.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 自动排程工序列表
 * @author: fengyi
 * @date: 2021年2月24日 下午4:38:42
 */
@Data
public class TempAutoScheProcessVO extends AutoScheProcessVO {

    /**
     * 工艺路线类型
     */
    private String routeType;
    /**
     * 工艺路线id
     */
    private String routeId;
    /**
     * 工艺路线code
     */
    private String routeCode;
    /**
     * 工艺路线name
     */
    private String routeName;
    /**
     * 计划开始时间
     */
    private Date planStartTime;
    /**
     * 计划开始时间 年月日
     */
    private String planStartTimeDate;
    /**
     * 计划开始时间 时分秒
     */
    private String planStartTimeTime;
   /**
     * 计划开始时间
     */
    private Date planEndTime;
    /**
     * 计划开始时间 年月日
     */
    private String planEndTimeDate;
    /**
     * 计划开始时间 时分秒
     */
    private String planEndTimeTime;

    /**
     * 当前工序任务所属工单优先级
     */
    private Long level;

    /**
     * 是否为最后一个大组元素
     *
     * @author niushuai
     * @date: 2021/11/8 13:25:20
     * @return: {@link Boolean}
     */
    public boolean lastGroupSeq() {
        return null == this.planStartTime;
    }

}
