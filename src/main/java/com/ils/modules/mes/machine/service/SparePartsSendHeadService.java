package com.ils.modules.mes.machine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.machine.entity.SparePartsSendHead;
import com.ils.modules.mes.machine.vo.SparePartsSendHeadVO;

import java.util.List;

/**
 * @Description: 备件入库表头
 * @Author: Tian
 * @Date:   2021-02-24
 * @Version: V1.0
 */
public interface SparePartsSendHeadService extends IService<SparePartsSendHead> {

    /**
     * 添加
     * @param sparePartsSendHeadVO
     */
    public void saveSparePartsSendHead(SparePartsSendHeadVO sparePartsSendHeadVO) ;
    
    /**
     * 修改
     * @param sparePartsSendHead
     */
    public void updateSparePartsSendHead(SparePartsSendHead sparePartsSendHead);
    
    /**
     * 删除
     * @param id
     */
    public void delSparePartsSendHead (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchSparePartsSendHead (List<String> idList);
}
