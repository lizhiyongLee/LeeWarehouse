package com.ils.modules.mes.qc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.aspect.annotation.KeyWord;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @Description: 质检任务
 * @Author: Tian
 * @Date: 2021-03-01
 * @Version: V1.0
 */
@Data
@TableName("mes_qc_task")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_qc_task对象", description = "质检任务")
public class QcTask extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 质检类型:1,入厂检；2，出厂检；3，首检；4，生产检，5，巡检，6，普通检
     */
    @Excel(name = "质检类型:1,入厂检；2，出厂检；3，首检；4，生产检，5，巡检，6，普通检", width = 15, dicCode = "mesQcType")
    @ApiModelProperty(value = "质检类型:1,入厂检；2，出厂检；3，首检；4，生产检，5，巡检，6，普通检", position = 2)
    @TableField("qc_type")
    @Dict(dicCode = "mesQcType")
    private String qcType;
    @Excel(name = "质检类别", width = 15, dicCode = "mesQcCatelog")
    @ApiModelProperty(value = "质检类别", position = 2)
    @TableField("qc_catalog")
    @Dict(dicCode = "mesQcCatelog")
    private String qcCatelog;
    /**
     * 质检任务编码
     */
    @KeyWord
    @Excel(name = "质检任务编码", width = 15)
    @ApiModelProperty(value = "质检任务编码", position = 3)
    @TableField("task_code")
    private String taskCode;
    /**
     * 原始任务编码,对于复检的质检任务才有
     */
    @Excel(name = "原始任务编码,对于复检的质检任务才有", width = 15)
    @ApiModelProperty(value = "原始任务编码,对于复检的质检任务才有", position = 4)
    @TableField("raw_task_code")
    private String rawTaskCode;

    /**
     * 关联单据类型
     */
    @Excel(name = "关联单据类型", width = 15)
    @ApiModelProperty(value = "关联单据类型", position = 4)
    @TableField("related_receipt_type")
    private String relatedReceiptType;

    /**
     * 关联单据id
     */
    @Excel(name = "关联单据id", width = 15)
    @ApiModelProperty(value = "关联单据id", position = 4)
    @TableField("related_receipt_id")
    private String relatedReceiptId;

    /**
     * 关联单据编码
     */
    @Excel(name = "关联单据编码", width = 15)
    @ApiModelProperty(value = "关联单据编码", position = 4)
    @TableField("related_receipt_code")
    private String relatedReceiptCode;

    /**
     * 质检方案id
     */
    @Excel(name = "质检方案id", width = 15)
    @ApiModelProperty(value = "质检方案id", position = 5)
    @TableField("method_id")
    private String methodId;
    /**
     * 质检方案名称
     */
    @Excel(name = "质检方案名称", width = 15)
    @ApiModelProperty(value = "质检方案名称", position = 6)
    @TableField("method_name")
    private String methodName;
    /**
     * 质检物料id
     */
    @Excel(name = "质检物料id", width = 15)
    @ApiModelProperty(value = "质检物料id", position = 7)
    @TableField("item_id")
    private String itemId;
    /**
     * 质检物料编码
     */
    @Excel(name = "质检物料编码", width = 15)
    @ApiModelProperty(value = "质检物料编码", position = 8)
    @TableField("item_code")
    private String itemCode;
    /**
     * 质检物料名称
     */
    @Excel(name = "质检物料名称", width = 15)
    @ApiModelProperty(value = "质检物料名称", position = 9)
    @TableField("item_name")
    private String itemName;
    /**
     * 1、未开始；2、生产；3、暂停；4、结束；5、取消；
     */
    @Excel(name = "1、未开始；2、生产；3、暂停；4、结束；5、取消；", width = 15, dicCode = "mesExeStatus")
    @ApiModelProperty(value = "1、未开始；2、生产；3、暂停；4、结束；5、取消；", position = 10)
    @TableField("exe_status")
    @Dict(dicCode = "mesExeStatus")
    private String exeStatus;
    /**
     * 质检结果：1、合格；2、不合格；
     */
    @Excel(name = "质检结果：1、合格；2、不合格；", width = 15, dicCode = "mesQcResult")
    @ApiModelProperty(value = "质检结果：1、合格；2、不合格；", position = 11)
    @TableField("qc_result")
    @Dict(dicCode = "mesQcResult")
    private String qcResult;
    /**
     * 位置类型 1，仓位。2，工位
     */
    @Excel(name = "位置类型", width = 15)
    @ApiModelProperty(value = "位置类型", position = 12)
    @TableField("location_type")
    private String locationType;
    /**
     * 位置id：当做生产检，首检，巡检时存工位id；入厂检，出厂检存仓位id,通用检根据情况存id。
     */
    @Excel(name = "位置id：当做生产检，首检，巡检时存工位id；入厂检，出厂检存仓位id,通用检根据情况存id。", width = 15)
    @ApiModelProperty(value = "位置id：当做生产检，首检，巡检时存工位id；入厂检，出厂检存仓位id,通用检根据情况存id。", position = 12)
    @TableField("location_id")
    private String locationId;
    /**
     * 位置名称：当做生产检，首检，巡检时存工位名称；入厂检，出厂检存仓位名称,通用检根据情况存名称。
     */
    @Excel(name = "位置名称：当做生产检，首检，巡检时存工位名称；入厂检，出厂检存仓位名称,通用检根据情况存名称。", width = 15)
    @ApiModelProperty(value = "位置名称：当做生产检，首检，巡检时存工位名称；入厂检，出厂检存仓位名称,通用检根据情况存名称。", position = 13)
    @TableField("location_name")
    private String locationName;
    /**
     * 位置编码：当做生产检，首检，巡检时存工位编码；入厂检，出厂检存仓位编码,通用检根据情况存编码。
     */
    @Excel(name = "位置编码：当做生产检，首检，巡检时存工位编码；入厂检，出厂检存仓位编码,通用检根据情况存编码。", width = 15)
    @ApiModelProperty(value = "位置编码：当做生产检，首检，巡检时存工位编码；入厂检，出厂检存仓位编码,通用检根据情况存编码。", position = 14)
    @TableField("location_code")
    private String locationCode;
    /**
     * 工单id
     */
    @Excel(name = "工单id", width = 15)
    @ApiModelProperty(value = "工单id", position = 15)
    @TableField("order_id")
    private String orderId;
    /**
     * 工单号
     */
    @Excel(name = "工单号", width = 15)
    @ApiModelProperty(value = "工单号", position = 16)
    @TableField("order_no")
    private String orderNo;
    /**
     * 批次号
     */
    @Excel(name = "批次号", width = 15)
    @ApiModelProperty(value = "批次号", position = 17)
    @TableField("batch_no")
    private String batchNo;
    /**
     * 工序id
     */
    @Excel(name = "工序id", width = 15)
    @ApiModelProperty(value = "工序id", position = 18)
    @TableField("process_id")
    private String processId;
    /**
     * 工序编码
     */
    @Excel(name = "工序编码", width = 15)
    @ApiModelProperty(value = "工序编码", position = 19)
    @TableField("process_code")
    private String processCode;
    /**
     * 工序名称
     */
    @Excel(name = "工序名称", width = 15)
    @ApiModelProperty(value = "工序名称", position = 20)
    @TableField("process_name")
    private String processName;
    /**
     * 工序号
     */
    @Excel(name = "工序号", width = 15)
    @ApiModelProperty(value = "工序号", position = 21)
    @TableField("seq")
    private Integer seq;
    /**
     * 计划日期
     */
    @Excel(name = "计划日期", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "计划日期", position = 22)
    @TableField("plan_date")
    private Date planDate;
    /**
     * 班次
     */
    @Excel(name = "班次", width = 15)
    @ApiModelProperty(value = "班次", position = 23)
    @TableField("shift_id")
    private String shiftId;
    /**
     * 班组
     */
    @Excel(name = "班组", width = 15)
    @ApiModelProperty(value = "班组", position = 24)
    @TableField("team_id")
    private String teamId;
    /**
     * totalQty
     */
    @Excel(name = "totalQty", width = 15)
    @ApiModelProperty(value = "totalQty", position = 25)
    @TableField("total_qty")
    private BigDecimal totalQty;
    /**
     * sampleQty
     */
    @Excel(name = "sampleQty", width = 15)
    @ApiModelProperty(value = "sampleQty", position = 26)
    @TableField("sample_qty")
    private BigDecimal sampleQty;
    /**
     * 计划开始时间
     */
    @Excel(name = "计划开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划开始时间", position = 27)
    @TableField("plan_start_time")
    private Date planStartTime;
    /**
     * 计划结束时间
     */
    @Excel(name = "计划结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划结束时间", position = 28)
    @TableField("plan_end_time")
    private Date planEndTime;
    /**
     * 实际开始时间
     */
    @Excel(name = "实际开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际开始时间", position = 29)
    @TableField("real_start_time")
    private Date realStartTime;
    /**
     * 实际结束时间
     */
    @Excel(name = "实际结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际结束时间", position = 30)
    @TableField("real_end_time")
    private Date realEndTime;

    /**
     * 审批流id，关联审批流表。
     */
    @Excel(name = "审批流id，关联审批流表。", width = 15)
    @TableField("flow_id")
    @ApiModelProperty(value = "审批流id，关联审批流表。", position = 31)
    private String flowId;

    /**
     * 审批状态
     */
    @Excel(name = "审批状态", width = 15, dicCode = "mesAuditStatus")
    @TableField("audit_status")
    @ApiModelProperty(value = "审批状态", position = 32)
    @Dict(dicCode = "mesAuditStatus")
    private String auditStatus;

    /**
     * 关联标准作业步骤id
     */
    @Excel(name = "关联标准作业步骤", width = 15)
    @ApiModelProperty(value = "关联标准作业步骤id", position = 33)
    @TableField("sop_step_id")
    private String sopStepId;

    /**
     * 关联标准作业控件id
     */
    @Excel(name = "关联标准作业控件", width = 15)
    @ApiModelProperty(value = "关联标准作业控件id", position = 34)
    @TableField("sop_control_id")
    private String sopControlId;
    /** 备注 */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 18)
    @TableField("note")
    private String note;
}
