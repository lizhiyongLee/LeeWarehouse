package com.ils.modules.mes.sop.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @Description: 标准作业任务步骤
 * @Author: Tian
 * @Date: 2021-07-16
 * @Version: V1.0
 */
@Data
@TableName("mes_sop_step")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_sop_step对象", description = "标准作业任务步骤")
public class SopStep extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 1、生产任务；2、质检任务；3、维保任务。
     */
    @Excel(name = "1、生产任务；2、质检任务；3、维保任务。", width = 15, dicCode = "mesRelatedTaskType")
    @ApiModelProperty(value = "1、生产任务；2、质检任务；3、维保任务。", position = 2)
    @TableField("related_task_type")
    @Dict(dicCode = "mesRelatedTaskType")
    private String relatedTaskType;
    /**
     * 关联任务id：可以是生产任务；质检任务；维保任务。
     */
    @Excel(name = "关联任务id：可以是生产任务；质检任务；维保任务。", width = 15)
    @ApiModelProperty(value = "关联任务id：可以是生产任务；质检任务；维保任务。", position = 3)
    @TableField("related_task_id")
    private String relatedTaskId;
    /**
     * 关联SOP模板id
     */
    @Excel(name = "关联SOP模板id", width = 15)
    @ApiModelProperty(value = "关联SOP模板id", position = 4)
    @TableField("template_id")
    private String templateId;
    /**
     * 同一层，同一父步骤下步骤顺序从大到小排列，顺序为1，2，3，4，5。。。
     */
    @Excel(name = "同一层，同一父步骤下步骤顺序从大到小排列，顺序为1，2，3，4，5。。。", width = 15)
    @ApiModelProperty(value = "同一层，同一父步骤下步骤顺序从大到小排列，顺序为1，2，3，4，5。。。", position = 5)
    @TableField("step_seq")
    private Integer stepSeq;
    /**
     * 步骤/步骤组名称
     */
    @Excel(name = "步骤/步骤组名称", width = 15)
    @ApiModelProperty(value = "步骤/步骤组名称", position = 6)
    @TableField("step_name")
    private String stepName;
    /**
     * 步骤/步骤组显示名称
     */
    @Excel(name = "步骤/步骤组显示名称", width = 15)
    @ApiModelProperty(value = "步骤/步骤组显示名称", position = 7)
    @TableField("step_display_name")
    private String stepDisplayName;
    /**
     * 1、是；0，否。
     */
    @Excel(name = "是否为第一步：1、是；0，否。", width = 15, dicCode = "mesIsOrNot")
    @ApiModelProperty(value = "1、是；0，否。", position = 8)
    @TableField("first_step")
    @Dict(dicCode = "mesIsOrNot")
    private String firstStep;
    /**
     * 1、是；0，否。
     */
    @Excel(name = "是否为最好后一步：1、是；0，否。", width = 15, dicCode = "mesIsOrNot")
    @ApiModelProperty(value = "1、是；0，否。", position = 9)
    @TableField("last_step")
    @Dict(dicCode = "mesIsOrNot")
    private String lastStep;
    /**
     * 1、用户；2、角色。
     */
    @Excel(name = "1、用户；2、角色。", width = 15, dicCode = "mesExecuteAuthority")
    @ApiModelProperty(value = "1、用户；2、角色。", position = 10)
    @TableField("execute_authority")
    @Dict(dicCode = "mesExecuteAuthority")
    private String executeAuthority;
    /**
     * 如果权限为角色，即对应角色id；如果权限为用户，即对应用户id。
     */
    @Excel(name = "如果权限为角色，即对应角色id；如果权限为用户，即对应用户id。", width = 15)
    @ApiModelProperty(value = "如果权限为角色，即对应角色id；如果权限为用户，即对应用户id。", position = 11)
    @TableField("executer")
    private String executer;
    /**
     * 后续步骤id
     */
    @Excel(name = "后续步骤id", width = 15)
    @ApiModelProperty(value = "后续步骤id", position = 12)
    @TableField("next_step_id")
    private String nextStepId;
    /**
     * 1、未执行；2、执行中；3、完成。
     */
    @Excel(name = "1、未执行；2、执行中；3、完成。", width = 15, dicCode = "mesStepStatus")
    @ApiModelProperty(value = "1、未执行；2、执行中；3、完成。", position = 13)
    @TableField("step_status")
    @Dict(dicCode = "mesStepStatus")
    private String stepStatus;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 14)
    @TableField("note")
    private String note;
}
