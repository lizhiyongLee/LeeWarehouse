package com.ils.modules.mes.base.qc.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.qc.entity.NgItem;
import com.ils.modules.mes.base.qc.vo.NgItemVO;

/**
 * @Description: 不良类型原因
 * @Author: fengyi
 * @Date: 2020-10-19
 * @Version: V1.0
 */
public interface NgItemService extends IService<NgItem> {

    /**
     * 添加
     * @param ngItem
     */
    public void saveNgItem(NgItem ngItem) ;
    
    /**
     * 修改
     * @param ngItem
     */
    public void updateNgItem(NgItem ngItem);
    
    /**
     * 删除
     * @param id
     */
    public void delNgItem (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchNgItem (List<String> idList);

    /**
     * 
     * 查询不良品信息
     * 
     * @param page
     * @param queryWrapper
     * @return IPage
     * @date 2020年7月28日O
     */
    IPage<NgItemVO> queryPageList(IPage<NgItemVO> page, @Param("ew") QueryWrapper<NgItemVO> queryWrapper);
}
