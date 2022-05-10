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
 * @Description: 备件类型
 * @Author: Tian
 * @Date: 2021-02-23
 * @Version: V1.0
 */
@Data
@TableName("mes_spare_parts_type")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_spare_parts_type对象", description = "备件类型")
public class SparePartsType extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 备品备件类型编码
     */
    @Excel(name = "备品备件类型编码", width = 15)
    @ApiModelProperty(value = "备品备件类型编码", position = 2)
    @TableField("type_code")
    private String typeCode;
    /**
     * 备品备件类型名称
     */
    @Excel(name = "备品备件类型名称", width = 15)
    @ApiModelProperty(value = "备品备件类型名称", position = 3)
    @TableField("type_name")
    private String typeName;
    /**
     * 状态 ：1，启用，0停用；
     */
    @Excel(name = "状态", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 4)
    @TableField("status")
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
