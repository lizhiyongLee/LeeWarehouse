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
 * @Description: 质检报告记录值
 * @Author: Tian
 * @Date: 2021-03-04
 * @Version: V1.0
 */
@Data
@TableName("mes_qc_task_report_item_value")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_qc_task_report_item_value对象", description = "质检报告记录值")
public class QcTaskReportItemValue extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 质检任务id
     */
    @Excel(name = "质检任务id", width = 15)
    @ApiModelProperty(value = "质检任务id", position = 2)
    @TableField("qc_task_id")
    private String qcTaskId;
    /**
     * 质检报告id
     */
    @Excel(name = "质检报告id", width = 15)
    @ApiModelProperty(value = "质检报告id", position = 3)
    @TableField("qc_task_report_id")
    private String qcTaskReportId;
    /**
     * 质检标准id
     */
    @Excel(name = "质检标准id", width = 15)
    @ApiModelProperty(value = "质检标准id", position = 4)
    @TableField("qc_task_standard_id")
    private String qcTaskStandardId;
    /**
     * 质检项id
     */
    @Excel(name = "质检项id", width = 15)
    @ApiModelProperty(value = "质检项id", position = 5)
    @TableField("item_id")
    private String itemId;
    /**
     * 样本序号
     */
    @Excel(name = "样本序号", width = 15)
    @ApiModelProperty(value = "样本序号", position = 6)
    @TableField("sample_seq")
    private Integer sampleSeq;
    /**
     * 质检值：质检值会根据质检标准所选的类型，填入文字，数字或是否。所以将该字段定义成了varchar。需要在代码中比较大小时可以根据质检项标准将其转换为相应的数字。
     */
    @Excel(name = "质检值：质检值会根据质检标准所选的类型，填入文字，数字或是否。所以将该字段定义成了varchar。需要在代码中比较大小时可以根据质检项标准将其转换为相应的数字。", width = 15)
    @ApiModelProperty(value = "质检值：质检值会根据质检标准所选的类型，填入文字，数字或是否。所以将该字段定义成了varchar。需要在代码中比较大小时可以根据质检项标准将其转换为相应的数字。", position = 7)
    @TableField("qc_check_value")
    private String qcCheckValue;
    /**
     * 是否合格
     */
    @Excel(name = "是否合格", width = 15)
    @ApiModelProperty(value = "是否合格", position = 8)
    @TableField("is_ok")
    private String ok;
    /**
     * 扣减
     */
    @Excel(name = "扣减", width = 15)
    @ApiModelProperty(value = "扣减", position = 9)
    @TableField("deduction")
    private BigDecimal deduction;
    /**
     * 附件
     */
    @Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件", position = 10)
    @TableField("attach")
    private String attach;
}
