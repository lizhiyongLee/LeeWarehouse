package com.ils.modules.mes.execution.vo;

import java.math.BigDecimal;

import com.ils.modules.mes.execution.entity.WorkProduceRecord;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 报工扩展信息
 * @author: fengyi
 * @date: 2021年1月4日 下午1:37:51
 */
@Setter
@Getter
@ToString(callSuper = true)
public class WorkProduceRecordInfoVO extends WorkProduceRecord {

    /**
     * 编辑页面总数量
     */
    private BigDecimal totalQty;

    /**
     * 编辑页面总数量
     */
    private Integer dataSize;

}
