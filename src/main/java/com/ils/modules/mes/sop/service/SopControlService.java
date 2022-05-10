package com.ils.modules.mes.sop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.sop.entity.SopControl;

import java.util.List;

/**
 * @Description: 标准作业任务步骤控件
 * @Author: Tian
 * @Date: 2021-07-16
 * @Version: V1.0
 */
public interface SopControlService extends IService<SopControl> {

    /**
     * 添加
     *
     * @param sopControl
     */
    void saveSopControl(SopControl sopControl);

    /**
     * 修改
     *
     * @param sopControl
     */
    void updateSopControl(SopControl sopControl);


}
