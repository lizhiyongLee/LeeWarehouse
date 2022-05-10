package com.ils.modules.mes.machine.vo;

import com.ils.modules.mes.machine.entity.SparePartsReceiptHead;
import com.ils.modules.mes.machine.entity.SparePartsReceiptLine;
import lombok.*;

import java.util.List;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2021/2/24 15:41
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SparePartsReceiptHeadVO extends SparePartsReceiptHead {
    private static final long serialVersionUID = 1L;
    /**
     * 备件入库物料信息集合
     */
    private List<SparePartsReceiptLine> sparePartsReceiptLineList;
}
