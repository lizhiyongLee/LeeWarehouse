package com.ils.modules.mes.execution.entity;

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
 * @author lishaojie
 * @description 生产任务缺陷分类记录
 * @date 2021/6/3 10:58
 */
@Data
@TableName("mes_work_produce_defect_classification")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_work_produce_defect_classification对象", description = "生产任务缺陷分类记录")
public class WorkProduceDefectClassification extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 缺陷分类id
     */
    @Excel(name = "缺陷分类id", width = 15)
    @ApiModelProperty(value = "缺陷分类id", position = 2)
    @TableField("ng_type_id")
    private String ngTypeId;

    /**
     * 缺陷分类名称
     */
    @Excel(name = "缺陷分类名称", width = 15)
    @ApiModelProperty(value = "缺陷分类名称", position = 3)
    @TableField("ng_type_name")
    private String ngTypeName;

    /**
     * 缺陷id
     */
    @Excel(name = "缺陷id", width = 15)
    @ApiModelProperty(value = "缺陷id", position = 4)
    @TableField("ng_item_id")
    private String ngItemId;

    /**
     * 缺陷名称
     */
    @Excel(name = "缺陷名称", width = 15)
    @ApiModelProperty(value = "缺陷名称", position = 5)
    @TableField("ng_item_name")
    private String ngItemName;

    /**
     * 执行生产任务id
     */
    @Excel(name = "执行生产任务id", width = 15)
    @ApiModelProperty(value = "执行生产任务id", position = 6)
    @TableField("produce_task_id")
    private String produceTaskId;

    /**
     * 工单id
     */
    @Excel(name = "工单id", width = 15)
    @ApiModelProperty(value = "工单id", position = 7)
    @TableField("order_id")
    private String orderId;

    /**
     * 工单号
     */
    @Excel(name = "工单号", width = 15)
    @ApiModelProperty(value = "工单号", position = 8)
    @TableField("order_no")
    private String orderNo;

    /**
     * 工序id
     */
    @Excel(name = "工序id", width = 15)
    @ApiModelProperty(value = "工序id", position = 9)
    @TableField("process_id")
    private String processId;

    /**
     * 工序编码
     */
    @Excel(name = "工序编码", width = 15)
    @ApiModelProperty(value = "工序编码", position = 10)
    @TableField("process_code")
    private String processCode;

    /**
     * 工序名称
     */
    @Excel(name = "工序名称", width = 15)
    @ApiModelProperty(value = "工序名称", position = 11)
    @TableField("process_name")
    private String processName;

    /**
     * 产品id
     */
    @Excel(name = "产品id", width = 15)
    @ApiModelProperty(value = "产品id", position = 12)
    @TableField("item_id")
    private String itemId;

    /**
     * 产品编码
     */
    @Excel(name = "产品编码", width = 15)
    @ApiModelProperty(value = "产品编码", position = 13)
    @TableField("item_code")
    private String itemCode;

    /**
     * 产品名称
     */
    @Excel(name = "产品名称", width = 15)
    @ApiModelProperty(value = "产品名称", position = 14)
    @TableField("item_name")
    private String itemName;

    /**
     * 报工数量
     */
    @Excel(name = "报工数量", width = 15)
    @ApiModelProperty(value = "报工数量", position = 15)
    @TableField("qty")
    private BigDecimal qty;

    /**
     * 工位id
     */
    @Excel(name = "工位id", width = 15)
    @ApiModelProperty(value = "工位id", position = 16)
    @TableField("station_id")
    private String stationId;

    /**
     * 工位编码
     */
    @Excel(name = "工位编码", width = 15)
    @ApiModelProperty(value = "工位编码", position = 17)
    @TableField("station_code")
    private String stationCode;

    /**
     * 工位名称
     */
    @Excel(name = "工位名称", width = 15)
    @ApiModelProperty(value = "工位名称", position = 18)
    @TableField("station_name")
    private String stationName;


}
