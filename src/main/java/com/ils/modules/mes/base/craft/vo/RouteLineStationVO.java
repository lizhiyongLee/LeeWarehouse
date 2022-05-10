package com.ils.modules.mes.base.craft.vo;

import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.base.craft.entity.RouteLineStation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 工艺路线工序关联工位
 * @author: fengyi
 * @date: 2020年11月2日 下午3:22:30
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RouteLineStationVO extends RouteLineStation {

    /** 工位编码 */
    private String stationCode;
    /** 工位名称 */
    @Excel(name = "工位名称", width = 15)
    private String stationName;
}
