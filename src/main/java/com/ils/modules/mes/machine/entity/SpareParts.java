package com.ils.modules.mes.machine.entity;

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

import java.math.BigDecimal;


/**
 * @Description: 备件定义
 * @Author: Tian
 * @Date:   2021-02-23
 * @Version: V1.0
 */
@Data
@TableName("mes_spare_parts")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_spare_parts对象", description="备件定义")
public class SpareParts extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**备件类型*/
	@Excel(name = "备件类型", width = 15,dictTable = "mes_spare_parts_type",dicText = "type_name",dicCode = "id")
    @ApiModelProperty(value = "备件类型", position = 2)
    @TableField("type_id")
	@Dict(dictTable = "mes_spare_parts_type",dicText = "type_name",dicCode = "id")
	private String typeId;
	/**图片*/
    @ApiModelProperty(value = "图片", position = 3)
    @TableField("picture")
	private String picture;
	/**备件编码*/
	@Excel(name = "备件编码", width = 15)
    @ApiModelProperty(value = "备件编码", position = 4)
    @TableField("spare_parts_code")
	private String sparePartsCode;
	/**备件名称*/
	@Excel(name = "备件名称", width = 15)
    @ApiModelProperty(value = "备件名称", position = 5)
    @TableField("spare_parts_name")
	private String sparePartsName;
	/**备件描述*/
	@Excel(name = "备件描述", width = 15)
    @ApiModelProperty(value = "备件描述", position = 6)
    @TableField("spare_parts_description")
	private String sparePartsDescription;
	/**型号*/
	@Excel(name = "型号", width = 15)
    @ApiModelProperty(value = "型号", position = 7)
    @TableField("model")
	private String model;
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量", position = 8)
    @TableField("qty")
	private BigDecimal qty;
	/**主单位*/
	@Excel(name = "单位",dictTable = "mes_unit",dicCode = "id",dicText = "unit_name")
    @ApiModelProperty(value = "主单位", position = 9)
    @TableField("unit")
	@Dict(dictTable = "mes_unit",dicCode = "id",dicText = "unit_name")
	private String unit;
	/**附件*/
    @ApiModelProperty(value = "附件", position = 10)
    @TableField("attach")
	private String attach;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 11)
    @TableField("note")
	private String note;
}
