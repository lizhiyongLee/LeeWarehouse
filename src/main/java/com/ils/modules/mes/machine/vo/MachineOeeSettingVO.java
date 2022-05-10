package com.ils.modules.mes.machine.vo;

import lombok.Data;

/**
 * @author lishaojie
 * @description
 * @date 2021/11/12 15:59
 */
@Data
public class MachineOeeSettingVO {
    /**
     * 样式（1，平铺样式；2、取图显示样式；）
     */
    private Integer style;
    /**
     * 行数
     */
    private Integer rowNum;
    /**
     * 列数
     */
    private Integer columnNum;
    /**
     * 理论产量（0，表示关闭；1表示开启；）
     */
    private Integer theoreticalYield;
    /**
     * 实际产量（0，表示关闭；1表示开启；）
     */
    private Integer realYield;
    /**
     * 性能稼动率（0，表示关闭；1表示开启；）
     */
    private Integer performanceRate;
    /**
     * 不合格产量（0，表示关闭；1表示开启；）
     */
    private Integer unqualifiedYield;
    /**
     * 不合格率（0，表示关闭；1表示开启；）
     */
    private Integer unqualifiedRate;
    /**
     * 运行时间（0，表示关闭；1表示开启；）
     */
    private Integer runningTime;
    /**
     * 停机时间（0，表示关闭；1表示开启；）
     */
    private Integer stopTime;
    /**
     * 总时间（0，表示关闭；1表示开启；）
     */
    private Integer totalTime;
    /**
     * 时间稼动率（0，表示关闭；1表示开启；）
     */
    private Integer timeRate;
    /**
     * OEE（0，表示关闭；1表示开启；）
     */
    private Integer oee;
    /**
     * 排序因子（workshop,machine_type,machine_code）
     */
    private String sortFactor;
    /**
     * 数据刷新时间（以秒为单位）
     */
    private Integer refreshTime;
    /**
     * 轮播时间（以秒为单位）
     */
    private Integer rotationTime;

}
