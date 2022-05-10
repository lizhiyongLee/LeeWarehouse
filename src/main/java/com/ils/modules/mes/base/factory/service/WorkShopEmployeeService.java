package com.ils.modules.mes.base.factory.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.WorkShopEmployee;
import com.ils.modules.mes.base.factory.vo.UserPosiztionVO;


/**
 * @Description: 车间人员
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
public interface WorkShopEmployeeService extends IService<WorkShopEmployee> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<WorkShopEmployee> selectByMainId(String mainId);

    /**
     * 
     * 查询系统用户信息
     * 
     * @param page
     * @param queryWrapper
     * @return IPage
     * @date 2020年7月28日O
     */
    IPage<UserPosiztionVO> queryUserPositionInfo(IPage<UserPosiztionVO> page,
        QueryWrapper<UserPosiztionVO> queryWrapper);

}
