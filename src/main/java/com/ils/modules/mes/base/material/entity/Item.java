package com.ils.modules.mes.base.material.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.aspect.annotation.KeyWord;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 物料定义
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_item")
@ApiModel(value = "mes_item对象", description = "物料定义")
public class Item extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 物料编码
     */
    @Excel(name = "物料编码", width = 15)
    @TableField("item_code")
    @ApiModelProperty(value = "物料编码", position = 2)
    @KeyWord
    private String itemCode;
    /**
     * 物料名称
     */
    @Excel(name = "物料名称", width = 15)
    @TableField("item_name")
    @ApiModelProperty(value = "物料名称", position = 3)
    @KeyWord
    private String itemName;
    /**
     * 物料描述
     */
    @Excel(name = "物料描述", width = 15)
    @TableField("item_description")
    @ApiModelProperty(value = "物料名称", position = 4)
    private String itemDescription;
    /**
     * 物料类型
     */
    @Excel(name = "物料类型", width = 15, dictTable = "mes_item_type", dicCode = "id", dicText = "type_name")
    @TableField("item_type_id")
    @ApiModelProperty(value = "物料类型", position = 5)
    @Dict(dictTable = "mes_item_type", dicCode = "id", dicText = "type_name")
    private String itemTypeId;
    /**
     * 状态 ：1，启用，0停用；
     */
    @Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @TableField("status")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 6)
    @Dict(dicCode = "mesStatus")
    private String status;
    /**
     * 主单位
     */
    @Excel(name = "主单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("main_unit")
    @ApiModelProperty(value = "主单位", position = 7)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String mainUnit;
    /**
     * 主单位精度
     */
    @Excel(name = "主单位精度", width = 15)
    @TableField("main_accuracy")
    @ApiModelProperty(value = "主单位精度", position = 8)
    private Integer mainAccuracy;
    /**
     * 投产单位
     */
    @Excel(name = "投产单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("feed_unit")
    @ApiModelProperty(value = "投产单位", position = 9)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String feedUnit;
    /**
     * 产出单位
     */
    @Excel(name = "产出单位", width = 15, dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("output_unit")
    @ApiModelProperty(value = "产出单位", position = 10)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String outputUnit;
    /**
     * 规格
     */
    @Excel(name = "规格", width = 15)
    @TableField("spec")
    @ApiModelProperty(value = "规格", position = 11)
    private String spec;
    /**
     * 启动批次管理：1，是；0，否；
     */
    @Excel(name = "启动批次管理：1，是；0，否；", width = 15, dicCode = "mesYesZero")
    @TableField("is_batch")
    @ApiModelProperty(value = "启动批次管理：1，是；0，否；", position = 12)
    @Dict(dicCode = "mesYesZero")
    private String batch;
    /**
     * 启动二维码1，是；0，否；
     */
    @Excel(name = "启动二维码1，是；0，否；", width = 15, dicCode = "mesYesZero")
    @TableField("is_qrcode")
    @Dict(dicCode = "mesYesZero")
    @ApiModelProperty(value = "启动二维码1，是；0，否；", position = 13)
    private String qrcode;
    /**
     * 标签规则
     */
    @Excel(name = "标签规则", width = 15)
    @TableField("label_rule")
    @ApiModelProperty(value = "标签规则", position = 14)
    @Dict(dictTable = "ils_code_generator", dicText = "generator_name", dicCode = "generator_code")
    private String labelRule;
    /**
     * 附件
     */
    @Excel(name = "附件", width = 15)
    @TableField("attach")
    @ApiModelProperty(value = "附件", position = 15)
    private String attach;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 16)
    private String note;
}
