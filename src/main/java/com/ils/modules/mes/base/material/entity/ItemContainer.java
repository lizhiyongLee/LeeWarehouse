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
 * @date 2021/10/25 9:27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_item_container")
@ApiModel(value = "mes_item_container对象", description = "物料载具定义")
public class ItemContainer extends ILSEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 载具编码
     */
    @Excel(name = "载具编码", width = 15)
    @ApiModelProperty(value = "载具编码", position = 2)
    @TableField("container_code")
    private String containerCode;
    /**
     * 载具名称
     */
    @Excel(name = "载具名称", width = 15)
    @ApiModelProperty(value = "载具名称", position = 3)
    @TableField("container_name")
    private String containerName;
    /**
     * 载具描述
     */
    @Excel(name = "载具描述", width = 15)
    @ApiModelProperty(value = "载具描述", position = 4)
    @TableField("container_description")
    private String containerDescription;
    /**
     * 载具类型
     */
    @Excel(name = "载具类型", width = 15)
    @ApiModelProperty(value = "载具类型", position = 5)
    @TableField("container_type_id")
    @Dict(dictTable = "mes_item_container_type", dicCode = "id", dicText = "type_name")
    private String containerTypeId;
    /**
     * 状态 ：1，启用，0停用；
     */
    @Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 6)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
    private String status;
    /**
     * 标签码
     */
    @Excel(name = "标签码", width = 15)
    @ApiModelProperty(value = "标签码", position = 7)
    @TableField("qrcode")
    private String qrcode;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 8)
    @TableField("note")
    private String note;
    /**
     * 附件
     */
    @Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件", position = 9)
    @TableField("attach")
    private String attach;
}
