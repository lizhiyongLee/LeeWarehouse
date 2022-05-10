package com.ils.modules.mes.machine.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.machine.entity.SpareParts;
import com.ils.modules.mes.machine.vo.SparePartsQtyVO;
import com.ils.modules.mes.machine.vo.SparePartsReceiptOrSendOrderVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 备件定义
 * @Author: Tian
 * @Date: 2021-02-23
 * @Version: V1.0
 */
public interface SparePartsMapper extends ILSMapper<SpareParts> {
    /**
     * 分页查询详情
     *
     * @param id
     * @return
     */
    public List<SparePartsReceiptOrSendOrderVO> querySparePartsRelate(String id);

    /**
     * 通过备件id分页查询在某个仓库下该备件的数量
     * @param id
     * @return
     */
    public List<SparePartsQtyVO> querySparePartsQtyWithStorage(String id);

    /**
     * 查询仓位下拥有的备件数量
     * @param storageId
     * @param sparePartsId
     * @return
     */
    public BigDecimal queryQtyByStorageIdAndSparePartId(String storageId, String sparePartsId);
}
