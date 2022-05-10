package com.ils.modules.mes.qc.vo;

import lombok.*;

/**
 * 看板数据集合
 *
 * @author Anna.
 * @date 2021/6/25 11:55
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class XrDashBoardDataVO {

    private ControlChartTableVO xControlChartTableVO;

    private ControlChartTableVO rControlChartTableVO;

    private XrControlChartTableVO xrControlChartTableVO;
}
