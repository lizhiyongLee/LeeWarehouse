package com.ils.modules.mes.base.sop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.sop.entity.SopTemplate;
import com.ils.modules.mes.base.sop.vo.SopTemplateVO;

import java.util.List;

/**
 * @Description: sop模板表头
 * @Author: Tian
 * @Date: 2021-07-15
 * @Version: V1.0
 */
public interface SopTemplateService extends IService<SopTemplate> {

    /**
     * 添加
     *
     * @param sopTemplate
     */
    void saveSopTemplate(SopTemplate sopTemplate);

    /**
     * 修改
     *
     * @param sopTemplateVO
     */
    void updateSopTemplate(SopTemplateVO sopTemplateVO);

    /**
     * 通过模板id查询详情
     * @param id
     * @return
     */
    SopTemplateVO queryById(String id);

    /**
     * 通过控件id查询下个控件所需数据
     * @param controlId
     */
    void queryByTemplateControlId(String controlId);

    /**
     * 停用sop模板
     * @param id
     */
    void stopTemplate(String id);

    /**
     * 启用sop模板
     * @param id
     */
    void startTemplate(String id);

}
