package com.ils.modules.mes.base.factory.mapper;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.factory.entity.ReportTemplate;

import java.util.List;

/**
 * @Description: 报告模板
 * @Author: Tian
 * @Date:   2020-12-10
 * @Version: V1.0
 */
public interface ReportTemplateMapper extends ILSMapper<ReportTemplate> {
    /**
     * 查询报告模板id和报告模板名字，用作服务化接口数据字段查询
     * @return
     */
    public List<DictModel> queryDictTemplate();

}
