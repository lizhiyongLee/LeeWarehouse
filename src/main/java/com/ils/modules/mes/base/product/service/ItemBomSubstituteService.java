package com.ils.modules.mes.base.product.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.product.entity.ItemBomSubstitute;
import com.ils.modules.mes.base.product.vo.ItemBomSubstituteVO;

/**
 * @Description: 物料BOM替代料
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
public interface ItemBomSubstituteService extends IService<ItemBomSubstitute> {

    /**
     * 添加
     * @param itemBomSubstitute
     */
    public void saveItemBomSubstitute(ItemBomSubstitute itemBomSubstitute) ;
    
    /**
     * 修改
     * @param itemBomSubstitute
     */
    public void updateItemBomSubstitute(ItemBomSubstitute itemBomSubstitute);
    
    /**
     * 删除
     * @param id
     */
    public void delItemBomSubstitute (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchItemBomSubstitute (List<String> idList);

    /**
     * 
     * 查询替代物料信息
     * 
     * @param queryWrapper
     * @return List<ItemBomSubstitute>
     * @date 2020年10月26日
     */
    List<ItemBomSubstituteVO> queryBomSubstituteMaterialInfoList(QueryWrapper queryWrapper);
}
