package com.ils.modules.mes.base.craft.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.base.craft.entity.RouteLineMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 工艺路线工序关联质检
 * @author: fengyi
 * @date: 2020年11月2日 下午3:22:54
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RouteLineMethodVO extends RouteLineMethod {
    /** 质检类型:1,入厂检；2，出厂检；3，首检；4，生产检，5，巡检，6，普通检 */
    @Excel(name = "方案名称",dicCode = "mesQcType")
    @Dict(dicCode = "mesQcType")
    private String qcType;
    /** 质检方案名称 */
    @Excel(name = "方案类型")
    private String methodName;

}
