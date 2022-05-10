package com.ils.modules.mes.base.material.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.modules.mes.base.material.entity.ItemQuality;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 关联质检方案VO
 * @author: fengyi
 * @date: 2020年10月26日 下午2:07:20
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ItemQualityVO extends ItemQuality {

    private static final long serialVersionUID = 1L;

    /** 质检方案 */
    private String methodName;

    /** 质检分类 */
    @Dict(dicCode = "mesQcType")
    private String qcType;
}
