package com.ils.modules.mes.produce.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.produce.entity.WorkOrderBom;
import com.ils.modules.mes.produce.vo.WorkOrderBomVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 工单产品BOM
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
public interface WorkOrderBomMapper extends ILSMapper<WorkOrderBom> {
    
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
	public List<WorkOrderBom> selectByMainId(String mainId);

    /**
     * 分页查询物料bom详情
     * @param page
     * @param queryWrapper
     * @return
     */
	public IPage<WorkOrderBomVO> listPageWorkOrderBom(Page<WorkOrderBomVO> page, @Param("ew") QueryWrapper<WorkOrderBomVO> queryWrapper);
}
