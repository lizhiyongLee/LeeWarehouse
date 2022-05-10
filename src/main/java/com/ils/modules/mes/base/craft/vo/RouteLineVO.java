package com.ils.modules.mes.base.craft.vo;

import java.util.ArrayList;
import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.craft.entity.RouteLine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 工艺路线VO
 * @author: fengyi
 * @date: 2020年11月2日 下午1:13:16
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RouteLineVO extends RouteLine {

    private List<RouteLineStationVO> routeLineStationList;

    private List<RouteLineMethodVO> routeLineMethodList;

    private List<RouteLineParaVO> routeLineParaVOList;
}
