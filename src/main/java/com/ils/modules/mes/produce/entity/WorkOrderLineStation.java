package com.ils.modules.mes.produce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.system.base.entity.ILSEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 工单工序对应工位
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_work_order_line_station")
@ApiModel(value = "mes_work_order_line_station", description = "工单工序对应工位")
public class WorkOrderLineStation extends ILSEntity {
    private static final long serialVersionUID = 1L;

    @TableField("related_type")
    @ApiModelProperty(value = "工单可能对应：1、工艺路线；2、生产bom。", position = 2)
    private String relatedType;

    @TableField("work_order_line_id")
    @ApiModelProperty(value = "工单工艺路线id", position = 3)
    private String workOrderLineId;

    @TableField("seq")
    @ApiModelProperty(value = "序号", position = 4)
    private Integer seq;
    /**
     * 工序id
     */
    @TableField("process_id")
    @ApiModelProperty(value = "工序id", position = 5)
    private String processId;
    /**
     * 工序编码
     */
    @TableField("process_code")
    @ApiModelProperty(value = "工序编码", position = 6)
    private String processCode;
    /**
     * 工序名称
     */
    @TableField("process_name")
    @ApiModelProperty(value = "工序名称", position = 7)
    private String processName;

    /**
     * 工位名称
     */
    @TableField("station_name")
    @ApiModelProperty(value = "工位名称", position = 8)
    private String stationName;

    /**
     * 工位id
     */
    @TableField("station_id")
    @ApiModelProperty(value = "工位id", position = 9)
    private String stationId;

}
