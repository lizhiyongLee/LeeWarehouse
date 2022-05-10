package com.ils.modules.mes.machine.vo;

import com.ils.modules.mes.machine.entity.MachineStopTime;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/11/12 16:12
 */
@Data
public class MachineOeeDataVO {
    /**
     * 设备id
     */
    private String machineId;
    /**
     * 设备名称
     */
    private String machineName;
    /**
     * 设备编码
     */
    private String machineCode;
    /**
     * 运行状态
     */
    private String runningState;
    /**
     * 理论产量
     */
    private BigDecimal theoreticalYield;
    /**
     * 实际产量
     */
    private BigDecimal realYield;
    /**
     * 性能稼动率
     */
    private BigDecimal performanceRate;
    /**
     * 不合格产量
     */
    private BigDecimal unqualifiedYield;
    /**
     * 不合格率
     */
    private BigDecimal unqualifiedRate;
    /**
     * 合格率
     */
    private BigDecimal qualifiedRate;
    /**
     * 运行时间
     */
    private Long runningTime;
    /**
     * 停机时间
     */
    private Long stopTime;
    /**
     * 停机时间列表
     */
    List<MachineStopTime> machineStopTimeList;
    /**
     * 总时间
     */
    private Long totalTime;
    /**
     * 时间稼动率
     */
    private BigDecimal timeRate;
    /**
     * OEE
     */
    private BigDecimal oee;
    /**
     * 停机状态缩略图
     */
    private String stopPicture;
    /**
     * 运行状态缩略图
     */
    private String runningPicture;
}
