package com.ils.modules.mes.machine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.machine.entity.SparePartsReceiptHead;
import com.ils.modules.mes.machine.vo.SparePartsReceiptHeadVO;

import java.util.List;

/**
 * @Description: 备件入库表头
 * @Author: Tian
 * @Date:   2021-02-24
 * @Version: V1.0
 */
public interface SparePartsReceiptHeadService extends IService<SparePartsReceiptHead> {

    /**
     * 添加
     * @param sparePartsReceiptHeadVO
     */
    public void saveSparePartsReceiptHead(SparePartsReceiptHeadVO sparePartsReceiptHeadVO) ;
    
    /**
     * 修改
     * @param sparePartsReceiptHead
     */
    public void updateSparePartsReceiptHead(SparePartsReceiptHead sparePartsReceiptHead);
    
    /**
     * 删除
     * @param id
     */
    public void delSparePartsReceiptHead (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchSparePartsReceiptHead (List<String> idList);

    /**
     * 查询入库详情
     * @param headId
     * @return
     */
    public SparePartsReceiptHeadVO queryReceiptDetail(String headId);
}
