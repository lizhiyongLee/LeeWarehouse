package com.ils.modules.mes.base.sop.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.ils.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.common.system.base.entity.ILSEntity;


/**
 * @Description: SOP模板步骤
 * @Author: Tian
 * @Date:   2021-07-15
 * @Version: V1.0
 */
@Data
@TableName("mes_sop_template_step")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_sop_template_step对象", description="SOP模板步骤")
public class SopTemplateStep extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**关联SOP模板id*/
	@Excel(name = "关联SOP模板id", width = 15)
    @ApiModelProperty(value = "关联SOP模板id", position = 2)
    @TableField("template_id")
	private String templateId;
	/**顺序为1，2，3，4，5。。。*/
	@Excel(name = "顺序为1，2，3，4，5。。。", width = 15)
    @ApiModelProperty(value = "顺序为1，2，3，4，5。。。", position = 3)
    @TableField("step_seq")
	private Integer stepSeq;
	/**步骤名称*/
	@Excel(name = "步骤名称", width = 15)
    @ApiModelProperty(value = "步骤名称", position = 4)
    @TableField("step_name")
	private String stepName;
	/**步骤显示名称*/
	@Excel(name = "步骤显示名称", width = 15)
    @ApiModelProperty(value = "步骤显示名称", position = 5)
    @TableField("step_display_name")
	private String stepDisplayName;
	/**1、是；0，否。*/
	@Excel(name = "1、是；0，否。", width = 15, dicCode = "mesIsOrNot")
    @ApiModelProperty(value = "1、是；0，否。", position = 6)
    @TableField("first_step")
	@Dict(dicCode = "mesIsOrNot")
	private String firstStep;
	/**1、是；0，否。*/
	@Excel(name = "1、是；0，否。", width = 15, dicCode = "mesIsOrNot")
    @ApiModelProperty(value = "1、是；0，否。", position = 7)
    @TableField("last_step")
	@Dict(dicCode = "mesIsOrNot")
	private String lastStep;
	/**1、用户；2、角色。*/
	@Excel(name = "1、用户；2、角色。", width = 15, dicCode = "mesExecuteAuthority")
    @ApiModelProperty(value = "1、用户；2、角色。", position = 8)
    @TableField("execute_authority")
	@Dict(dicCode = "mesExecuteAuthority")
	private String executeAuthority;
	/**如果权限为角色，即对应角色编码；如果权限为用户，即对应用户。*/
	@Excel(name = "如果权限为角色，即对应角色编码；如果权限为用户，即对应用户。", width = 15)
    @ApiModelProperty(value = "如果权限为角色，即对应角色编码；如果权限为用户，即对应用户。", position = 9)
    @TableField("executer")
	private String executer;
	/**后续步骤id,当最后一个步骤时，该值为end。*/
	@Excel(name = "后续步骤id,当最后一个步骤时，该值为end。", width = 15)
    @ApiModelProperty(value = "后续步骤id,当最后一个步骤时，该值为end。", position = 10)
    @TableField("next_step_id")
	private String nextStepId;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 11)
    @TableField("note")
	private String note;
}
