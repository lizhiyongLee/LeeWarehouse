package com.ils.modules.mes.qc.vo;

import com.alibaba.fastjson.JSONArray;
import com.ils.modules.mes.qc.entity.QcTask;
import lombok.*;

import java.util.List;

/**
 * xr控制图vo类
 *
 * @author Anna.
 * @date 2021/6/24 13:51
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class XrControlChartTableVO {

    private List<QcTask> qcTaskList;

    private JSONArray tableValue;

}
