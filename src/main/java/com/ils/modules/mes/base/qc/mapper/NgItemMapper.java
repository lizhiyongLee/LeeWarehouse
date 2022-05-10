package com.ils.modules.mes.base.qc.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.qc.entity.NgItem;
import com.ils.modules.mes.base.qc.vo.NgItemVO;

/**
 * @Description: 不良类型原因
 * @Author: fengyi
 * @Date: 2020-10-19
 * @Version: V1.0
 */
public interface NgItemMapper extends ILSMapper<NgItem> {

    /**
     * 
     * 查询不良品信息
     * 
     * @param page
     * @param queryWrapper
     * @return IPage
     * @date 2020年7月28日O
     */
    IPage<NgItemVO> queryPageList(IPage<NgItemVO> page,
        @Param("ew") QueryWrapper<NgItemVO> queryWrapper);
}
