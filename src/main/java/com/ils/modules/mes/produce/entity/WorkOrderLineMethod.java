package com.ils.modules.mes.produce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.system.base.entity.ILSEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 工单工序对应工位
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_work_order_line_method")
@ApiModel(value = "mes_work_order_line_method", description = "工单工艺质检方案")
public class WorkOrderLineMethod extends ILSEntity {
    private static final long serialVersionUID = 1L;

    @TableField("related_type")
    @ApiModelProperty(value = "工单可能对应：1、工艺路线；2、生产bom。", position = 2)
    private String relatedType;

    @TableField("work_order_line_id")
    @ApiModelProperty(value = "工单工艺路线id", position = 3)
    private String workOrderLineId;

    @TableField("seq")
    @ApiModelProperty(value = "序号", position = 4)
    private Integer seq;
    /**
     * 工序id
     */
    @TableField("process_id")
    @ApiModelProperty(value = "工序id", position = 5)
    private String processId;
    /**
     * 工序编码
     */
    @TableField("process_code")
    @ApiModelProperty(value = "工序编码", position = 6)
    private String processCode;
    /**
     * 工序名称
     */
    @TableField("process_name")
    @ApiModelProperty(value = "工序名称", position = 7)
    private String processName;

    /**
     * 质检方案名称
     */
    @TableField("qc_method_name")
    @ApiModelProperty(value = "质检方案名称", position = 8)
    private String qcMethodName;

    /**
     * 质检方案id
     */
    @TableField("qc_method_id")
    @ApiModelProperty(value = "质检方案id", position = 9)
    private String qcMethodId;

    /**
     * 质检类型
     */
    @TableField("qc_type")
    @ApiModelProperty(value = "质检类型", position = 10)
    private String qcType;
}
