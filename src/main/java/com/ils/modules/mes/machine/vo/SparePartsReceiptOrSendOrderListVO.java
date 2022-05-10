package com.ils.modules.mes.machine.vo;

import com.ils.common.system.base.entity.ILSEntity;
import lombok.*;

import java.util.List;

/**
 * 备件入库明细vo
 *
 * @author Anna.
 * @date 2021/4/23 11:57
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SparePartsReceiptOrSendOrderListVO extends ILSEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 备件入库明细vo
     */
    private List<SparePartsReceiptOrSendOrderVO> sparePartsReceiptOrSendOrderVOList;
}
