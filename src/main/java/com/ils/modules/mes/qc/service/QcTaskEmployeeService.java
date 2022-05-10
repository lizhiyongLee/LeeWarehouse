package com.ils.modules.mes.qc.service;

import java.util.List;
import com.ils.modules.mes.qc.entity.QcTaskEmployee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 质检任务关联人员
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
public interface QcTaskEmployeeService extends IService<QcTaskEmployee> {

    /**
     * 添加
     * @param qcTaskEmployee
     */
    public void saveQcTaskEmployee(QcTaskEmployee qcTaskEmployee) ;
    
    /**
     * 修改
     * @param qcTaskEmployee
     */
    public void updateQcTaskEmployee(QcTaskEmployee qcTaskEmployee);
    
    /**
     * 删除
     * @param id
     */
    public void delQcTaskEmployee (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchQcTaskEmployee (List<String> idList);
}
