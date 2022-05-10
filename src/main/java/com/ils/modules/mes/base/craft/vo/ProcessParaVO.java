package com.ils.modules.mes.base.craft.vo;

import com.ils.modules.mes.base.craft.entity.ProcessParaDetail;
import com.ils.modules.mes.base.craft.entity.ProcessParaHead;
import lombok.Data;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/18 11:20
 */
@Data
public class ProcessParaVO extends ProcessParaHead {
  private List<ProcessParaDetail> processParaDetailList;
}
