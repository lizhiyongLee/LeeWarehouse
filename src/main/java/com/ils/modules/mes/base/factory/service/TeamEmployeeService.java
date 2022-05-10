package com.ils.modules.mes.base.factory.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.TeamEmployee;

/**
 * @Description: 班组人员
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
public interface TeamEmployeeService extends IService<TeamEmployee> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<TeamEmployee> selectByMainId(String mainId);
}
