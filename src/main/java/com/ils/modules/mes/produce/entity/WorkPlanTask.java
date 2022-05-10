package com.ils.modules.mes.produce.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @Description: 派工单生产任务
 * @Author: fengyi
 * @Date: 2020-11-23
 * @Version: V1.0
 */
@Data
@TableName("mes_work_plan_task")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_work_plan_task对象", description = "派工单生产任务")
public class WorkPlanTask extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 生产任务单号
     */
    @Excel(name = "生产任务单号", width = 15)
    @ApiModelProperty(value = "生产任务单号", position = 2)
    @TableField("task_code")
    private String taskCode;
    /**
     * 1、任务排他2、任务共接
     */
    @Excel(name = "1、任务排他2、任务共接", width = 15, dicCode = "mesPlanTaskType")
    @ApiModelProperty(value = "1、任务排他2、任务共接", position = 3)
    @TableField("task_type")
    @Dict(dicCode = "mesPlanTaskType")
    private String taskType;
    /**
     * 关联工单工序任务id
     */
    @Excel(name = "关联工单工序任务id", width = 15)
    @ApiModelProperty(value = "关联工单工序任务id", position = 4)
    @TableField("process_task_id")
    private String processTaskId;
    /**
     * 工单id
     */
    @Excel(name = "工单id", width = 15)
    @ApiModelProperty(value = "工单id", position = 5)
    @TableField("order_id")
    private String orderId;
    /**
     * 工单号
     */
    @Excel(name = "工单号", width = 15)
    @ApiModelProperty(value = "工单号", position = 6)
    @TableField("order_no")
    private String orderNo;
    /**
     * 批次号
     */
    @Excel(name = "批次号", width = 15)
    @ApiModelProperty(value = "批次号", position = 7)
    @TableField("batch_no")
    private String batchNo;
    /**
     * 工序id
     */
    @Excel(name = "工序id", width = 15)
    @ApiModelProperty(value = "工序id", position = 8)
    @TableField("process_id")
    private String processId;
    /**
     * 工序编码
     */
    @Excel(name = "工序编码", width = 15)
    @ApiModelProperty(value = "工序编码", position = 9)
    @TableField("process_code")
    private String processCode;
    /**
     * 工序名称
     */
    @Excel(name = "工序名称", width = 15)
    @ApiModelProperty(value = "工序名称", position = 10)
    @TableField("process_name")
    private String processName;

    /**
     * 工序
     */
    @Excel(name = "工序", width = 15)
    @ApiModelProperty(value = "工序", position = 12)
    @TableField("seq")
    private Integer seq;

    /**
     * 产品
     */
    @Excel(name = "产品", width = 15)
    @ApiModelProperty(value = "产品", position = 11)
    @TableField("item_id")
    private String itemId;
    /**
     * 产品编码
     */
    @Excel(name = "产品编码", width = 15)
    @ApiModelProperty(value = "产品编码", position = 12)
    @TableField("item_code")
    private String itemCode;
    /**
     * 产品名称
     */
    @Excel(name = "产品名称", width = 15)
    @ApiModelProperty(value = "产品名称", position = 13)
    @TableField("item_name")
    private String itemName;
    /**
     * 工位id
     */
    @Excel(name = "工位id", width = 15)
    @ApiModelProperty(value = "工位id", position = 14)
    @TableField("station_id")
    private String stationId;
    /**
     * 工位编码
     */
    @Excel(name = "工位编码", width = 15)
    @ApiModelProperty(value = "工位编码", position = 15)
    @TableField("station_code")
    private String stationCode;
    /**
     * 工位名称
     */
    @Excel(name = "工位名称", width = 15)
    @ApiModelProperty(value = "工位名称", position = 16)
    @TableField("station_name")
    private String stationName;
    /**
     * 排程数量
     */
    @Excel(name = "排程数量", width = 15)
    @ApiModelProperty(value = "排程数量", position = 17)
    @TableField("plan_qty")
    private BigDecimal planQty;
    /**
     * 单位
     */
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位", position = 18)
    @TableField("unit")
    private String unit;
    /**
     * 1、指定给班组（用户组）2、指定给人员（用户）
     */
    @Excel(name = "1、指定给班组（用户组）2、指定给人员（用户）", width = 15, dicCode = "mesTaskUserType")
    @ApiModelProperty(value = "1、指定给班组（用户组）2、指定给人员（用户）", position = 19)
    @TableField("user_type")
    @Dict(dicCode = "mesTaskUserType")
    private String userType;
    /**
     * 指定班组ID，单选，在主表中体现。
     */
    @Excel(name = "指定班组ID，单选，在主表中体现。", width = 15, dictTable = "mes_team", dicCode = "id", dicText = "team_name")
    @ApiModelProperty(value = "指定班组ID，单选，在主表中体现。", position = 20)
    @TableField("team_id")
    @Dict(dictTable = "mes_team", dicCode = "id", dicText = "team_name")
    private String teamId;
    /**
     * 1,排时间；2，排班次；
     */
    @Excel(name = "1,排时间；2，排班次；", width = 15, dicCode = "mesTaskPlanType")
    @ApiModelProperty(value = "1,排时间；2，排班次；", position = 21)
    @TableField("plan_type")
    @Dict(dicCode = "mesTaskPlanType")
    private String planType;
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
    @Excel(name = "班次", width = 15, dictTable = "mes_shift", dicCode = "id", dicText = "shift_name")
    @ApiModelProperty(value = "班次", position = 23)
    @TableField("shift_id")
    @Dict(dictTable = "mes_shift", dicCode = "id", dicText = "shift_name")
    private String shiftId;
    /**
     * 计划开始时间
     */
    @Excel(name = "计划开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划开始时间", position = 24)
    @TableField("plan_start_time")
    private Date planStartTime;
    /**
     * 计划时长
     */
    @Excel(name = "计划时长", width = 15)
    @ApiModelProperty(value = "计划时长", position = 25)
    @TableField("plan_time")
    private BigDecimal planTime;
    /**
     * 单位:1,分；2，小时；3，天；
     */
    @Excel(name = "单位:1,分；2，小时；3，天；", width = 15, dicCode = "mesScheduleUnit")
    @ApiModelProperty(value = "单位:1,分；2，小时；3，天；", position = 26)
    @TableField("time_unit")
    @Dict(dicCode = "mesScheduleUnit")
    private String timeUnit;
    /**
     * 计划完工时间
     */
    @Excel(name = "计划完工时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "计划完工时间", position = 27)
    @TableField("plan_end_time")
    private Date planEndTime;
    /**
     * 实际开始时间
     */
    @Excel(name = "实际开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际开始时间", position = 28)
    @TableField("real_start_time")
    private Date realStartTime;
    /**
     * 实际完工时间
     */
    @Excel(name = "实际完工时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际完工时间", position = 29)
    @TableField("real_end_time")
    private Date realEndTime;

    /**
     * 下发时间
     */
    @Excel(name = "下发时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "下发时间", position = 29)
    @TableField("distrubute_time")
    private Date distrubuteTime;

    /**
     * 实际产量
     */
    @Excel(name = "实际产量", width = 15)
    @ApiModelProperty(value = "实际产量", position = 30)
    @TableField("total_qty")
    private BigDecimal totalQty;
    /**
     * 1、锁定；0、释放；
     */
    @Excel(name = "1、锁定；0、释放；", width = 15, dicCode = "lock_status")
    @ApiModelProperty(value = "1、锁定；0、释放；", position = 31)
    @TableField("lock_status")
    @Dict(dicCode = "lock_status")
    private String lockStatus;
    /**
     * 1、已排程；2、已下发；
     */
    @Excel(name = "1、已排程；2、已下发；", width = 15, dicCode = "mesPlanTaskPlanStatus")
    @ApiModelProperty(value = "1、已排程；2、已下发；", position = 32)
    @TableField("plan_status")
    @Dict(dicCode = "mesPlanTaskPlanStatus")
    private String planStatus;
    /**
     * 1、未开始；2、生产；3、暂停；4、结束；5、取消；
     */
    @Excel(name = "1、未开始；2、生产；3、暂停；4、结束；5、取消；", width = 15, dicCode = "mesPlanTaskExeStatus")
    @ApiModelProperty(value = "1、未开始；2、生产；3、暂停；4、结束；5、取消；", position = 33)
    @TableField("exe_status")
    @Dict(dicCode = "mesPlanTaskExeStatus")
    private String exeStatus;
    /**
     * 1、 是；0，否；
     */
    @Excel(name = "是否检查库存：1、 是；0，否；", width = 15, dicCode = "mesYesZero")
    @ApiModelProperty(value = "1、 是；0，否；", position = 34)
    @TableField("is_stock_check")
    @Dict(dicCode = "mesYesZero")
    private String stockCheck;
}
