package com.ils.modules.mes.base.craft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.craft.entity.Parameter;

import java.util.List;

/**
 * @Description: 参数管理
 * @Author: Tian
 * @Date: 2021-10-15
 * @Version: V1.0
 */
public interface ParameterService extends IService<Parameter> {

    /**
     * 添加
     *
     * @param parameter
     */
    void saveParameter(Parameter parameter);

    /**
     * 修改
     *
     * @param parameter
     */
    void updateParameter(Parameter parameter);

}
