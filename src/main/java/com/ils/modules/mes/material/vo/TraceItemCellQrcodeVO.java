package com.ils.modules.mes.material.vo;

import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.execution.vo.WorkProduceMaterialRecordVO;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.entity.ItemTransferRecord;
import com.ils.modules.mes.qc.vo.QcTaskRecodTraceVO;
import lombok.*;

import java.util.List;

/**
 * 物料单元标签码追溯
 *
 * @author Anna.
 * @date 2021/1/18 9:40
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class TraceItemCellQrcodeVO extends ItemCell {

    private static final long serialVersionUID = 1L;
    /**
     * 产出记录
     */
    private List<WorkProduceRecord> workProduceRecordList;
    /**
     * 投料记录
     */
    private List<WorkProduceMaterialRecordVO> feedingRecordList;
    /**
     * 投向记录
     */
    private List<WorkProduceMaterialRecordVO> flowRecordList;
    /**
     * 转移记录
     */
    private List<ItemTransferRecord> itemTransferRecordList;
    /**
     * 质检记录
     */
    private List<QcTaskRecodTraceVO> qcTaskRecordTraceVOList;
    /**
     * 出入厂记录
     */
    private List<EntryAndExitRecordVO> entryAndExitRecordVOList;
}
