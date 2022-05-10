package com.ils.modules.mes.produce.vo;

import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.produce.entity.WorkPlanTask;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 工序任务派工任务VO
 * @author: fengyi
 * @date: 2020年11月26日 下午2:54:21
 */
@Setter
@Getter
@ToString(callSuper = true)
public class WorkPlanTaskProcessVO extends WorkPlanTask {

    private static final long serialVersionUID = 1L;
    /** 上道工序编码 */
    @Excel(name = "上道工序编码", width = 15)
    @ApiModelProperty(value = "上道工序编码", position = 8)
    private String priorCode;
    /** 下道工序编码 */
    @Excel(name = "下道工序编码", width = 15)
    @ApiModelProperty(value = "下道工序编码", position = 9)
    private String nextCode;
    /** 1,前续开始后续可开始2，前续结束后续可开始 */
    @Excel(name = "1,前续开始后续可开始2，前续结束后续可开始", width = 15)
    @ApiModelProperty(value = "1,前续开始后续可开始2，前续结束后续可开始", position = 10)
    private String linkType;
}
