package com.ils.modules.mes.base.schedule.entity;

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
 * @Description: 自动排程规则设置
 * @Author: fengyi
 * @Date: 2021-02-22
 * @Version: V1.0
 */
@Data
@TableName("mes_schedule_auto_rule_configure")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_schedule_auto_rule_configure对象", description="自动排程规则设置")
public class ScheduleAutoRuleConfigure extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**三个枚举含义：按工单优先级降序；按工单计划时间升序；按工单计划结束时间升序。*/
	@Excel(name = "按工单优先级降序；按工单计划时间升序；按工单计划结束时间升序。", width = 15, dicCode = "mesScheduleAutoOrder")
    @ApiModelProperty(value = "三个枚举含义：按工单优先级降序；按工单计划时间升序；按工单计划结束时间升序。", position = 2)
    @TableField("order_rule")
	@Dict(dicCode = "mesScheduleAutoOrder")
	private String orderRule;
	/**1、常规方案；2、最小换型时间*/
	@Excel(name = "1、常规方案；2、最小换型时间", width = 15, dicCode = "mesScheduleAutoProcess")
    @ApiModelProperty(value = "1、常规方案；2、最小换型时间", position = 3)
    @TableField("process_rule")
	@Dict(dicCode = "mesScheduleAutoProcess")
	private String processRule;
	/**1、N型排程；2、Z型排程*/
	@Excel(name = "1、N型排程；2、Z型排程", width = 15, dicCode = "mesScheduleAutoStation")
    @ApiModelProperty(value = "1、N型排程；2、Z型排程", position = 4)
    @TableField("station_rule")
	@Dict(dicCode = "mesScheduleAutoStation")
	private String stationRule;
	/**1、按班次拆分；2、按设置数量拆分；3、不拆分；*/
	@Excel(name = "1、按班次拆分；2、按设置数量拆分；3、不拆分；", width = 15, dicCode = "nesScheduleAutoSplit")
    @ApiModelProperty(value = "1、按班次拆分；2、按设置数量拆分；3、不拆分；", position = 5)
    @TableField("split_rule")
	@Dict(dicCode = "nesScheduleAutoSplit")
	private String splitRule;
	/**1、正向分派；2、逆向分派。*/
	@Excel(name = "1、正向分派；2、逆向分派。", width = 15, dicCode = "nesScheduleAutoDispatch")
    @ApiModelProperty(value = "1、正向分派；2、逆向分派。", position = 6)
    @TableField("dispatch_rule")
	@Dict(dicCode = "nesScheduleAutoDispatch")
	private String dispatchRule;
	/**预留规则1*/
	@Excel(name = "预留规则1", width = 15)
    @ApiModelProperty(value = "预留规则1", position = 7)
    @TableField("reserve1")
	private String reserve1;
	/**预留规则2*/
	@Excel(name = "预留规则2", width = 15)
    @ApiModelProperty(value = "预留规则2", position = 8)
    @TableField("reserve2")
	private String reserve2;
	/**预留规则3*/
	@Excel(name = "预留规则3", width = 15)
    @ApiModelProperty(value = "预留规则3", position = 9)
    @TableField("reserve3")
	private String reserve3;
	/**预留规则4*/
	@Excel(name = "预留规则4", width = 15)
    @ApiModelProperty(value = "预留规则4", position = 10)
    @TableField("reserve4")
	private String reserve4;
	/**预留规则5*/
	@Excel(name = "预留规则5", width = 15)
    @ApiModelProperty(value = "预留规则5", position = 11)
    @TableField("reserve5")
	private String reserve5;
	/**预留规则6*/
	@Excel(name = "预留规则6", width = 15)
    @ApiModelProperty(value = "预留规则6", position = 12)
    @TableField("reserve6")
	private String reserve6;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 13)
    @TableField("note")
	private String note;
}
