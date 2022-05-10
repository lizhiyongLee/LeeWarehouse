package com.ils.modules.mes.base.qc.entity;

import java.math.BigDecimal;

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
 * @Description: 质检方案关联质检项
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_qc_method_detail")
@ApiModel(value="mes_qc_method_detail对象", description="质检方案关联质检项")
public class QcMethodDetail extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**质检方案id*/
    @TableField("mehtod_id")
    @ApiModelProperty(value = "质检方案id", position = 2)
	private String mehtodId;
	/**质检项id*/
    @Excel(name = "质检项名称", width = 15,dicText = "qc_item_name",dictTable = "mes_qc_item",dicCode = "id")
    @TableField("item_id")
    @ApiModelProperty(value = "质检项id", position = 3)
	private String itemId;
	/**质检项名称*/
    @TableField("item_name")
    @ApiModelProperty(value = "质检项名称", position = 4)
	private String itemName;
	/**质检项标准*/
    @Excel(name = "质检项标准", width = 15,dicCode = "mesQcStandard")
    @TableField("qc_standard")
    @ApiModelProperty(value = "质检项标准", position = 5)
	private String qcStandard;
	/**最小值*/
    @Excel(name = "最小值", width = 15)
    @TableField("min_value")
    @ApiModelProperty(value = "最小值", position = 6)
	private BigDecimal minValue;
	/**最大值*/
    @Excel(name = "最大值", width = 15)
    @TableField("max_value")
    @ApiModelProperty(value = "最大值", position = 7)
	private BigDecimal maxValue;
	/**等于值*/
    @Excel(name = "等于值", width = 15)
    @TableField("equel_value")
    @ApiModelProperty(value = "等于值", position = 8)
	private BigDecimal equelValue;
	/**标准值*/
    @Excel(name = "标准值", width = 15)
    @TableField("standard_value")
    @ApiModelProperty(value = "标准值", position = 9)
	private BigDecimal standardValue;
	/**上偏差*/
    @Excel(name = "上偏差", width = 15)
    @TableField("up_diaviation")
    @ApiModelProperty(value = "上偏差", position = 10)
	private BigDecimal upDiaviation;
	/**下偏差*/
    @Excel(name = "下偏差", width = 15)
    @TableField("down_diaviation")
    @ApiModelProperty(value = "下偏差", position = 11)
	private BigDecimal downDiaviation;
	/**单位*/
    @Excel(name = "单位", width = 15,dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
    @TableField("value_unit")
    @ApiModelProperty(value = "单位", position = 12)
    @Dict(dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
	private String valueUnit;
}
