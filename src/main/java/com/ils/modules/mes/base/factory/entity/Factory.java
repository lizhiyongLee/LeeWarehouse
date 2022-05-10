package com.ils.modules.mes.base.factory.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @Description: 工厂
 * @Author: fengyi
 * @Date: 2020-10-13
 * @Version: V1.0
 */
@Data
@TableName("mes_factory")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_factory对象", description="工厂")
public class Factory extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**所属企业*/
	@Excel(name = "所属企业", width = 15)
    @ApiModelProperty(value = "所属企业", position = 2)
    @TableField("company")
	private String company;
	/**工厂编码*/
	@Excel(name = "工厂编码", width = 15)
    @ApiModelProperty(value = "工厂编码", position = 3)
    @TableField("factory_code")
	private String factoryCode;
	/**工厂名称*/
	@Excel(name = "工厂名称", width = 15)
    @ApiModelProperty(value = "工厂名称", position = 4)
    @TableField("factory_name")
	private String factoryName;
	/**0，商用；1，测试；2，渠道；3，内部使用；租户类型可以考虑用单独表。*/
	@Excel(name = "0，商用；1，测试；2，渠道；3，内部使用；租户类型可以考虑用单独表。", width = 15, dicCode = "mesFactoryTenantType")
    @ApiModelProperty(value = "0，商用；1，测试；2，渠道；3，内部使用；租户类型可以考虑用单独表。", position = 5)
    @TableField("tenant_type")
    @Dict(dicCode = "mesFactoryTenantType")
    private String tenantType;
	/**0,本地私有云；1，阿里云；2，微软云；3，华为云；这个在工厂表里不体现。*/
	@Excel(name = "0,本地私有云；1，阿里云；2，微软云；3，华为云；这个在工厂表里不体现。", width = 15, dicCode = "mesFactoryCloudEnvironment")
    @ApiModelProperty(value = "0,本地私有云；1，阿里云；2，微软云；3，华为云；这个在工厂表里不体现。", position = 6)
    @TableField("cloud_environment")
    @Dict(dicCode = "mesFactoryCloudEnvironment")
    private String cloudEnvironment;
	/**合同编码*/
	@Excel(name = "合同编码", width = 15)
    @ApiModelProperty(value = "合同编码", position = 7)
    @TableField("contract")
	private String contract;
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 8)
    @Dict(dicCode = "mesStatus")
    @TableField("status")
	private String status;
	/**合同开始日期*/
	@Excel(name = "合同开始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "合同开始日期", position = 9)
    @TableField("start_date")
	private Date startDate;
	/**合同到期日期*/
	@Excel(name = "合同到期日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "合同到期日期", position = 10)
    @TableField("end_date")
	private Date endDate;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 11)
    @TableField("note")
	private String note;
}
