package com.ils.modules.mes.base.craft.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.craft.entity.Parameter;
import com.ils.modules.mes.base.craft.mapper.ParameterMapper;
import com.ils.modules.mes.base.craft.service.ParameterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 参数管理
 * @Author: Conner
 * @Date: 2021-10-15
 * @Version: V1.0
 */
@Service
public class ParameterServiceImpl extends ServiceImpl<ParameterMapper, Parameter> implements ParameterService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveParameter(Parameter parameter) {
        baseMapper.insert(parameter);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateParameter(Parameter parameter) {
        baseMapper.updateById(parameter);
    }
}
