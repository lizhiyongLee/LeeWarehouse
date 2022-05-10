package com.ils.modules.mes.base.factory.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.factory.entity.WorkShopEmployee;
import com.ils.modules.mes.base.factory.vo.UserPosiztionVO;


/**
 * @Description: 车间人员
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
public interface WorkShopEmployeeMapper extends ILSMapper<WorkShopEmployee> {
    
    /**
     * 通过主表 Id 删除
     * @param mainId
     * @return
     */
	public boolean deleteByMainId(String mainId);
    
    /**
     * 通过主表 Id 查询
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
        @Param("ew") QueryWrapper<UserPosiztionVO> queryWrapper);

}
