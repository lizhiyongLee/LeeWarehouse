package com.ils.modules.mes.base.craft.vo;

import com.ils.modules.mes.base.craft.entity.RouteLineParaDetail;
import com.ils.modules.mes.base.craft.entity.RouteLineParaHead;
import lombok.Data;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/18 14:44
 */
@Data
public class RouteLineParaVO extends RouteLineParaHead {
    private List<RouteLineParaDetail> routeLineParaDetailList;
}
