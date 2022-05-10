package com.ils.modules.mes.produce.vo;

import com.ils.common.system.base.entity.ILSEntity;
import lombok.*;

import java.util.List;

/**
 * @author lishaojie
 * @description 工单详情页
 * @date 2021/5/28 14:20
 */

@Data
public class WorkOrderDetailVO  extends ILSEntity {
    private  WorkOrderVO workOrderVO;
    private List<WorkOrderProgressVO> workOrderProgressVOList;
    private List<WorkOrderProcessProgressVO> workOrderProcessProgressVOList;
}
