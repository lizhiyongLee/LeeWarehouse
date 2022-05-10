package com.ils.modules.mes.base.qc.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.qc.entity.QcItem;
import com.ils.modules.mes.base.qc.vo.QcItemVO;

/**
 * @Description: 质检项
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
public interface QcItemService extends IService<QcItem> {

    /**
     * 添加
     * @param qcItem
     */
    public void saveQcItem(QcItem qcItem) ;
    
    /**
     * 修改
     * @param qcItem
     */
    public void updateQcItem(QcItem qcItem);
    
    /**
     * 删除
     * @param id
     */
    public void delQcItem (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchQcItem (List<String> idList);

    /**
     * 
     * 查询质检项信息
     * 
     * @param page
     * @param queryWrapper
     * @return IPage
     * @date 2020年7月28日O
     */
    IPage<QcItemVO> queryPageList(IPage<QcItemVO> page, QueryWrapper<QcItemVO> queryWrapper);

}
