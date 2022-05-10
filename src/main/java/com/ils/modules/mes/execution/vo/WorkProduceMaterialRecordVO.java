package com.ils.modules.mes.execution.vo;

import com.ils.modules.mes.execution.entity.WorkProduceMaterialRecord;
import lombok.*;

/**
 * 标签码查询投料记录
 *
 * @author Anna.
 * @date 2021/1/18 15:17
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorkProduceMaterialRecordVO extends WorkProduceMaterialRecord {

    /**
     * 工位
     */
    private String workStation;
    /**
     * 工序
     */
    private String processName;
    /**
     * 工单
     */
    private String orderNo;
    /**
     * 任务号
     */
    private String taskCode;
}
