package com.ils.modules.mes.produce.entity;

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

import java.math.BigDecimal;

/**
 * @Description: 工单产品BOM
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_work_order_bom")
@ApiModel(value="mes_work_order_bom对象", description="工单产品BOM")
public class WorkOrderBom extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**工单id*/
    @TableField("order_id")
    @ApiModelProperty(value = "工单id", position = 2)
	private String orderId;
	/**产品bomid*/
    @Excel(name = "产品bomid", width = 15)
    @TableField("product_id")
    @ApiModelProperty(value = "产品bomid", position = 3)
	private String productId;
	/**产品工艺路线明细id*/
    @Excel(name = "产品工艺路线明细id", width = 15)
    @TableField("product_line_id")
    @ApiModelProperty(value = "产品工艺路线明细id", position = 4)
	private String productLineId;
	/**工序序号*/
    @Excel(name = "工序序号", width = 15)
    @TableField("seq")
    @ApiModelProperty(value = "工序序号", position = 5)
	private Integer seq;
	/**工序id*/
    @Excel(name = "工序id", width = 15)
    @TableField("process_id")
    @ApiModelProperty(value = "工序id", position = 6)
	private String processId;
	/**工序编码*/
    @Excel(name = "工序编码", width = 15)
    @TableField("process_code")
    @ApiModelProperty(value = "工序编码", position = 7)
	private String processCode;
	/**工序名称*/
    @Excel(name = "工序名称", width = 15)
    @TableField("process_name")
    @ApiModelProperty(value = "工序名称", position = 8)
	private String processName;
	/**物料id*/
    @Excel(name = "物料id", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 9)
	private String itemId;
	/**物料编码*/
	@KeyWord
    @Excel(name = "物料编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "物料编码", position = 10)
	private String itemCode;
	/**物料名称*/
	@KeyWord
    @Excel(name = "物料名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "物料名称", position = 11)
	private String itemName;
	/**数量*/
    @Excel(name = "数量", width = 15)
    @TableField("qty")
    @ApiModelProperty(value = "数量", position = 12)
	private BigDecimal qty;

    /** 本工序物料总数量 */
    @Excel(name = "本工序物料总数量", width = 15)
    @TableField("total_qty")
    @ApiModelProperty(value = "本工序物料总数量", position = 12)
    private BigDecimal totalQty;

	/**单位*/
    @Excel(name = "单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("unit")
    @ApiModelProperty(value = "单位", position = 13)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
	private String unit;

    /** 单位名称 */
    @Excel(name = "单位名称", width = 15)
    @TableField("unit_name")
    @ApiModelProperty(value = "单位名称", position = 13)
    private String unitName;
	/**耗损率*/
    @Excel(name = "耗损率", width = 15)
    @TableField("loss_rate")
    @ApiModelProperty(value = "耗损率", position = 14)
	private BigDecimal lossRate;
	/**投料管控*/
    @Excel(name = "投料管控", width = 15)
    @TableField("is_control")
    @ApiModelProperty(value = "投料管控", position = 15)
	private String control;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 16)
	private String note;

}
