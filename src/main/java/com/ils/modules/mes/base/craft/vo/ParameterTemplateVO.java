package com.ils.modules.mes.base.craft.vo;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.craft.entity.ParaTemplateDetail;
import com.ils.modules.mes.base.craft.entity.ParaTemplateHead;
import lombok.Data;

import java.util.List;

/**
 * 参数模板类
 *
 * @author Anna.
 * @date 2021/10/15 13:44
 */
@Data
public class ParameterTemplateVO extends ParaTemplateHead {
    @ExcelCollection(name = "参数项列表")
    private List<ParaTemplateDetail> paraTemplateDetailList;
}
