package com.ils.modules.mes.base.craft.entity;

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
 * @Description: 工艺路线明细表
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_route_line")
@ApiModel(value="mes_route_line对象", description="工艺路线明细表")
public class RouteLine extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**工艺id*/
    @TableField("route_id")
    @ApiModelProperty(value = "工艺id", position = 2)
	private String routeId;
	/**序号*/
    @Excel(name = "序号", width = 15)
    @TableField("seq")
    @ApiModelProperty(value = "序号", position = 3)
	private Integer seq;
	/**工序id*/
    @TableField("process_id")
    @ApiModelProperty(value = "工序id", position = 4)
	private String processId;
	/**工序编码*/
    @Excel(name = "工序编码", width = 15)
    @TableField("process_code")
    @ApiModelProperty(value = "工序编码", position = 5)
	private String processCode;
	/**工序名称*/
    @Excel(name = "工序名称", width = 15)
    @TableField("process_name")
    @ApiModelProperty(value = "工序名称", position = 6)
	private String processName;
	/**上道工序编码；如果该工序为首道工序，prior_code填充first*/
    @Excel(name = "上道工序编码", width = 15, dictTable = "mes_route_line",dicCode = "id",dicText = "process_code")
    @TableField("prior_code")
    @ApiModelProperty(value = "上道工序编码；如果该工序为首道工序，prior_code填充first", position = 7)
    @Dict(dictTable = "mes_route_line",dicCode = "id",dicText = "process_code")
	private String priorCode;
	/**下道工序编码。如果该工序为最后一道工序，next_code字段填充end。用这个两个字段目的是为了更好寻找到最后一道工序与第一道工序。*/
    @Excel(name = "下道工序编码", width = 15, dictTable = "mes_route_line",dicCode = "id",dicText = "process_code")
    @TableField("next_code")
    @ApiModelProperty(value = "下道工序编码。如果该工序为最后一道工序，next_code字段填充end。用这个两个字段目的是为了更好寻找到最后一道工序与第一道工序。", position = 8)
    @Dict(dictTable = "mes_route_line",dicCode = "id",dicText = "process_code")
	private String nextCode;
	/**1,前续开始后续可开始2，前续结束后续可开始*/
    @Excel(name = "接续方式", width = 15,dicCode = "mesLinkType")
    @TableField("link_type")
    @ApiModelProperty(value = "1,前续开始后续可开始2，前续结束后续可开始", position = 9)
    @Dict(dicCode = "mesLinkType")
	private String linkType;
	/**投产单位*/
    @Excel(name = "投产单位", width = 15,dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("in_unit")
    @ApiModelProperty(value = "投产单位", position = 10)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
	private String inUnit;
	/**产出单位*/
    @Excel(name = "产出单位", width = 15,dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    @TableField("out_unit")
    @ApiModelProperty(value = "产出单位", position = 11)
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
	private String outUnit;
	/**投产数量*/
    @Excel(name = "投产数量", width = 15)
    @TableField("in_qty")
    @ApiModelProperty(value = "投产数量", position = 12)
	private BigDecimal inQty;
	/**产出数量*/
    @Excel(name = "产出数量", width = 15)
    @TableField("out_qty")
    @ApiModelProperty(value = "产出数量", position = 13)
	private BigDecimal outQty;
	/**设备数量*/
    @Excel(name = "设备数量", width = 15)
    @TableField("machine_qty")
    @ApiModelProperty(value = "设备数量", position = 14)
	private Integer machineQty;
	/**等待时长*/
    @Excel(name = "等待时长", width = 15)
    @TableField("wait_time")
    @ApiModelProperty(value = "等待时长", position = 15)
	private BigDecimal waitTime;
	/**准备时长*/
    @Excel(name = "准备时长", width = 15)
    @TableField("setup_time")
    @ApiModelProperty(value = "准备时长", position = 16)
	private BigDecimal setupTime;
	/**1，分钟；2，小时；3，天*/
    @Excel(name = "准备时间单位", width = 15,dicCode = "mesValidatUnit")
    @TableField("setup_time_unit")
    @ApiModelProperty(value = "1，分钟；2，小时；3，天", position = 17)
    @Dict(dicCode = "mesValidatUnit")
	private String setupTimeUnit;
	/**加工时长*/
    @Excel(name = "加工时长", width = 15)
    @TableField("process_time")
    @ApiModelProperty(value = "加工时长", position = 18)
	private BigDecimal processTime;
	/**设备台时*/
    @Excel(name = "设备台时", width = 15)
    @TableField("mactime_time")
    @ApiModelProperty(value = "设备台时", position = 19)
	private BigDecimal mactimeTime;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 20)
	private String note;
}
