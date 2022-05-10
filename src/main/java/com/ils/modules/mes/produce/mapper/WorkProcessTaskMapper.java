package com.ils.modules.mes.produce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.produce.entity.WorkProcessTask;
import com.ils.modules.mes.produce.vo.AutoScheWorkProcessVO;
import com.ils.modules.mes.produce.vo.WorkProcessTaskVO;

/**
 * @Description: 工单工序任务
 * @Author: fengyi
 * @Date: 2020-11-18
 * @Version: V1.0
 */
public interface WorkProcessTaskMapper extends ILSMapper<WorkProcessTask> {

    /**
     * 查询工序任务ListVO
     *
     * @param page
     * @param queryWrapper
     * @return
     * @date 2020年11月6日
     */
    public IPage<WorkProcessTaskVO> listPage(Page<WorkProcessTaskVO> page,
                                             @Param("ew") QueryWrapper<WorkProcessTaskVO> queryWrapper);

    /**
     * 查询工序任务ListVO(不分页)
     *
     * @param queryWrapper
     * @return
     * @date 2020年11月6日
     */
    List<WorkProcessTaskVO> listPageAll(@Param("ew") QueryWrapper<WorkProcessTaskVO> queryWrapper);

    /**
     * 查询排序的工序任务
     *
     * @param queryWrapper
     * @return
     * @date 2021年2月25日
     */
    public List<AutoScheWorkProcessVO>
    querySortWorkProcessTask(@Param("ew") QueryWrapper<AutoScheWorkProcessVO> queryWrapper);

    /**
     * 根据订单id查询工序进度
     *
     * @param orderId
     * @return
     * @date 2021年2月25日
     */
    public List<WorkProcessTaskVO> queryWorkProcessTaskVOByOrderId(String orderId);
}
