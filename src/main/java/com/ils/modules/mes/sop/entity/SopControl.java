package com.ils.modules.mes.sop.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @Description: 标准作业任务步骤控件
 * @Author: Tian
 * @Date: 2021-07-16
 * @Version: V1.0
 */
@Data
@TableName("mes_sop_control")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_sop_control对象", description = "标准作业任务步骤控件")
public class SopControl extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 模板步骤id
     */
    @Excel(name = "模板步骤id", width = 15)
    @ApiModelProperty(value = "模板步骤id", position = 2)
    @TableField("sop_step_id")
    private String sopStepId;
    /**
     * 关联任务号
     */
    @Excel(name = "关联任务号", width = 15)
    @ApiModelProperty(value = "关联任务号", position = 3)
    @TableField("related_task_id")
    private String relatedTaskId;
    /**
     * 控件顺序
     */
    @Excel(name = "控件顺序", width = 15)
    @ApiModelProperty(value = "控件顺序", position = 2)
    @TableField("control_seq")
    private Integer controlSeq;
    /**
     * 1、标签入库；2、库存入库；3、投料；4、产出；5、质检；6、标签出库；7、库存出库、8、报告模板。
     * 如果是入库，投料，产出，质检，出库控件，控件值为空，操作记录分别记录到对应操作表中。这些对应的每个操作表中都要有一个字段对应标准作业任务步骤记录值中的id。
     */
    @TableField("control_type")
    private String controlType;
    /**
     * 对于入库控件：1、管控物料清单物料与入库位置；2、不管控。
     * 对于出库控件：1、管控产出物料与出库位置；2、不管控。
     * 对于投料控件：1、管控某次单独投入的物料；2、不管控。
     * 对于产出控件：无需管控，由结束按钮控制；
     * 对于质检控件：质检任务产生可以根据上个步骤点完成时触发产生一个质检任务；
     * 报告模板控件：报告模板控件不需要控制逻辑。
     */

    @TableField("control_logic")
    private String controlLogic;
    /**
     * 当使用的是投料控件时，实体对应物料编码，其他控件该字段为空。
     */
    @Excel(name = "当使用的是投料控件时，实体对应物料编码，其他控件该字段为空。", width = 15)
    @ApiModelProperty(value = "当使用的是投料控件时，实体对应物料编码，其他控件该字段为空。", position = 6)
    @TableField("entity_item")
    private String entityItem;
    /**
     * 任务步骤控件名称
     */
    @Excel(name = "任务步骤控件名称", width = 15)
    @ApiModelProperty(value = "任务步骤控件名称", position = 7)
    @TableField("control_name")
    private String controlName;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 8)
    @TableField("note")
    private String note;
}
