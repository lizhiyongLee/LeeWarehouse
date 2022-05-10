package com.ils.modules.mes.base.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.product.entity.ItemBomSubstitute;
import com.ils.modules.mes.base.product.vo.ItemBomSubstituteVO;

/**
 * @Description: 物料BOM替代料
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
public interface ItemBomSubstituteMapper extends ILSMapper<ItemBomSubstitute> {

    /**
     * 通过物料Bom Id 删除
     * 
     * @param bomId
     * @return
     */
    public boolean deleteByMainId(String bomId);

    /**
     * 
     * 查询替代物料信息
     * 
     * @param queryWrapper
     * @return List<ItemBomSubstitute>
     * @date 2020年10月26日
     */
    List<ItemBomSubstituteVO> queryBomSubstituteMaterialInfoList(@Param("ew") QueryWrapper queryWrapper);

}
