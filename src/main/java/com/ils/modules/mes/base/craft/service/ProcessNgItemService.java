package com.ils.modules.mes.base.craft.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.craft.entity.ProcessNgItem;

/**
 * @Description: 工序关联不良项
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
public interface ProcessNgItemService extends IService<ProcessNgItem> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<ProcessNgItem> selectByMainId(String mainId);
}
