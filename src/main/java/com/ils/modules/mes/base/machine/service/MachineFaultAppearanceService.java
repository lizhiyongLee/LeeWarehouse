package com.ils.modules.mes.base.machine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.machine.entity.MachineFaultAppearance;

import java.util.List;

/**
 * @Description: 故障现象
 * @Author: Tian
 * @Date:   2020-11-10
 * @Version: V1.0
 */
public interface MachineFaultAppearanceService extends IService<MachineFaultAppearance> {

    /**
     * 添加
     * @param machineFaultAppearance
     */
    public void saveMachineFaultAppearance(MachineFaultAppearance machineFaultAppearance) ;
    
    /**
     * 修改
     * @param machineFaultAppearance
     */
    public void updateMachineFaultAppearance(MachineFaultAppearance machineFaultAppearance);


    /**
     * 查询故障现象和故障id,用作数据字典
     * @return
     */
    public List<DictModel> queryDictFaultAppearance();
}
