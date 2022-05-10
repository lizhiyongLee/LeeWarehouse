package com.ils.modules.mes.base.sop.vo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.modules.mes.base.sop.entity.SopTemplate;
import com.ils.modules.mes.base.sop.entity.SopTemplateControl;
import com.ils.modules.mes.base.sop.entity.SopTemplateStep;
import com.ils.modules.mes.base.sop.mapper.SopTemplateControlMapper;
import com.ils.modules.mes.base.sop.mapper.SopTemplateStepMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * sop模板步骤vo类
 *
 * @author Anna.
 * @date 2021/7/15 10:35
 */
@Data
public class SopTemplateStepVO extends SopTemplateStep {

    /**
     * executer
     */
    private String executerName;
    /**
     * 模板控件集合
     */
    private List<SopTemplateControl> sopTemplateControlList;
}
