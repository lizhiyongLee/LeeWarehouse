package com.ils.modules.mes.sop.vo;

import com.ils.modules.mes.base.craft.vo.ProcessParaVO;
import com.ils.modules.mes.execution.entity.WorkProduceMaterialRecord;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.execution.entity.WorkProduceTaskPara;
import com.ils.modules.mes.execution.vo.GroupMaterialRecordVO;
import com.ils.modules.mes.execution.vo.WorkProduceRecordInfoVO;
import com.ils.modules.mes.material.entity.ItemTransferRecord;
import com.ils.modules.mes.qc.entity.QcTaskReport;
import com.ils.modules.mes.qc.vo.QcTaskDetailVO;
import com.ils.modules.mes.qc.vo.QcTaskVO;
import com.ils.modules.mes.report.entity.TaskReport;
import com.ils.modules.mes.sop.entity.SopControl;
import lombok.Data;

import java.util.List;

/**
 * 控件展示类
 *
 * @author Anna.
 * @date 2021/7/28 10:53
 */
@Data
public class SopControlVO extends SopControl {
    /**
     * 标签入库记录
     */
    List<ItemTransferRecord> tagInStorageRecordList;
    /**
     * 库存入库记录
     */
    List<ItemTransferRecord> stockInStorageRecordList;
    /**
     * 标签出库记录
     */
    List<ItemTransferRecord> tagOutStorageRecordList;
    /**
     * 库存出库记录
     */
    List<ItemTransferRecord> stockOutStorageRecordList;
    /**
     * 投料记录
     */
    List<WorkProduceMaterialRecord> workProduceMaterialRecordList;
    /**
     * 产出记录
     */
    List<WorkProduceRecordInfoVO> workProduceRecordList;
    /**
     * 质检报告
     */
    QcTaskDetailVO qcTaskDetailVO;
    /**
     * 生产报告
     */
    TaskReport taskReport;
    /**
     * 任务关联报告模板
     */
    List<WorkProduceTaskPara> workProduceTaskParaList;
}
