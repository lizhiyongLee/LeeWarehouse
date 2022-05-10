package com.ils.modules.mes.base.sop.service.impl;

import com.ils.modules.mes.base.sop.entity.SopTemplateControl;
import com.ils.modules.mes.base.sop.mapper.SopTemplateControlMapper;
import com.ils.modules.mes.base.sop.service.SopTemplateControlService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: SOP模板步骤对应控件
 * @Author: Conner
 * @Date:   2021-07-15
 * @Version: V1.0
 */
@Service
public class SopTemplateControlServiceImpl extends ServiceImpl<SopTemplateControlMapper, SopTemplateControl> implements SopTemplateControlService {

    @Override
    public void deletedByMainId(String mainId) {

    }
}
