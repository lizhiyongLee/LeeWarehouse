package com.ils.modules.mes.base.sop.service.impl;

import com.ils.modules.mes.base.sop.entity.SopTemplateStep;
import com.ils.modules.mes.base.sop.mapper.SopTemplateStepMapper;
import com.ils.modules.mes.base.sop.service.SopTemplateStepService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: SOP模板步骤
 * @Author: Conner
 * @Date:   2021-07-15
 * @Version: V1.0
 */
@Service
public class SopTemplateStepServiceImpl extends ServiceImpl<SopTemplateStepMapper, SopTemplateStep> implements SopTemplateStepService {

    @Override
    public void deletedByMainId(String id) {

    }
}
