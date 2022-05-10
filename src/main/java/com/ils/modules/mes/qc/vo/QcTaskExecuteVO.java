package com.ils.modules.mes.qc.vo;

import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.qc.entity.QcTaskItemStandard;
import com.ils.modules.mes.qc.entity.QcTaskReport;
import com.ils.modules.mes.qc.entity.QcTaskReportItemValue;
import lombok.*;

import java.util.List;

/**
 * 提交质检任务数据VO
 *
 * @author Anna.
 * @date 2021/5/10 16:16
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class QcTaskExecuteVO {

    /**
     * 质检报告记录值
     */
    private List<QcTaskReportItemValue> qcTaskReportItemValueList;

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
