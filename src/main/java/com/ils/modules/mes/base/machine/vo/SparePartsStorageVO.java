package com.ils.modules.mes.base.machine.vo;

import com.ils.modules.mes.base.machine.entity.SparePartsStorage;
import lombok.*;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2021/2/24 9:36
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SparePartsStorageVO extends SparePartsStorage {
    private static final long serialVersionUID = 1L;

    /**
     * 是否有子节点
     */
    private Integer hasChild;

    /**
     * 质量状态名称
     */
    private String qcStatusName;
}
