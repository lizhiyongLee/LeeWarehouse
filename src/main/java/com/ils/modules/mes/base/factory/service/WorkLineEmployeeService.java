package com.ils.modules.mes.base.factory.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.WorkLineEmployee;

/**
 * @Description: 产线人员
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
public interface WorkLineEmployeeService extends IService<WorkLineEmployee> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<WorkLineEmployee> selectByMainId(String mainId);
}
