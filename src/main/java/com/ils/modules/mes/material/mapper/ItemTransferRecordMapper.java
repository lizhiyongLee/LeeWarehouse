package com.ils.modules.mes.material.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.material.entity.ItemTransferRecord;

import java.util.List;

/**
 * @Description: 转移记录
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
public interface ItemTransferRecordMapper extends ILSMapper<ItemTransferRecord> {

    /**
     * 入库时查询选择的位置下面有哪些物料单元
     * @param code
     * @return
     */
    public List<ItemTransferRecord> queryInStocksItemTramTransferRecord(String code);
}
