package com.ils.modules.mes.machine.service;

import java.util.List;
import com.ils.modules.mes.machine.entity.SparePartsReceiptLine;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 备件入库明细
 * @Author: Tian
 * @Date:   2021-02-24
 * @Version: V1.0
 */
public interface SparePartsReceiptLineService extends IService<SparePartsReceiptLine> {

    /**
     * 添加
     * @param sparePartsReceiptLine
     */
    public void saveSparePartsReceiptLine(SparePartsReceiptLine sparePartsReceiptLine) ;
    
    /**
     * 修改
     * @param sparePartsReceiptLine
     */
    public void updateSparePartsReceiptLine(SparePartsReceiptLine sparePartsReceiptLine);
    
    /**
     * 删除
     * @param id
     */
    public void delSparePartsReceiptLine (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchSparePartsReceiptLine (List<String> idList);
}
