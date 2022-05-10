package com.ils.modules.mes.base.machine.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.ils.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.common.system.base.entity.ILSEntity;


/**
 * @Description: 设备制造商
 * @Author: Tian
 * @Date:   2020-10-28
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_manufacturer")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine_manufacturer对象", description="设备制造商")
public class MachineManufacturer extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**制造商名称*/
	@Excel(name = "制造商名称", width = 15)
    @ApiModelProperty(value = "制造商名称", position = 2)
    @TableField("manufacturer_name")
	private String manufacturerName;
	/**制造商简称*/
	@Excel(name = "制造商简称", width = 15)
    @ApiModelProperty(value = "制造商简称", position = 3)
    @TableField("simple_name")
	private String simpleName;
	/**联系地址*/
	@Excel(name = "联系地址", width = 15)
    @ApiModelProperty(value = "联系地址", position = 4)
    @TableField("adress")
	private String adress;
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
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 8)
    @TableField("status")
	@Dict(dicCode = "mesStatus")
	private String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 9)
    @TableField("note")
	private String note;
}
