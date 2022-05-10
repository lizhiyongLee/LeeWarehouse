package com.ils.modules.mes.produce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.produce.entity.WorkPlanTask;
import com.ils.modules.mes.produce.vo.WorkPlanTaskProcessVO;
import com.ils.modules.mes.produce.vo.WorkPlanTaskVO;

/**
 * @Description: 派工单生产任务
 * @Author: fengyi
 * @Date: 2020-11-23
 * @Version: V1.0
 */
public interface WorkPlanTaskMapper extends ILSMapper<WorkPlanTask> {

    /**
     * 
     * 查询派工任务 BOM ListVO
     * 
     * @param page
     * @param queryWrapper
     * @return
     * @date 2020年11月6日
     */
    public IPage<WorkPlanTaskVO> listPage(Page<WorkPlanTaskVO> page,
        @Param("ew") QueryWrapper<WorkPlanTaskVO> queryWrapper);

    /**
     * 
     * 根据指定派工任务ID查询任务（升序）
     * 
     * @param ids
     * @return List<WorkPlanTaskProcessVO>
     * @date 2020年11月26日
     */
    public List<WorkPlanTaskProcessVO> queryPlanWorkProcessAscList(List<String> ids);

    /**
     * 
     * 根据指定派工任务ID查询任务(降序)
     * 
     * @param ids
     * @return List<WorkPlanTaskProcessVO>
     * @date 2020年11月26日
     */
    public List<WorkPlanTaskProcessVO> queryPlanWorkProcessDescList(List<String> ids);

}
