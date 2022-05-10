package com.ils.modules.mes.base.sop.service;

import java.util.List;
import com.ils.modules.mes.base.sop.entity.SopTemplateControl;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: SOP模板步骤对应控件
 * @Author: Tian
 * @Date:   2021-07-15
 * @Version: V1.0
 */
public interface SopTemplateControlService extends IService<SopTemplateControl> {

    /**
     * 通过主表id删除
     * @param mainId
     */
    void deletedByMainId(String mainId);
}
