package com.ils.modules.mes.base.qc.entity;

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
 * @Description: 质检项
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
@Data
@TableName("mes_qc_item")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_qc_item对象", description="质检项")
public class QcItem extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**不良类型*/
	@Excel(name = "不良类型", width = 15)
    @ApiModelProperty(value = "不良类型", position = 2)
    @TableField("qc_type_id")
	@Dict(dictTable = "mes_qc_item_type", dicCode = "id", dicText = "qc_type_name")
	private String qcTypeId;
	/**不良项编码*/
	@Excel(name = "不良项编码", width = 15)
    @ApiModelProperty(value = "不良项编码", position = 3)
    @TableField("qc_item_code")
    @KeyWord
	private String qcItemCode;
	/**不良项名称*/
	@Excel(name = "不良项名称", width = 15)
    @ApiModelProperty(value = "不良项名称", position = 4)
    @TableField("qc_item_name")
    @KeyWord
	private String qcItemName;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 5)
    @TableField("note")
	private String note;
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 6)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
	private String status;
}
