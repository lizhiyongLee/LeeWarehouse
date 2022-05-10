package com.ils.modules.mes.machine.vo;

import lombok.*;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/12/25 16:56
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RepairTaskRealExcuter {
    private static final long serialVersionUID = 1L;
    /**
     * 任务id
     */
    public String id;
    /**
     * 实际执行人id字符串
     */
    public String realExcuters;
}
