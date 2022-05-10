package com.ils.modules.mes.material.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.modules.mes.material.entity.ItemCell;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lishaojie
 * @description 出入厂记录
 * @date 2021/6/8 11:03
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class EntryAndExitRecordVO extends ILSEntity {
    /**
     * 0:收货
     * 1:发货
     */
    private String type;
    /**
     * 数量
     */
    private BigDecimal qty;

    /**
     * 质检状态
     */
    @Dict(dicCode = "mesQcStatus")
    private String qualityStatus;
    /**
     * 仓库编码
     */
    private String storageCode;
    /**
     * 仓库名称
     */
    private String storageName;
}
