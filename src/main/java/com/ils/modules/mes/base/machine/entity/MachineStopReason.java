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
 * @Description: 停机原因
 * @Author: Conner
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_stop_reason")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_machine_stop_reason对象", description = "停机原因")
public class MachineStopReason extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 停机原因
     */
    @Excel(name = "停机原因", width = 15)
    @ApiModelProperty(value = "停机原因", position = 2)
    @TableField("stop_reason")
    private String stopReason;
    /**
     * 停机原因编码
     */
    @Excel(name = "停机原因编码", width = 15)
    @ApiModelProperty(value = "停机原因编码", position = 3)
    @TableField("stop_code")
    private String stopCode;
    /**
     * 1、计划性停机；2、非计划性停机
     */
    @Excel(name = "是否计划停机", width = 15, dicCode = "mesReasonType")
    @ApiModelProperty(value = "1、计划性停机；2、非计划性停机", position = 4)
    @TableField("reason_type")
    @Dict(dicCode = "mesReasonType")
    private String reasonType;

    /**
     * 状态
     */
    @Excel(name = "状态", width = 15, dicCode = "mesStatus")
    @TableField("status")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 8)
    @Dict(dicCode = "mesStatus")
    private String status;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 5)
    @TableField("note")
    private String note;
}
