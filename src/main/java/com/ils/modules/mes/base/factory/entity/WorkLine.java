package com.ils.modules.mes.base.factory.entity;

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
 * @Description: 产线
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_work_line")
@ApiModel(value="mes_work_line对象", description="产线")
public class WorkLine extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**产线编码*/
    @Excel(name = "产线编码", width = 15)
    @TableField("line_code")
    @ApiModelProperty(value = "产线编码", position = 2)
	private String lineCode;
	/**产线名称*/
    @Excel(name = "产线名称", width = 15)
    @TableField("line_name")
    @ApiModelProperty(value = "产线名称", position = 3)
	private String lineName;
	/**二维码*/
    @Excel(name = "二维码", width = 15)
    @TableField("qrcode")
    @ApiModelProperty(value = "二维码", position = 4)
	private String qrcode;
	/**上级区域*/
    @Excel(name = "上级区域id", width = 15)
    @TableField("up_area")
    @ApiModelProperty(value = "上级区域id", position = 5)
	private String upArea;
	/**上级区域*/
    @Excel(name = "上级区域名称", width = 15)
    @TableField("up_area_name")
    @ApiModelProperty(value = "上级区域名称", position = 6)
	private String upAreaName;
	/**负责人*/
    @Excel(name = "负责人id", width = 15)
    @TableField("duty_person")
    @ApiModelProperty(value = "负责人id", position = 7)
	private String dutyPerson;
	/**负责人*/
    @Excel(name = "负责人姓名", width = 15)
    @TableField("duty_person_name")
    @ApiModelProperty(value = "负责人姓名", position = 8)
	private String dutyPersonName;
	/**状态 ：1，启用，0停用；*/
    @Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @TableField("status")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 9)
    @Dict(dicCode = "mesStatus")
	private String status;
	/**附件*/
    @Excel(name = "附件", width = 15)
    @TableField("attach")
    @ApiModelProperty(value = "附件", position = 10)
	private String attach;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 11)
	private String note;
}
