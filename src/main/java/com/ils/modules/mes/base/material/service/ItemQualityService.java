package com.ils.modules.mes.base.material.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.material.entity.ItemQuality;
import com.ils.modules.mes.base.material.vo.ItemQualityVO;

/**
 * @Description: 物料关联质检方案
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
public interface ItemQualityService extends IService<ItemQuality> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
    public List<ItemQualityVO> selectByMainId(String mainId);
}
