package com.ils.modules.mes.produce.vo;

import com.ils.common.system.base.entity.ILSEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/8/20 13:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProgressProductsProcessVO extends ILSEntity {
  private  List<WorkOrderDetailVO> workOrderDetailVOList;
}
