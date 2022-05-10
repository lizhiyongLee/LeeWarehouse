package com.ils.modules.mes.base.craft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.craft.entity.ParaTemplateHead;
import com.ils.modules.mes.base.craft.vo.ParameterTemplateVO;

/**
 * @Description: 参数模板主表
 * @Author: Tian
 * @Date: 2021-10-15
 * @Version: V1.0
 */
public interface ParaTemplateHeadService extends IService<ParaTemplateHead> {

    /**
     * 添加
     *
     * @param parameterTemplateVO
     */
    void saveParaTemplateHead(ParameterTemplateVO parameterTemplateVO);

    /**
     * 修改
     *
     * @param parameterTemplateVO
     */
    void updateParaTemplateHead(ParameterTemplateVO parameterTemplateVO);

    /**
     * 通过id查询详情
     *
     * @param id
     */
    ParameterTemplateVO queryById(String id);

}
