package com.ils.modules.mes.base.craft.vo;

import java.util.ArrayList;
import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.craft.entity.Route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 工艺路线主表
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RouteVO extends Route {

	private String orderId;
	
	private static final long serialVersionUID = 1L;
	
	@ExcelCollection(name="工艺路线明细表",type = ArrayList.class)
    private List<RouteLineVO> routeLineList;
}
