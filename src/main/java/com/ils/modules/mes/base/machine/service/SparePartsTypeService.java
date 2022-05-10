package com.ils.modules.mes.base.machine.service;

import java.util.List;
import com.ils.modules.mes.base.machine.entity.SparePartsType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 备件类型
 * @Author: Tian
 * @Date:   2021-02-23
 * @Version: V1.0
 */
public interface SparePartsTypeService extends IService<SparePartsType> {

    /**
     * 添加
     * @param sparePartsType
     */
    public void saveSparePartsType(SparePartsType sparePartsType) ;
    
    /**
     * 修改
     * @param sparePartsType
     */
    public void updateSparePartsType(SparePartsType sparePartsType);

}
