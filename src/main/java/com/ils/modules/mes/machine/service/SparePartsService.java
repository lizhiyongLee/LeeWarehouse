package com.ils.modules.mes.machine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.machine.entity.SpareParts;
import com.ils.modules.mes.machine.vo.SparePartsQtyVO;
import com.ils.modules.mes.machine.vo.SparePartsReceiptOrSendOrderVO;

import java.util.List;

/**
 * @Description: 备件定义
 * @Author: Tian
 * @Date:   2021-02-23
 * @Version: V1.0
 */
public interface SparePartsService extends IService<SpareParts> {

    /**
     * 添加
     * @param spareParts
     */
    public void saveSpareParts(SpareParts spareParts) ;
    
    /**
     * 修改
     * @param spareParts
     */
    public void updateSpareParts(SpareParts spareParts);
    
    /**
     * 删除
     * @param id
     */
    public void delSpareParts (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchSpareParts (List<String> idList);

    /**
     * 分页查询
     * @param id
     * @return
     */
    public List<SparePartsReceiptOrSendOrderVO> querySparePartsRelate(String id);

    /**
     * 通过备件id查询某仓库下的备件数量
     * @param id
     * @return
     */
    public List<SparePartsQtyVO> querySparePartsQtyWithStorage(String id);
}
