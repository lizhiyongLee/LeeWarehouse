package com.ils.modules.mes.produce.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.WorkOrderBom;
import com.ils.modules.mes.produce.vo.WorkOrderBomVO;

import java.util.List;

/**
 * @Description: 工单产品BOM
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
public interface WorkOrderBomService extends IService<WorkOrderBom> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<WorkOrderBom> selectByMainId(String mainId);

    /**
     * 分页查询工单物料bom
     * @param page
     * @param queryWrapper
     * @return
     */
	public IPage<WorkOrderBomVO> listPageWorkOrderBom(Page<WorkOrderBomVO> page, QueryWrapper<WorkOrderBomVO> queryWrapper);
}
