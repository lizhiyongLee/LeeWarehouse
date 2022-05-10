package com.ils.modules.mes.base.material.entity;

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
 * @author lishaojie
 * @description
 * @date 2021/10/25 9:25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_item_container_type")
@ApiModel(value = "mes_item_container_type", description = "容器容器类型")
public class ItemContainerType extends ILSEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 容器类型编码
     */
    @Excel(name = "容器类型编码", width = 15)
    @ApiModelProperty(value = "容器类型编码", position = 2)
    @TableField("type_code")
    private String typeCode;
    /**
     * 容器类型名称
     */
    @Excel(name = "容器类型名称", width = 15)
    @ApiModelProperty(value = "容器类型名称", position = 3)
    @TableField("type_name")
    private String typeName;
    /**
     * 状态 ：1，启用，0停用；
     */
    @Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
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
