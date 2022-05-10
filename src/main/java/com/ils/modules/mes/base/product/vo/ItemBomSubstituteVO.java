package com.ils.modules.mes.base.product.vo;

import com.ils.modules.mes.base.product.entity.ItemBomSubstitute;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 物料替代物
 * @author: fengyi
 * @date: 2020年10月26日 下午5:45:36
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ItemBomSubstituteVO extends ItemBomSubstitute {

    private static final long serialVersionUID = 1L;

    /** 物料名称 */
    private String itemName;

    /** 物料编码 */
    private String itemCode;

}
