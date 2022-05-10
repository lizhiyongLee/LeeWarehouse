package com.ils.modules.mes.base.factory.vo;

import com.ils.modules.mes.base.factory.entity.ReportTemplateControl;
import lombok.*;

import java.util.Map;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/12/14 14:14
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ReportTemplateControlVO extends ReportTemplateControl {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Map<String, Object> extendData;

}
