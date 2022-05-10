package com.ils.modules.mes.machine.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.modules.mes.machine.entity.MachineMaintenanceTask;
import com.ils.modules.mes.machine.entity.MachineTaskLog;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/11/27 14:24
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MaintenanceTaskDetailVO extends MachineMaintenanceTask {
    private static final long serialVersionUID = 1L;
    /**
     * 设备维修日志
     */
    private List<MachineTaskLog> machineTaskLogList;
    /**
     * 目标类别
     */
    @Dict(dictTable = "mes_machine_label", dicCode = "id", dicText = "machine_label_name")
    private String labelType;
    /**
     * 设备名称
     */
    private String machineName;
    /**
     * 设备类型
     */
    @Dict(dictTable = "mes_machine_type", dicCode = "id", dicText = "type_name")
    private String machineTypeId;
    /**
     * 设备编码
     */
    private String machineCode;
    /**
     * d电子标签
     */
    private String qrcode;
    /**
     * 车间
     */
    @Dict(dictTable = "mes_work_shop", dicCode = "id", dicText = "shop_name")
    private String workshopId;
    /**
     * 工位
     */
    @Dict(dictTable = "mes_workstation", dicCode = "id", dicText = "station_name")
    private String workStation;
    /**
     * 任务类型
     */
    @Dict(dicCode = "mesMaintainType")
    private String taskType;
    /**
     * 首次启用日期
     */
    private Date firstTurnOnDate;
    /**
     * 计划执行人
     */
    private String planExcuter;
    /**
     * 周期
     */
    private String cycle;
    /**
     * 计划工时
     */
    private String planWorkTime;

    /**
     * 策略组id
     */
    @Dict(dictTable = "mes_machine_maintain_policy", dicCode = "id", dicText = "data_name")
    private String policyGroupId;

    /**
     * 策略描述
     */
    private String policyDescription;

    /**
     * 1、固定周期 2、浮动周期 3、累计用度 4、固定用度 5、手工创建
     */
    @Dict(dicCode = "mesPolicyRule")
    private String policyRule;

    /**
     * 用度名
     */
    @Dict(dictTable = "mes_machinedataconfig", dicCode = "data_name", dicText = "data_name")
    private String ruleName;

    /**
     * qty
     */
    private Integer qty;

    /**
     * 1、分钟；2、小时；3、日；4、周；5、月
     */
    @Dict(dicCode = "mesUnit")
    private String unit;
    /**
     * 计划工时
     */
    private Integer planTime;

    /**
     * 1、分钟；2、小时；
     */
    @Dict(dicCode = "mesTimeUnit")
    private String timeUnit;

    /**
     * 模板id
     */
    @Dict(dicText = "template_name",dictTable = "mes_report_template",dicCode = "id")
    private String policyTemplateId;
    /**
     * 1、需要，0，不需要
     */
    @Dict(dicCode = "mesScan")
    private String scan;

    /**
     * 1,需要；0，不需要
     */
    @Dict(dicCode = "mesConfirm")
    private String confirm;

    /**
     * 策略开始时间
     */
    private Date startTime;

    /**
     * 策略结束时间
     */
    private Date endTime;

}
