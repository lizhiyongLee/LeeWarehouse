package com.ils.modules.mes.machine.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


/**
 * @Description: 备件入库明细
 * @Author: Tian
 * @Date:   2021-02-24
 * @Version: V1.0
 */
@Data
@TableName("mes_spare_parts_receipt_line")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_spare_parts_receipt_line对象", description="备件入库明细")
public class SparePartsReceiptLine extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**入库单表头id*/
	@Excel(name = "入库单表头id", width = 15)
    @ApiModelProperty(value = "入库单表头id", position = 2)
    @TableField("receipt_head_id")
	private String receiptHeadId;
	/**备件id*/
	@Excel(name = "备件id", width = 15)
    @ApiModelProperty(value = "备件id", position = 3)
    @TableField("spare_parts_id")
	private String sparePartsId;
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
	/**型号*/
	@Excel(name = "型号", width = 15)
    @ApiModelProperty(value = "型号", position = 6)
    @TableField("model")
	private String model;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量", position = 7)
    @TableField("qty")
	private BigDecimal qty;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位", position = 8)
    @TableField("unit")
	private String unit;
	/**相应的备件库库位*/
	@Excel(name = "相应的备件库库位", width = 15)
    @ApiModelProperty(value = "相应的备件库库位", position = 9)
    @TableField("in_storage_id")
	private String inStorageId;
}
