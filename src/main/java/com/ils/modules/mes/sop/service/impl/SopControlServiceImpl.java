package com.ils.modules.mes.sop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.sop.entity.SopControl;
import com.ils.modules.mes.sop.mapper.SopControlMapper;
import com.ils.modules.mes.sop.service.SopControlService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 标准作业任务步骤控件
 * @Author: Conner
 * @Date: 2021-07-16
 * @Version: V1.0
 */
@Service
public class SopControlServiceImpl extends ServiceImpl<SopControlMapper, SopControl> implements SopControlService {

    @Override
    public void saveSopControl(SopControl sopControl) {
        baseMapper.insert(sopControl);
    }

    @Override
    public void updateSopControl(SopControl sopControl) {
        baseMapper.updateById(sopControl);
    }
}
