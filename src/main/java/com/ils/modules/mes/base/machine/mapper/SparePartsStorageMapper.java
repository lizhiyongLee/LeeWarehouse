package com.ils.modules.mes.base.machine.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.machine.entity.SparePartsStorage;
import com.ils.modules.mes.base.machine.vo.SparePartsStorageVO;

/**
 * @Description: 备件库位
 * @Author: Tian
 * @Date:   2021-02-24
 * @Version: V1.0
 */
public interface SparePartsStorageMapper extends ILSMapper<SparePartsStorage> {

    /**
     * 分页查询仓库
     * @param page
     * @return
     */
    public IPage<SparePartsStorageVO> querySparePartsHouse(Page<SparePartsStorageVO> page);
}
