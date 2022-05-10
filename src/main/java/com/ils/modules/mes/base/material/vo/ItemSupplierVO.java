package com.ils.modules.mes.base.material.vo;

import com.ils.modules.mes.base.material.entity.ItemSupplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 描述这个类的作用
 * @author: fengyi
 * @date: 2020年10月26日 下午2:34:52
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ItemSupplierVO extends ItemSupplier {
    private static final long serialVersionUID = 1L;

    /** 供应商名称 */
    private String supplierName;
}
