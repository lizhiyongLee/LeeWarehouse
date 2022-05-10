package com.ils.modules.mes.base.craft.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.craft.entity.ProcessQcMethod;
import com.ils.modules.mes.base.craft.vo.ProcessQcMethodVO;

/**
 * @Description: 工序关联质检
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
public interface ProcessQcMethodService extends IService<ProcessQcMethod> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
    public List<ProcessQcMethodVO> selectByMainId(String mainId);
}
