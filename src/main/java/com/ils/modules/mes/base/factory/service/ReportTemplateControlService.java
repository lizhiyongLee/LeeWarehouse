package com.ils.modules.mes.base.factory.service;

import java.util.List;
import com.ils.modules.mes.base.factory.entity.ReportTemplateControl;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 报告模板控件
 * @Author: Tian
 * @Date:   2020-12-10
 * @Version: V1.0
 */
public interface ReportTemplateControlService extends IService<ReportTemplateControl> {

    /**
     * 添加
     * @param reportTemplateControl
     */
    public void saveReportTemplateControl(ReportTemplateControl reportTemplateControl) ;
    
    /**
     * 修改
     * @param reportTemplateControl
     */
    public void updateReportTemplateControl(ReportTemplateControl reportTemplateControl);
    
    /**
     * 删除
     * @param id
     */
    public void delReportTemplateControl (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchReportTemplateControl (List<String> idList);
}
