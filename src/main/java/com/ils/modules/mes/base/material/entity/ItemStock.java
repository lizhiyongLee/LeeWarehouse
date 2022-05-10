package com.ils.modules.mes.base.material.entity;

import java.math.BigDecimal;

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
 * @Description: 物料关联库存
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_item_stock")
@ApiModel(value = "mes_item_stock对象", description = "物料关联库存")
public class ItemStock extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 物料id
     */
    @TableField("item_id")
    @ApiModelProperty(value = "物料id", position = 2)
    private String itemId;
    /**
     * 启动先进先出：1，是；0，否；
     */
    @Excel(name = "启动先进先出：1，是；0，否；", width = 15, dicCode = "mesYesZero")
    @TableField("is_fifo")
    @ApiModelProperty(value = "启动先进先出：1，是；0，否；", position = 3)
    @Dict(dicCode = "mesYesZero")
    private String fifo;
    /**
     * 入库单位，选单位表里的单位
     */
    @Excel(name = "入库单位，选单位表里的单位", width = 15, dicCode = "id", dictTable = "mes_unit", dicText = "unit_name")
    @TableField("in_unit")
    @ApiModelProperty(value = "入库单位，选单位表里的单位", position = 4)
    @Dict(dicCode = "id", dictTable = "mes_unit", dicText = "unit_name")
    private String inUnit;
    /**
     * 存储有效期，时间段
     */
    @Excel(name = "存储有效期，时间段", width = 15)
    @TableField("valid_time")
    @ApiModelProperty(value = "存储有效期，时间段", position = 5)
    private Integer validTime;
    /**
     * 1，分钟；2，小时；3，天；4，月；5，年
     */
    @Excel(name = "1，分钟；2，小时；3，天；4，月；5，年", width = 15, dicCode = "mesValidatUnit")
    @TableField("valid_unit")
    @ApiModelProperty(value = "1，分钟；2，小时；3，天；4，月；5，年", position = 6)
    @Dict(dicCode = "mesValidatUnit")
    private String validUnit;
    /**
     * 预警提前期
     */
    @Excel(name = "预警提前期", width = 15)
    @TableField("warn_time")
    @ApiModelProperty(value = "预警提前期", position = 7)
    private Integer warnTime;
    /**
     * 1，分钟；2，小时；3，天；4，月；5，年
     */
    @Excel(name = "1，分钟；2，小时；3，天；4，月；5，年", width = 15, dicCode = "mesValidatUnit")
    @TableField("warn_unit")
    @ApiModelProperty(value = "1，分钟；2，小时；3，天；4，月；5，年", position = 8)
    @Dict(dicCode = "mesValidatUnit")
    private String warnUnit;
    /**
     * 存储仓库
     */
    @Excel(name = "存储仓库", width = 15)
    @TableField("warehouse")
    @ApiModelProperty(value = "存储仓库", position = 9)
    private String warehouse;
    /**
     * 安全库存
     */
    @Excel(name = "安全库存", width = 15)
    @TableField("safety_stock")
    @ApiModelProperty(value = "安全库存", position = 10)
    private BigDecimal safetyStock;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 11)
    private String note;
    /**
     * 启用有效期提醒
     */
    @Excel(name = "启用有效期提醒", width = 15, dicCode = "mesYesZero")
    @TableField("valid_time_status")
    @ApiModelProperty(value = "启用有效期提醒", position = 12)
    private String validTimeStatus;
    /**
     * 启用库存提醒
     */
    @Excel(name = "启用库存提醒", width = 15, dicCode = "mesYesZero")
    @TableField("safety_status")
    @ApiModelProperty(value = "启用库存提醒", position = 13)
    private String safetyStatus;
    /**
     * 安全库存提醒频率
     */
    @Excel(name = "安全库存提醒频率", width = 15)
    @TableField("safety_warn_frequency")
    @ApiModelProperty(value = "安全库存提醒频率", position = 14)
    private Integer safetyWarnFrequency;
    /**
     * 安全库存提醒单位
     */
    @Excel(name = "安全库存提醒单位", width = 15)
    @TableField("safety_warn_unit")
    @ApiModelProperty(value = "安全库存提醒单位", position = 15)
    @Dict(dicCode = "mesValidatUnit")
    private String safetyWarnUnit;
    /**
     * 物料管理人员id
     */
    @Excel(name = "物料管理人员id", width = 15)
    @TableField("item_manager_id")
    @ApiModelProperty(value = "物料管理人员id", position = 16)
    private String itemManagerId;
    /**
     * 物料管理人员名称
     */
    @Excel(name = "物料管理人员名称", width = 15)
    @TableField("item_manager")
    @ApiModelProperty(value = "物料管理人员名称", position = 17)
    private String itemManager;
}
