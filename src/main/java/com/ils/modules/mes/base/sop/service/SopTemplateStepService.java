package com.ils.modules.mes.base.sop.service;

import java.util.List;
import com.ils.modules.mes.base.sop.entity.SopTemplateStep;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: SOP模板步骤
 * @Author: Tian
 * @Date:   2021-07-15
 * @Version: V1.0
 */
public interface SopTemplateStepService extends IService<SopTemplateStep> {
    /**
     * 通过主表id删除
     * @param mainId
     */
    void deletedByMainId(String mainId);

}
