package com.ils.modules.mes.base.factory.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.WorkstationEmployee;


/**
 * @Description: 车位人员
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
public interface WorkstationEmployeeService extends IService<WorkstationEmployee> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<WorkstationEmployee> selectByMainId(String mainId);
}
