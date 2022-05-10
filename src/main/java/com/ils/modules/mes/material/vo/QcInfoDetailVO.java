package com.ils.modules.mes.material.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import lombok.*;

/**
 * 质检信息详情
 *
 * @author Anna.
 * @date 2021/6/10 9:45
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class QcInfoDetailVO extends ILSEntity {
    /**
     * 任务编码
     */
    private String taskCode;
    /**
     *任务类型
     */
    @Dict(dicCode = "mesQcType")
    private String qcType;
    /**
     * 方案名
     */
    private String methodName;
    /**
     * 状态
     */
    @Dict(dicCode = "mesQcResult")
    private String status;
}
