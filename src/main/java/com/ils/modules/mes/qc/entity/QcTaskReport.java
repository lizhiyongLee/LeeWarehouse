package com.ils.modules.mes.qc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


/**
 * @Description: 质检任务报告
 * @Author: Tian
 * @Date: 2021-03-04
 * @Version: V1.0
 */
@Data
@TableName("mes_qc_task_report")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_qc_task_report对象", description = "质检任务报告")
public class QcTaskReport extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 质检任务id
     */
    @Excel(name = "质检任务id", width = 15)
    @ApiModelProperty(value = "质检任务id", position = 2)
    @TableField("qc_task_id")
    private String qcTaskId;
    /**
     * 合格数量
     */
    @Excel(name = "合格数量", width = 15)
    @ApiModelProperty(value = "合格数量", position = 3)
    @TableField("good_qty")
    private Integer goodQty;
    /**
     * 不合格数量
     */
    @Excel(name = "不合格数量", width = 15)
    @ApiModelProperty(value = "不合格数量", position = 4)
    @TableField("bad_qty")
    private Integer badQty;
    /**
     * 合格率
     */
    @Excel(name = "合格率", width = 15)
    @ApiModelProperty(value = "合格率", position = 5)
    @TableField("good_rate")
    private BigDecimal goodRate;
    /**
     * 是否合格，指本次质检总体判断是否合格。1、合格；0，不合格。
     */
    @Excel(name = "是否合格，指本次质检总体判断是否合格。1、合格；0，不合格。", width = 15)
    @ApiModelProperty(value = "是否合格，指本次质检总体判断是否合格。1、合格；0，不合格。", position = 6)
    @TableField("qc_result")
    private String qcResult;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 7)
    @TableField("note")
    private String note;
}
