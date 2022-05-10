package com.ils.modules.mes.base.material.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.material.entity.ItemSupplier;
import com.ils.modules.mes.base.material.vo.ItemSupplierVO;

/**
 * @Description: 物料关联供应商
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
public interface ItemSupplierService extends IService<ItemSupplier> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
    public List<ItemSupplierVO> selectByMainId(String mainId);
}
