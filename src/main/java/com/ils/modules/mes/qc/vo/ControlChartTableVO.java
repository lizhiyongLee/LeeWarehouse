package com.ils.modules.mes.qc.vo;

import com.ils.modules.mes.material.vo.DashBoardDataVO;
import lombok.*;

import java.util.List;

/**
 * x图数据
 *
 * @author Anna.
 * @date 2021/6/24 14:16
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ControlChartTableVO {
    /**
     * r图看板数据看板
     */
    private List<DashBoardDataVO> boardDataVOList;

    /**
     * 平均值
     */
    private Double average;
    /**
     * ucl控制线
     */
    private Double highControlLine;

    /**
     * lul控制线
     */
    private Double lowControlLine;
}
