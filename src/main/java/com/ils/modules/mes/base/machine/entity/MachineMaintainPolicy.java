package com.ils.modules.mes.base.machine.entity;

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
 * @Description: 维保策略组定义
 * @Author: Tian
 * @Date: 2020-10-27
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_maintain_policy")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_machine_maintain_policy对象", description = "维保策略组定义")
public class MachineMaintainPolicy extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 1、设备；2、工装备件；
     */
    @Excel(name = "范围", width = 15, dicCode = "mesPolicyType")
    @ApiModelProperty(value = "1、设备；2、工装备件；", position = 2)
    @TableField("policy_type")
    @Dict(dicCode = "mesPolicyType")
    private String policyType;
    /**
     * 策略组名称
     */
    @Excel(name = "策略组名称", width = 15)
    @ApiModelProperty(value = "策略组名称", position = 3)
    @TableField("data_name")
    private String dataName;
    /**
     * 引用数量：指其他地方引用该策略组的所有累计数量。
     */
    @ApiModelProperty(value = "引用数量：指其他地方引用该策略组的所有累计数量。", position = 4)
    @TableField("ref_qty")
    private Integer refQty;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 5)
    @TableField("note")
    private String note;
    /**
     * 状态 ：1，启用，0停用；
     */
    @Excel(name = "状态", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 6)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
    private String status;
}
