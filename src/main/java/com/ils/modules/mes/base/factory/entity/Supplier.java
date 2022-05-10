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
 * @Description: 供应商
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@Data
@TableName("mes_supplier")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_supplier对象", description="供应商")
public class Supplier extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**供应商编码*/
	@Excel(name = "供应商编码", width = 15)
    @ApiModelProperty(value = "供应商编码", position = 2)
    @TableField("supplier_code")
	private String supplierCode;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称", position = 3)
    @TableField("supplier_name")
	private String supplierName;
	/**联系地址*/
	@Excel(name = "联系地址", width = 15)
    @ApiModelProperty(value = "联系地址", position = 4)
    @TableField("address")
	private String address;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人", position = 5)
    @TableField("contact")
	private String contact;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
    @ApiModelProperty(value = "联系电话", position = 6)
    @TableField("phone")
	private String phone;
	/**邮箱*/
	@Excel(name = "邮箱", width = 15)
    @ApiModelProperty(value = "邮箱", position = 7)
    @TableField("email")
	private String email;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件", position = 8)
    @TableField("attach")
	private String attach;
	/**状态：1，启用，0，停用；*/
	@Excel(name = "状态：1，启用，0，停用；", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态：1，启用，0，停用；", position = 9)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
	private String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 10)
    @TableField("note")
	private String note;
}
