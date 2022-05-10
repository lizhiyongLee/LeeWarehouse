package com.ils.modules.mes.base.craft.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.aspect.annotation.KeyWord;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 工序
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_process")
@ApiModel(value="mes_process对象", description="工序")
public class Process extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**工序编码*/
    @Excel(name = "工序编码", width = 15)
    @TableField("process_code")
    @ApiModelProperty(value = "工序编码", position = 2)
	private String processCode;
	/**工序名称*/
    @Excel(name = "工序名称", width = 15)
    @TableField("process_name")
    @ApiModelProperty(value = "工序名称", position = 3)
    @KeyWord
	private String processName;
	/**允许超产比例*/
    @Excel(name = "允许超产比例", width = 15)
    @TableField("surpass_rate")
    @ApiModelProperty(value = "允许超产比例", position = 4)
	private Integer surpassRate;
	/**工艺描述*/
    @Excel(name = "工艺描述", width = 15)
    @TableField("description")
    @ApiModelProperty(value = "工艺描述", position = 5)
	private String description;
	/**填报模板*/
    @Excel(name = "填报模板", width = 15)
    @TableField("template_id")
    @ApiModelProperty(value = "填报模板", position = 6)
    @Dict(dicCode = "id",dictTable = "mes_report_template",dicText = "template_name")
	private String templateId;
	/**状态 ：1，启用，0停用；*/
    @Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @TableField("status")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 7)
    @Dict(dicCode = "mesStatus")
	private String status;
	/**附件*/
    @Excel(name = "附件", width = 15)
    @TableField("attach")
    @ApiModelProperty(value = "附件", position = 8)
	private String attach;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 9)
	private String note;
}
