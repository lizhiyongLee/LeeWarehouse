package com.ils.modules.mes.produce.entity;

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
 * @Description: 工单物料清单明细
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_work_order_item_bom")
@ApiModel(value="mes_work_order_item_bom对象", description="工单物料清单明细")
public class WorkOrderItemBom extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**工单id*/
    @TableField("order_id")
    @ApiModelProperty(value = "工单id", position = 2)
	private String orderId;
	/**引用的物料清单id*/
    @Excel(name = "引用的物料清单id", width = 15)
    @TableField("bom_id")
    @ApiModelProperty(value = "引用的物料清单id", position = 3)
	private String bomId;
	/**物料id*/
    @Excel(name = "物料id", width = 15)
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 4)
	private String itemId;
	/**物料名称*/
    @Excel(name = "物料名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "物料名称", position = 5)
	private String itemName;
	/**物料编码*/
    @Excel(name = "物料编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "物料编码", position = 6)
	private String itemCode;
	/**数量*/
    @Excel(name = "数量", width = 15)
    @TableField("qty")
    @ApiModelProperty(value = "数量", position = 7)
	private BigDecimal qty;

    /** 本物料工单总数量 */
    @Excel(name = "本物料工单总数量", width = 15)
    @TableField("total_qty")
    @ApiModelProperty(value = "本物料工单总数量", position = 12)
    private BigDecimal totalQty;
	/**单位*/
    @Excel(name = "单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("unit")
    @ApiModelProperty(value = "单位", position = 8)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
	private String unit;
    /** 单位名称 */
    @Excel(name = "单位名称", width = 15)
    @TableField("unit_name")
    @ApiModelProperty(value = "单位名称", position = 8)
    private String unitName;

	/**耗损率*/
    @Excel(name = "耗损率", width = 15)
    @TableField("loss_rate")
    @ApiModelProperty(value = "耗损率", position = 9)
	private BigDecimal lossRate;
	/**投料管控:1,管控；0，不管控；*/
    @Excel(name = "投料管控:1,管控；0，不管控；", width = 15)
    @TableField("is_control")
    @ApiModelProperty(value = "投料管控:1,管控；0，不管控；", position = 10)
	private String control;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 11)
	private String note;
}
