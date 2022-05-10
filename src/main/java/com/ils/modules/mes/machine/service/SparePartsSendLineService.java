package com.ils.modules.mes.machine.service;

import java.util.List;
import com.ils.modules.mes.machine.entity.SparePartsSendLine;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 备件入库明细
 * @Author: Tian
 * @Date:   2021-02-24
 * @Version: V1.0
 */
public interface SparePartsSendLineService extends IService<SparePartsSendLine> {

    /**
     * 添加
     * @param sparePartsSendLine
     */
    public void saveSparePartsSendLine(SparePartsSendLine sparePartsSendLine) ;
    
    /**
     * 修改
     * @param sparePartsSendLine
     */
    public void updateSparePartsSendLine(SparePartsSendLine sparePartsSendLine);
    
    /**
     * 删除
     * @param id
     */
    public void delSparePartsSendLine (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchSparePartsSendLine (List<String> idList);
}
