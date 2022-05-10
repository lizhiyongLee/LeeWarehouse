package com.ils.modules.mes.base.factory.service.impl;

import com.ils.modules.mes.base.factory.entity.ReportTemplateControl;
import com.ils.modules.mes.base.factory.mapper.ReportTemplateControlMapper;
import com.ils.modules.mes.base.factory.service.ReportTemplateControlService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 报告模板控件
 * @Author: Conner
 * @Date:   2020-12-10
 * @Version: V1.0
 */
@Service
public class ReportTemplateControlServiceImpl extends ServiceImpl<ReportTemplateControlMapper, ReportTemplateControl> implements ReportTemplateControlService {

    @Override
    public void saveReportTemplateControl(ReportTemplateControl reportTemplateControl) {
         baseMapper.insert(reportTemplateControl);
    }

    @Override
    public void updateReportTemplateControl(ReportTemplateControl reportTemplateControl) {
        baseMapper.updateById(reportTemplateControl);
    }

    @Override
    public void delReportTemplateControl(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void delBatchReportTemplateControl(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
