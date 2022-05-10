package com.ils.modules.mes.base.sop.vo;

import com.ils.modules.mes.base.sop.entity.SopTemplate;
import lombok.Data;

import java.util.List;

/**
 * sop模板vo类
 *
 * @author Anna.
 * @date 2021/7/15 10:29
 */
@Data
public class SopTemplateVO extends SopTemplate {
    /**
     * sop步骤集合
     */
    private List<SopTemplateStepVO> sopTemplateStepVOList;
}
