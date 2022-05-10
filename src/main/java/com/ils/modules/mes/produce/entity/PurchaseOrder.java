package com.ils.modules.mes.produce.entity;

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
 * @Description: 采购清单
 * @Author: Tian
 * @Date:   2021-01-28
 * @Version: V1.0
 */
@Data
@TableName("mes_purchase_order")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_purchase_order对象", description="采购清单")
public class PurchaseOrder extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**采购清单号*/
	@Excel(name = "采购清单号", width = 15)
    @ApiModelProperty(value = "采购清单号", position = 2)
    @TableField("purchase_code")
	private String purchaseCode;
	/**供应商*/
	@Excel(name = "供应商Id", width = 15)
    @ApiModelProperty(value = "供应商", position = 3)
    @TableField("supplier_id")
	private String supplierId;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称", position = 4)
    @TableField("supplier_name")
	private String supplierName;
	/**选择人员*/
	@Excel(name = "选择人员", width = 15, dicCode = "id", dictTable = "sys_user", dicText = "realname")
    @ApiModelProperty(value = "选择人员", position = 5)
    @TableField("handperson_id")
	@Dict(dicCode = "id",dictTable = "sys_user",dicText = "realname")
	private String handpersonId;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 6)
    @TableField("note")
	private String note;
	/**1、新建；2、已申请；3、取消；4、结束；*/
	@Excel(name = "1、新建；2、已申请；3、取消；4、结束；", width = 15, dicCode = "mesPurchaseOrderStatus")
    @ApiModelProperty(value = "1、新建；2、已申请；3、取消；4、结束；", position = 7)
    @TableField("status")
	@Dict(dicCode = "mesPurchaseOrderStatus")
	private String status;
}
