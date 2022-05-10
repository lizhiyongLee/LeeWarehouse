package com.ils.modules.mes.base.qc.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.qc.entity.QcItem;
import com.ils.modules.mes.base.qc.vo.QcItemVO;

/**
 * @Description: 质检项
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
public interface QcItemMapper extends ILSMapper<QcItem> {

    /**
     * 
     * 查询质检项信息
     * 
     * @param page
     * @param queryWrapper
     * @return IPage
     * @date 2020年7月28日O
     */
    IPage<QcItemVO> queryPageList(IPage<QcItemVO> page, @Param("ew") QueryWrapper<QcItemVO> queryWrapper);
}
