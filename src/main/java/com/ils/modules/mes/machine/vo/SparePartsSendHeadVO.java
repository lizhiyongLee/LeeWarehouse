package com.ils.modules.mes.machine.vo;

import com.ils.modules.mes.machine.entity.SparePartsSendHead;
import com.ils.modules.mes.machine.entity.SparePartsSendLine;
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
public class SparePartsSendHeadVO extends SparePartsSendHead {
    private static final long serialVersionUID = 1L;

    /**
     * 备件出库物料信息集合
     */
    private List<SparePartsSendLine> sparePartsSendLineList;
}
