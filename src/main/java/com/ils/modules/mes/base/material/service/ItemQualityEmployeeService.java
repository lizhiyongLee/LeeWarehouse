package com.ils.modules.mes.base.material.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.material.entity.ItemQualityEmployee;

/**
 * @Description: 物料关联质检人员
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
public interface ItemQualityEmployeeService extends IService<ItemQualityEmployee> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<ItemQualityEmployee> selectByMainId(String mainId);
}
