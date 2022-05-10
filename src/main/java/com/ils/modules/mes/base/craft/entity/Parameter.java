package com.ils.modules.mes.base.craft.entity;

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
 * @Description: 参数管理
 * @Author: Tian
 * @Date: 2021-10-15
 * @Version: V1.0
 */
@Data
@TableName("mes_parameter")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_parameter对象", description = "参数管理")
public class Parameter extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 参数分类:1、下发参数；2、监控参数。
     */
    @Excel(name = "参数分类", width = 15,dicCode = "mesParaType")
    @ApiModelProperty(value = "参数分类:1、下发参数；2、监控参数。", position = 2)
    @TableField("para_temp_type")
    @Dict(dicCode = "mesParaTemplate")
    private String paraTempType;
    /**
     * 参数编码
     */
    @Excel(name = "参数编码", width = 15)
    @ApiModelProperty(value = "参数编码", position = 3)
    @TableField("para_code")
    private String paraCode;
    /**
     * 参数名称
     */
    @Excel(name = "参数名称", width = 15)
    @ApiModelProperty(value = "参数名称", position = 4)
    @TableField("para_name")
    private String paraName;
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
    @Excel(name = "状态", width = 15,dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 6)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
    private String status;
}
