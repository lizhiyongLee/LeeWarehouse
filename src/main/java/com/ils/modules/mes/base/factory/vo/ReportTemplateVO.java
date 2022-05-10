package com.ils.modules.mes.base.factory.vo;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.factory.entity.ReportTemplate;
import com.ils.modules.mes.base.factory.entity.ReportTemplateControl;
import lombok.*;

import java.util.List;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/12/14 13:22
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ReportTemplateVO extends ReportTemplate {

    private static final long serialVersionUID = 1L;
    @ExcelCollection(name = "报告明细")

    private List<ReportTemplateControl> reportTemplateControlList;

}
