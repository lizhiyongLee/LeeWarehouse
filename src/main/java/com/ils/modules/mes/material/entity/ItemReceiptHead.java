package com.ils.modules.mes.material.entity;

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
 * @Description: 收货单头
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_item_receipt_head")
@ApiModel(value="mes_item_receipt_head对象", description="收货单头")
public class ItemReceiptHead extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**入库单单号*/
    @Excel(name = "入库单单号", width = 15)
    @TableField("receipt_code")
    @ApiModelProperty(value = "入库单单号", position = 2)
	private String receiptCode;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 3)
	private String note;
	/**状态：1、新建；2、下发；3、结束；4、取消；5、完成；*/
    @Excel(name = "状态：1、新建；2、下发；3、结束；4、取消；5、完成；", width = 15, dicCode = "mesReceiptStatus")
    @TableField("receipt_status")
    @ApiModelProperty(value = "状态：1、新建；2、下发；3、结束；4、取消；5、完成；", position = 4)
    @Dict(dicCode = "mesReceiptStatus")
	private String receiptStatus;
}
