package com.ils.modules.mes.execution.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 更新参数接收VO
 * @author: fengyi
 * @date: 2020年12月10日 下午1:20:12
 */
@Setter
@Getter
@ToString(callSuper = true)
public class UpdateParamVO {
    /**
     * 执行任务ID
     */
    private String id;
    /**
     * 执行任务状态
     */
    private String exeStatus;

    /**
     * 任务类型
     */
    private String finishType;

    /**
     * 任务结束原因
     */
    private String finishReason;

}
