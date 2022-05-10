package com.ils.modules.mes.base.ware.entity;

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
 * @Description: 仓库移动事务
 * @Author: Tian
 * @Date:   2021-02-08
 * @Version: V1.0
 */
@Data
@TableName("mes_ware_house_move_event")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_ware_house_move_event对象", description="仓库移动事务")
public class WareHouseMoveEvent extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**1、库存出库；2、标签出库；*/
	@Excel(name = "1、库存出库；2、标签出库；", width = 15, dicCode = "mesModelName")
    @ApiModelProperty(value = "1、库存出库；2、标签出库；", position = 2)
    @TableField("model_name")
	@Dict(dicCode = "mesModelName")
	private String modelName;
	/**事务编码*/
	@Excel(name = "事务编码", width = 15)
    @ApiModelProperty(value = "事务编码", position = 3)
    @TableField("event_code")
	private String eventCode;
	/**事务名称*/
	@Excel(name = "事务名称", width = 15)
    @ApiModelProperty(value = "事务名称", position = 4)
    @TableField("event_name")
	private String eventName;
	/**1、工单；2、销售订单；*/
	@Excel(name = "1、工单；2、销售订单；", width = 15, dicCode = "mesEventObject")
    @ApiModelProperty(value = "1、工单；2、销售订单；", position = 5)
    @TableField("event_object")
	@Dict(dicCode = "mesEventObject")
	private String eventObject;
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 6)
    @TableField("status")
	@Dict(dicCode = "mesStatus")
	private String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 7)
    @TableField("note")
	private String note;
}
