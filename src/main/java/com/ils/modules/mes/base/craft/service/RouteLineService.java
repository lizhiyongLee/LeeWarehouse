package com.ils.modules.mes.base.craft.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.craft.entity.RouteLine;
import com.ils.modules.mes.base.craft.vo.RouteLineVO;

/**
 * @Description: 工艺路线明细表
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
public interface RouteLineService extends IService<RouteLine> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
    public List<RouteLineVO> selectByMainId(String mainId);
}
