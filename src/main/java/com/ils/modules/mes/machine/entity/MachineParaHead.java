package com.ils.modules.mes.machine.entity;

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
 * @Description: 设备关联参数主表
 * @Author: Tian
 * @Date: 2021-10-19
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_para_head")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_machine_para_head对象", description = "设备关联参数主表")
public class MachineParaHead extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 设备id
     */
    @Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id", position = 2)
    @TableField("machine_id")
    private String machineId;
    /**
     * 参数模板id
     */
    @Excel(name = "参数模板id", width = 15)
    @ApiModelProperty(value = "参数模板id", position = 3)
    @TableField("para_temp_id")
    private String paraTempId;
    /**
     * 参数模板名称
     */
    @Excel(name = "参数模板名称", width = 15)
    @ApiModelProperty(value = "参数模板名称", position = 4)
    @TableField("para_temp_name")
    private String paraTempName;
    /**
     * 模板分类:1,下发模板；2，监控模板。
     */
    @Excel(name = "模板分类:1,下发模板；2，监控模板。", width = 15)
    @ApiModelProperty(value = "模板分类:1,下发模板；2，监控模板。", position = 5)
    @TableField("para_temp_type")
    @Dict(dicCode = "mesParaType")
    private String paraTempType;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 6)
    @TableField("note")
    private String note;
    /**
     * 状态 ：1，启用，0停用；
     */
    @Excel(name = "状态 ：1，启用，0停用；", width = 15)
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 7)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
    private String status;
}
