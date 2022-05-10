package com.ils.modules.mes.qc.vo;

import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.qc.entity.QcTaskReport;
import com.ils.modules.mes.qc.entity.QcTaskReportItemValue;
import lombok.*;

import java.util.List;

/**
 * 质检任务结束VO
 *
 * @author Anna.
 * @date 2021/3/8 10:32
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class QcTaskEndVO {
    /**
     * 质检任务报告
     */
    private QcTaskReport qcTaskReport;
    /**
     * 待更新物流列表
     */
    private List<ItemCell> itemCellList;
    /**
     * 质检任务id。
     */
    private String qcTaskId;
}
