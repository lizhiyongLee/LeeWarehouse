package com.ils.modules.mes.base.factory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.factory.entity.ReportTemplate;
import com.ils.modules.mes.base.factory.entity.ReportTemplateControl;
import com.ils.modules.mes.base.factory.mapper.ReportTemplateControlMapper;
import com.ils.modules.mes.base.factory.mapper.ReportTemplateMapper;
import com.ils.modules.mes.base.factory.service.ReportTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 报告模板
 * @Author: Conner
 * @Date: 2020-12-10
 * @Version: V1.0
 */
@Service
public class ReportTemplateServiceImpl extends ServiceImpl<ReportTemplateMapper, ReportTemplate> implements ReportTemplateService {

    @Autowired
    private ReportTemplateControlMapper reportTemplateControlMapper;
    private final String COPY_STR = "_copy";
    private final String CONTROL_TYPE = "group";
    private final String SELECT = "select";


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ReportTemplate saveMain(ReportTemplate reportTemplate, List<ReportTemplateControl> reportTemplateControlList) {
        baseMapper.insert(reportTemplate);
        List<ReportTemplateControl> sonControlList = new ArrayList<>(16);
        List<ReportTemplateControl> fatherControlList = new ArrayList<>(16);

        for (ReportTemplateControl entity : reportTemplateControlList) {
            //替换中文逗号为英文逗号
            if (SELECT.equals(entity.getControlType())){
                entity.setOptionValue(entity.getOptionValue().replace("，",","));
            }
            if (entity.getFatherControlId() != null) {
                ReportTemplateControl reportTemplateControl = new ReportTemplateControl();
                BeanUtils.copyProperties(entity, reportTemplateControl);
                sonControlList.add(reportTemplateControl);
            } else {
                ReportTemplateControl control = new ReportTemplateControl();
                BeanUtils.copyProperties(entity, control);
                fatherControlList.add(control);
            }
        }

        for (ReportTemplateControl templateControl : fatherControlList) {
            templateControl.setReportTemplateId(reportTemplate.getId());
            reportTemplateControlMapper.insert(templateControl);
            if (CONTROL_TYPE.equals(templateControl.getControlType())) {
                for (ReportTemplateControl sonControl : sonControlList) {
                    if (sonControl.getFatherControlId().equals(templateControl.getControlName())) {
                        sonControl.setReportTemplateId(reportTemplate.getId());
                        sonControl.setFatherControlId(templateControl.getId());
                        reportTemplateControlMapper.insert(sonControl);
                    }
                }
            }
        }
        return reportTemplate;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMain(ReportTemplate reportTemplate, List<ReportTemplateControl> reportTemplateControlList) {
        //跟新主表
        baseMapper.updateById(reportTemplate);
        //删除子表
        reportTemplateControlMapper.deleteByMainId(reportTemplate.getId());
        //插入子表
        List<ReportTemplateControl> sonControlList = new ArrayList<>(16);
        List<ReportTemplateControl> fatherControlList = new ArrayList<>(16);

        for (ReportTemplateControl entity : reportTemplateControlList) {
            //替换中文逗号为英文逗号
            if (SELECT.equals(entity.getControlType())){
                entity.setOptionValue(entity.getOptionValue().replace("，",","));
            }
            if (entity.getFatherControlId() != null) {
                ReportTemplateControl reportTemplateControl = new ReportTemplateControl();
                BeanUtils.copyProperties(entity, reportTemplateControl);
                sonControlList.add(reportTemplateControl);
            } else {
                ReportTemplateControl control = new ReportTemplateControl();
                BeanUtils.copyProperties(entity, control);
                fatherControlList.add(control);
            }
        }

        for (ReportTemplateControl templateControl : fatherControlList) {
            templateControl.setReportTemplateId(reportTemplate.getId());
            reportTemplateControlMapper.insert(templateControl);
            if (CONTROL_TYPE.equals(templateControl.getControlType())) {
                for (ReportTemplateControl sonControl : sonControlList) {
                    if (sonControl.getFatherControlId().equals(templateControl.getControlName())) {
                        sonControl.setFatherControlId(templateControl.getId());
                        sonControl.setReportTemplateId(reportTemplate.getId());
                        reportTemplateControlMapper.insert(sonControl);
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void copy(String id) {
        ReportTemplate reportTemplate = baseMapper.selectById(id);
        List<ReportTemplateControl> reportTemplateControlList = reportTemplateControlMapper.selectByMainId(id);
        reportTemplate.setTemplateName(reportTemplate.getTemplateName() + COPY_STR);
        reportTemplate.setId(null);
        baseMapper.insert(reportTemplate);
        for (ReportTemplateControl detail : reportTemplateControlList) {
            detail.setId(null);
            detail.setReportTemplateId(reportTemplate.getId());
        }
        reportTemplateControlMapper.insertBatchSomeColumn(reportTemplateControlList);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMain(String id) {
        //删除主表
        reportTemplateControlMapper.deleteByMainId(id);
        //删除主表
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMain(List<String> idList) {
        for (Serializable id : idList) {
            reportTemplateControlMapper.deleteByMainId(id.toString());
            baseMapper.deleteById(id);
        }
    }

    @Override
    public List<ReportTemplateControl> selectByMainId(String mainId) {
        return reportTemplateControlMapper.selectByMainId(mainId);
    }

    @Override
    public List<DictModel> queryDictTemplate() {
        return baseMapper.queryDictTemplate();
    }
}
