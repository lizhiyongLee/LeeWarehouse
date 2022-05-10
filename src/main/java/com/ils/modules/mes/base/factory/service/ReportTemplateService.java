package com.ils.modules.mes.base.factory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.factory.entity.ReportTemplate;
import com.ils.modules.mes.base.factory.entity.ReportTemplateControl;

import java.util.List;

/**
 * @Description: 报告模板
 * @Author: Tian
 * @Date:   2020-12-10
 * @Version: V1.0
 */
public interface ReportTemplateService extends IService<ReportTemplate> {

    /**
     * 添加报告模板
     * @param reportTemplate
     * @param reportTemplateControlList
     */
    public ReportTemplate saveMain(ReportTemplate reportTemplate, List<ReportTemplateControl> reportTemplateControlList);

    /**
     * 更新报告模板
     * @param reportTemplate
     * @param reportTemplateControlList
     */
    public void updateMain(ReportTemplate reportTemplate, List<ReportTemplateControl> reportTemplateControlList);

    /**
     * 复制，通过id
     * @param id
     */
    public void copy(String id);

    /**
     * 通过id删除
     * @param id
     */
    public void delMain(String id);

    /**
     * 通过id批量删除
     * @param idList
     */
    public void delBatchMain(List<String> idList);


    /**
     * 通过主表id查询报告模板明细
     * @param mainId
     * @return
     */
    public List<ReportTemplateControl> selectByMainId(String mainId);

    /**
     * 查询报告模板id和报告模板名字，用作服务化接口数据字段查询
     * @return
     */
    public List<DictModel> queryDictTemplate();
}
